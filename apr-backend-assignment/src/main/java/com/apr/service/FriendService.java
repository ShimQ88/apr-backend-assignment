package com.apr.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apr.entity.Friend;
import com.apr.entity.FriendRequest;
import com.apr.repository.FriendRepository;
import com.apr.repository.FriendRequestRepository;

@Service
public class FriendService {

    private final FriendRepository friendRepo;
    private final FriendRequestRepository friendRequestRepo;

    public FriendService(FriendRepository friendRepo, FriendRequestRepository friendRequestRepo) {
        this.friendRepo = friendRepo;
        this.friendRequestRepo = friendRequestRepo;
    }

    public List<Friend> listFriends(Long userId, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "approvedAt"));
        return friendRepo.findByUserId(userId, pageable).getContent();
    }

    public List<FriendRequest> listIncomingRequests(Long userId, Instant since, int limit) {
        return friendRequestRepo.findIncomingSince(userId, since, PageRequest.of(0, limit));
    }

    @Transactional
    public Friend acceptFriendRequest(Long requestUserId, Long targetUserId) {
        if (requestUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot accept your own request.");
        }

        var pending = friendRequestRepo.findByRequestUserIdAndTargetUserId(requestUserId, targetUserId)
                .orElseThrow(() -> new IllegalStateException("Pending request not found."));

        boolean alreadyFriends =
                friendRepo.existsByFromUserIdAndToUserId(requestUserId, targetUserId) ||
                friendRepo.existsByFromUserIdAndToUserId(targetUserId, requestUserId);
        if (alreadyFriends) {
            throw new IllegalStateException("Users are already friends.");
        }

        Friend f = new Friend();
        f.setFromUserId(requestUserId);
        f.setToUserId(targetUserId);
        f.setApprovedAt(Instant.now());
        Friend saved = friendRepo.save(f);

        friendRequestRepo.delete(pending); // or deleteByFromTo(requestUserId, targetUserId)
        return saved;
    }

    @Transactional
    public void rejectFriendRequest(Long requestUserId, Long targetUserId) {
        if (requestUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot reject your own request.");
        }

        var pending = friendRequestRepo.findByRequestUserIdAndTargetUserId(requestUserId, targetUserId)
                .orElseThrow(() -> new IllegalStateException("Pending request not found."));

        friendRequestRepo.delete(pending); // or deleteByFromTo(requestUserId, targetUserId)
    }
}
