// Friend.java
package com.apr.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
  name = "friends",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_friends_pair",
    columnNames = {"from_user_id", "to_user_id"}
  )
)
public class Friend {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="from_user_id", nullable=false)
  private Long fromUserId;

  @Column(name="to_user_id", nullable=false)
  private Long toUserId;

  @Column(name="approved_at", nullable=false)
  private Instant approvedAt;

  // getters/setters...
}