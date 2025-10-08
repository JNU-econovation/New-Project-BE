package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.common.util.TimeMapper;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.dto.Payload;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import com.econo_4factorial.newproject.travel.dto.req.*;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import com.econo_4factorial.newproject.travel.exception.NotAllowedEventForStatusException;
import com.econo_4factorial.newproject.travel.mapper.TravelMapper;
import com.econo_4factorial.newproject.travel.repository.TravelRecordRepository;
import com.econo_4factorial.newproject.travel.util.GeoUtil;
import com.econo_4factorial.newproject.travel.util.EventPolicy;
import com.econo_4factorial.newproject.travel.util.PayloadMapper;
import com.econo_4factorial.newproject.travel.util.TravelResponseMapper;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelService {
    private final CourseService courseService;
    private final UserService userService;
    private Map<TravelEvent, BiFunction<Payload, Long, TravelEventResponse>> handlers = new HashMap<>();
    private final PayloadMapper payloadMapper;
    private final EventPolicy eventPolicy;
    private final TravelTrackingInfoStore travelTrackingInfoStore;
    private final TravelDomainService travelDomainService;
    private final TravelRecordRepository recordRepository;

    @PostConstruct
    public void init() {
        handlers.put(TravelEvent.START, this::start);
        handlers.put(TravelEvent.CURRENT_POSITION, this::currentPosition);
        handlers.put(TravelEvent.PAUSE, this::pause);
        handlers.put(TravelEvent.RESTART, this::reStart);
        handlers.put(TravelEvent.END, this::end);
    }

    public TravelEventResponse execute(Payload payload, Long userId) {
        TravelEvent travelEvent = TravelEvent.fromName(payload.event());
        Status status = travelTrackingInfoStore.getStatus(userId);
        validateEventForStatus(travelEvent, status);
        BiFunction<Payload, Long, TravelEventResponse> handler = findHandler(travelEvent);
        return handler.apply(payload, userId);
    }

    private BiFunction<Payload, Long, TravelEventResponse> findHandler(TravelEvent travelEvent) {
        return handlers.get(travelEvent);
    }

    private void validateEventForStatus(TravelEvent travelEvent, Status status) {
        if (!eventPolicy.isAllowed(status, travelEvent)) {
            log.error("현재 유저 상태에서 허락되지 않은 이벤트 입니다. 현재 상태 = {}", status);
            throw new NotAllowedEventForStatusException();
        }
    }

    private TravelEventResponse start(Payload payload, Long userId) {
        StartEventReq dto = payloadMapper.extractDataToDTO(payload, StartEventReq.class);
        Long courseId = dto.courseId();
        LocalDateTime startedAt = TimeMapper.toLocalTime(dto.time());
        Point userPoint = GeoUtil.toPoint(dto.coordinate());
        travelTrackingInfoStore.makeInfo(userId, courseId, startedAt, userPoint);

        Point prevPoint = travelTrackingInfoStore.getLastPoint(userId);
        Double totalTravelDistance = travelTrackingInfoStore.getTotalTravelDistance(userId);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(courseId, prevPoint, userPoint, totalTravelDistance);
        travelTrackingInfoStore.start(userId, result.travelRemainingTime());

        return TravelResponseMapper.toStartEventRes(result);
    }


    private TravelEventResponse currentPosition(Payload payload, Long userId) {
        CurrentPositionEventReq dto = payloadMapper.extractDataToDTO(payload, CurrentPositionEventReq.class);
        Long courseId = dto.courseId();
        Point prevPoint = travelTrackingInfoStore.getLastPoint(userId);
        Point userPoint = GeoUtil.toPoint(dto.coordinate());
        Double totalTravelDistance = travelTrackingInfoStore.getTotalTravelDistance(userId);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(courseId, prevPoint, userPoint, totalTravelDistance);
        travelTrackingInfoStore.currentPosition(userId, userPoint, result.travelRemainingTime(), result.totalTravelDistance());

        return TravelResponseMapper.toCurrentPositionEventRes(result);
    }

    private TravelEventResponse pause(Payload payload, Long userId) {
        PauseEventReq dto = payloadMapper.extractDataToDTO(payload, PauseEventReq.class);
        Long courseId = dto.courseId();
        Point prevPoint = travelTrackingInfoStore.getLastPoint(userId);
        Point userPoint = GeoUtil.toPoint(dto.coordinate());
        Double totalTravelDistance = travelTrackingInfoStore.getTotalTravelDistance(userId);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(courseId, prevPoint, userPoint, totalTravelDistance);
        travelTrackingInfoStore.pause(userId, userPoint, result.travelRemainingTime(), result.totalTravelDistance());

        return TravelResponseMapper.toPauseEventRes(result);
    }


    private TravelEventResponse reStart(Payload payload, Long userId) {
        RestartEventReq dto = payloadMapper.extractDataToDTO(payload, RestartEventReq.class);
        Long courseId = dto.courseId();
        Point prevPoint = travelTrackingInfoStore.getLastPoint(userId);
        Point userPoint = GeoUtil.toPoint(dto.coordinate());
        Double totalTravelDistance = travelTrackingInfoStore.getTotalTravelDistance(userId);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(courseId, prevPoint, userPoint, totalTravelDistance);
        travelTrackingInfoStore.reStart(userId, userPoint, result.travelRemainingTime(), result.totalTravelDistance());

        return TravelResponseMapper.toRestartEventRes(result);
    }

    private TravelEventResponse end(Payload payload, Long userId) {
        EndEventReq dto = payloadMapper.extractDataToDTO(payload, EndEventReq.class);
        Long courseId = dto.courseId();
        Duration totalTravelTime = Duration.ofMillis(dto.totalTravelTime());
        LocalDateTime endAt = TimeMapper.toLocalTime(dto.time());
        Point prevPoint = travelTrackingInfoStore.getLastPoint(userId);
        Point userPoint = GeoUtil.toPoint(dto.coordinate());
        Double totalTravelDistance = travelTrackingInfoStore.getTotalTravelDistance(userId);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(courseId, prevPoint, userPoint, totalTravelDistance);
        TravelTrackingInfo info = travelTrackingInfoStore.end(endAt, userId, userPoint, result.travelRemainingTime(), result.totalTravelDistance(), totalTravelTime);

        saveRecord(info);
        travelTrackingInfoStore.deleteInfo(userId);

        return TravelResponseMapper.toEndEventRes(result);
    }

    private void saveRecord(TravelTrackingInfo info) {
        User user = userService.findUserByIdOrThrow(info.getUserId());
        Course course = courseService.findByIdOrThrow(info.getCourseId());
        TravelRecord record = recordRepository.save(TravelMapper.toRecord(user, course, info));
    }
}
