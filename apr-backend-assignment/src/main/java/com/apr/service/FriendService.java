package com.apr.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    /**
     * 친구 목록 조회 (최신순)
     */
    public List<Friend> listFriends(Long userId, int page, int size) {
        // approvedAt 최신순 보장
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "approvedAt"));
        return friendRepo.findByUserId(userId, pageable).getContent();
    }

    /**
     * 받은 친구 신청 목록 조회 (슬라이딩 윈도우 방식)
     */
    public List<FriendRequest> listIncomingRequests(Long userId, Instant since, int limit) {
        return friendRequestRepo.findIncomingSince(userId, since, PageRequest.of(0, limit));
    }

    public FriendRequest sendFriendRequest(Long fromUserId, Long targetUserId) {
        // 1) 자기 자신 금지
        if (fromUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot request friendship to yourself.");
        }

        // 2) 이미 친구인지(무방향) 확인
        boolean alreadyFriends =
                friendRepo.existsByFromUserIdAndToUserId(fromUserId, targetUserId)
             || friendRepo.existsByFromUserIdAndToUserId(targetUserId, fromUserId);
        if (alreadyFriends) {
            throw new IllegalStateException("Users are already friends.");
        }

        // 3) 동일 대기 요청 있는지 확인 (from -> to)
        if (friendRequestRepo.existsByRequestUserIdAndTargetUserId(fromUserId, targetUserId)) {
            throw new IllegalStateException("A pending request already exists.");
        }

        // (선택) 교차 대기 요청 방지: 상대가 이미 보냈다면 굳이 또 만들지 않도록
        if (friendRequestRepo.existsByRequestUserIdAndTargetUserId(targetUserId, fromUserId)) {
            throw new IllegalStateException("Counter pending request exists.");
        }

        // 4) 생성
        FriendRequest fr = new FriendRequest();
        fr.setRequestUserId(fromUserId);
        fr.setTargetUserId(targetUserId);
        fr.setRequestedAt(Instant.now());
        return friendRequestRepo.save(fr);
    }
}
