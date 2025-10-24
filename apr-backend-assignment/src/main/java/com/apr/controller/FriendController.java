package com.apr.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apr.dto.FriendDto;
import com.apr.dto.FriendRequestDto;
import com.apr.entity.Friend;
import com.apr.entity.FriendRequest;
import com.apr.service.FriendService;
import com.apr.util.TimeWindowParser;

@RestController
@RequestMapping("/api")
public class FriendController {

    private final FriendService service;

    public FriendController(FriendService service) {
        this.service = service;
    }

    @GetMapping("/friends")
    public List<FriendDto> listFriends(@RequestParam Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        List<Friend> list = service.listFriends(userId, page, size);
        return list.stream().map(FriendDto::new).collect(Collectors.toList());
    }

    @GetMapping("/friends/requests")
    public List<FriendRequestDto> listIncoming(@RequestParam Long userId,
                                               @RequestParam String since,
                                               @RequestParam(defaultValue = "20") int limit) {
        Instant sinceInstant = TimeWindowParser.parse(since);
        List<FriendRequest> list = service.listIncomingRequests(userId, sinceInstant, limit);
        return list.stream().map(FriendRequestDto::new).collect(Collectors.toList());
    }

     // DTO 요청 바디
    public static class FriendRequestCreate {
        public Long fromUserId;
        public Long targetUserId;
    }

    @PostMapping("/friends/request")
    public FriendRequestDto createRequest(@RequestBody FriendRequestCreate body) {
        try {
            FriendRequest saved = service.sendFriendRequest(body.fromUserId, body.targetUserId);
            return new FriendRequestDto(saved);
        } catch (IllegalArgumentException e) {
            // 400 잘못된 요청
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // 409 충돌 (중복/이미 친구 등)
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
