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
@RequestMapping("/api/friends")
public class DevController {

    private final FriendService service;

    public DevController(FriendService service) {
        this.service = service;
    }

    @GetMapping
    public List<Friend> listFriends(@RequestParam Long userId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        return service.listFriends(userId, page, size);
    }

    @GetMapping("/requests")
    public List<FriendRequest> listIncoming(@RequestParam Long userId,
                                            @RequestParam String since,
                                            @RequestParam(defaultValue = "5") int limit) {
        return service.listIncomingRequests(userId, Instant.parse(since), limit);
    }
}
