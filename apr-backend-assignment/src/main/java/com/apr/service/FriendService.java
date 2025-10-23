package com.apr.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.PageRequest;
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
        return friendRepo.findByUserId(userId, PageRequest.of(page, size)).getContent();
    }

    /**
     * 받은 친구 신청 목록 조회 (슬라이딩 윈도우 방식)
     */
    public List<FriendRequest> listIncomingRequests(Long userId, Instant since, int limit) {
        return friendRequestRepo.findIncomingSince(userId, since, PageRequest.of(0, limit));
    }
}
