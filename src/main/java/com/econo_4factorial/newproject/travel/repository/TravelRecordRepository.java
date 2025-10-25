package com.econo_4factorial.newproject.travel.repository;

import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {

    List<TravelRecord> findAllByIdAndStartedAtBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
