package com.econo_4factorial.newproject.travel.repository;

import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {

    List<TravelRecord> findAllByUserIdAndStartedAtBetween(Long userId, LocalDateTime startOfMonth,
                                                          LocalDateTime endOfMonth);

    Optional<TravelRecord> findByIdAndUserId(Long recordId, Long userId);
}
