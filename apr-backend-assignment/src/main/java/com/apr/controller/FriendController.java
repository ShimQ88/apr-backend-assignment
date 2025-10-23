package com.apr.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apr.entity.Friend;
import com.apr.entity.FriendRequest;
import com.apr.service.FriendService;

@RestController
@RequestMapping("/api")
public class FriendController {

    private final FriendService service;

    public FriendController(FriendService service) {
        this.service = service;
    }

    /**
     * 친구 목록 조회 (approvedAt DESC, 페이지네이션)
     * 예) /api/friends?userId=1&page=0&size=10
     */
    @GetMapping("/friends")
    public List<Friend> listFriends(@RequestParam Long userId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return service.listFriends(userId, page, size);
    }

    /**
     * 받은 친구 신청 목록 조회 (since 이후, 최신순, limit)
     * 예) /api/friends/requests?userId=1&since=2024-12-31T00:00:00Z&limit=20
     */
    @GetMapping("/friends/requests")
    public List<FriendRequest> listIncoming(@RequestParam Long userId,
                                            @RequestParam String since,
                                            @RequestParam(defaultValue = "20") int limit) {
        Instant s = Instant.parse(since); // 최소 구현: ISO-8601만 받음 (1d/7d 파서는 다음 패치)
        return service.listIncomingRequests(userId, s, limit);
    }
}
