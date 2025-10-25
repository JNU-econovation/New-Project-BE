package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.common.util.DateUtil;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.repository.TravelRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelRecordService {
    private final TravelRecordRepository travelRecordRepository;

    public List<TravelRecordDTO> findRecordByMonth(Long userId, Integer year, Integer month) {
        LocalDateTime startOfMonth = DateUtil.getStartOfYearAndMonth(year, month);
        LocalDateTime endOfMonth = DateUtil.getEndOfYearAndMonth(year, month);

        return travelRecordRepository.findAllByIdAndStartedAtBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .map(TravelRecordDTO::from)
                .toList();
    }
}
