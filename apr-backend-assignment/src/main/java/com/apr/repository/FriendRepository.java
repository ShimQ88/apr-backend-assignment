// FriendRepository.java
package com.apr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.apr.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
  Page<Friend> findByUserId(@Param("userId") Long userId, Pageable pageable);
  
  boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
