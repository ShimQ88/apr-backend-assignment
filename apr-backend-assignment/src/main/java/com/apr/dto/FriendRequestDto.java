package com.apr.dto;

import java.time.Instant;

import com.apr.entity.FriendRequest;

public class FriendRequestDto {
    private String id;
    private Long requestUserId;
    private Long targetUserId;
    private Instant requestedAt;

    public FriendRequestDto(FriendRequest fr) {
        this.id = fr.getId();
        this.requestUserId = fr.getRequestUserId();
        this.targetUserId = fr.getTargetUserId();
        this.requestedAt = fr.getRequestedAt();
    }

    public String getId() { return id; }
    public Long getRequestUserId() { return requestUserId; }
    public Long getTargetUserId() { return targetUserId; }
    public Instant getRequestedAt() { return requestedAt; }
}
