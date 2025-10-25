package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.common.util.DateUtil;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;
import com.econo_4factorial.newproject.travel.exception.TravelRecordNotFoundException;
import com.econo_4factorial.newproject.travel.mapper.TravelMapper;
import com.econo_4factorial.newproject.travel.repository.TravelRecordRepository;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelRecordService {
    private final TravelRecordRepository travelRecordRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public List<TravelRecordDTO> findRecordByMonth(Long userId, Integer year, Integer month) {
        LocalDateTime startOfMonth = DateUtil.getStartOfYearAndMonth(year, month);
        LocalDateTime endOfMonth = DateUtil.getEndOfYearAndMonth(year, month);

        return travelRecordRepository.findAllByIdAndStartedAtBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .map(TravelRecordDTO::from)
                .toList();
    }

    public void saveTravelRecord(TravelTrackingInfo info) {
        User user = userService.findUserByIdOrThrow(info.getUserId());
        Course course = courseService.findByIdOrThrow(info.getCourseId());
        String displayNameOfRecord = course.getDisplayName();
        travelRecordRepository.save(TravelMapper.toRecord(user, course, info, displayNameOfRecord));
    }

    @Transactional(readOnly = true)
    public TravelRecordDetailDTO findRecordById(Long recordId) {
        return travelRecordRepository.findById(recordId)
                .map(TravelRecordDetailDTO::from)
                .orElseThrow(TravelRecordNotFoundException::new);
    }
}
