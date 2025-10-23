package com.apr.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "friend_requests",
    indexes = {
        @Index(name = "idx_fr_requested_at", columnList = "requested_at"),
        @Index(name = "idx_fr_target_reqat", columnList = "target_user_id, requested_at DESC")
    }
)
public class FriendRequest {

    @Id
    @Column(length = 36, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "request_user_id", nullable = false)
    private Long requestUserId;   // 보낸 사람

    @Column(name = "target_user_id", nullable = false)
    private Long targetUserId;    // 받은 사람

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt = Instant.now();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getRequestUserId() { return requestUserId; }
    public void setRequestUserId(Long requestUserId) { this.requestUserId = requestUserId; }
    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public Instant getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Instant requestedAt) { this.requestedAt = requestedAt; }
}
