package com.apr.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    // --- 기존 GET 2개는 그대로 ---

    public static class AcceptRejectBody {
        public Long requestUserId;
        public Long targetUserId;
    }

    @PostMapping("/friends/accept")
    public FriendDto accept(@RequestBody AcceptRejectBody body) {
        try {
            Friend f = service.acceptFriendRequest(body.requestUserId, body.targetUserId);
            return new FriendDto(f);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // pending 없음 / 이미 친구
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PostMapping("/friends/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@RequestBody AcceptRejectBody body) {
        try {
            service.rejectFriendRequest(body.requestUserId, body.targetUserId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // pending 없음
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // --- 아래는 기존 목록 엔드포인트 예시 (참고) ---
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
}
