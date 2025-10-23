package com.apr.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apr.entity.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    @Query("""
        select fr from FriendRequest fr
        where fr.targetUserId = :userId
          and fr.requestedAt >= :since
        order by fr.requestedAt desc
    """)
    List<FriendRequest> findIncomingSince(
            @Param("userId") Long userId,
            @Param("since") Instant since,
            Pageable pageable
    );
}
