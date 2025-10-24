package com.apr.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    boolean existsByRequestUserIdAndTargetUserId(Long requestUserId, Long targetUserId);

    Optional<FriendRequest> findByRequestUserIdAndTargetUserId(Long requestUserId, Long targetUserId);

    @Modifying
    @Query("delete from FriendRequest fr where fr.requestUserId = :from and fr.targetUserId = :to")
    void deleteByFromTo(@Param("from") Long from, @Param("to") Long to);
}
