package com.apr;

import java.time.Instant;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apr.entity.Friend;
import com.apr.entity.FriendRequest;
import com.apr.repository.FriendRepository;
import com.apr.repository.FriendRequestRepository;
import com.apr.service.FriendService;

@Configuration
public class DevRunner {

    @Bean
    ApplicationRunner run(FriendService service,
                          FriendRepository friendRepo,
                          FriendRequestRepository frRepo) {
        return args -> {
            // 🧱 샘플 데이터 입력
            friendRepo.save(friend(1L, 2L, "2025-01-01T00:00:00Z"));
            friendRepo.save(friend(3L, 1L, "2025-01-03T00:00:00Z"));
            friendRepo.save(friend(1L, 4L, "2025-01-02T00:00:00Z"));

            frRepo.save(req(10L, 1L, "2025-01-01T00:00:00Z"));
            frRepo.save(req(11L, 1L, "2025-01-03T00:00:00Z"));
            frRepo.save(req(12L, 1L, "2025-01-02T00:00:00Z"));

            // 🧭 서비스 호출 후 콘솔 출력
            System.out.println("\n=== listFriends(1) ===");
            service.listFriends(1L, 0, 5)
                   .forEach(f -> System.out.println("Friend " + f.getFromUserId() + "-" + f.getToUserId() + " @" + f.getApprovedAt()));

            System.out.println("\n=== listIncomingRequests(1) ===");
            service.listIncomingRequests(1L, Instant.parse("2024-12-31T00:00:00Z"), 3)
                   .forEach(r -> System.out.println("Request from " + r.getRequestUserId() + " @" + r.getRequestedAt()));
        };
    }

    private Friend friend(Long from, Long to, String iso) {
        Friend f = new Friend();
        f.setFromUserId(from);
        f.setToUserId(to);
        f.setApprovedAt(Instant.parse(iso));
        return f;
    }

    private FriendRequest req(Long from, Long to, String iso) {
        FriendRequest r = new FriendRequest();
        r.setRequestUserId(from);
        r.setTargetUserId(to);
        r.setRequestedAt(Instant.parse(iso));
        return r;
    }

    private void saveFriendIfAbsent(FriendRepository repo, Long from, Long to, Instant at) {
        if (!repo.existsByFromUserIdAndToUserId(from, to)) {
            Friend f = new Friend();
            f.setFromUserId(from);
            f.setToUserId(to);
            f.setApprovedAt(at);
            repo.save(f);
        }
    }

    private void saveReqIfAbsent(FriendRequestRepository repo, Long from, Long to, String iso) {
        if (!repo.existsByRequestUserIdAndTargetUserId(from, to)) {
            FriendRequest r = new FriendRequest();
            r.setRequestUserId(from);
            r.setTargetUserId(to);
            r.setRequestedAt(Instant.parse(iso));
            repo.save(r);
        }
    }
}
