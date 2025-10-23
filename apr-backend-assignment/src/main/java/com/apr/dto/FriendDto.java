package com.apr.dto;

import java.time.Instant;

import com.apr.entity.Friend;

public class FriendDto {
    private Long fromUserId;
    private Long toUserId;
    private Instant approvedAt;

    public FriendDto(Friend f) {
        this.fromUserId = f.getFromUserId();
        this.toUserId = f.getToUserId();
        this.approvedAt = f.getApprovedAt();
    }

    public Long getFromUserId() { return fromUserId; }
    public Long getToUserId() { return toUserId; }
    public Instant getApprovedAt() { return approvedAt; }
}
