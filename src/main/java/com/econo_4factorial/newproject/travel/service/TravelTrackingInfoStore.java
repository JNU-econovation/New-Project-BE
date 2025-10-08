package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.exception.TravelTrackingInfoNotFoundException;
import com.econo_4factorial.newproject.travel.mapper.TravelMapper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class TravelTrackingInfoStore {
    private final Map<Long, TravelTrackingInfo> infoStore = new HashMap<>();

    public Status getStatus(Long userId) {
        return Optional.ofNullable(infoStore.get(userId))
                .map(TravelTrackingInfo::getStatus)
                .orElse(Status.UNSTARTED);
    }


    public void makeInfo(Long userId, Long courseId, LocalDateTime startedAt, Point userPoint) {
        TravelTrackingInfo info = TravelMapper.toDefaultInfo(userId, courseId, startedAt, userPoint);
        infoStore.put(userId, info);
        log.info("산행 정보 객체 생성. userId : {}", userId);
    }

    public void start(Long userId, RemainingTime remainingTime) {
        TravelTrackingInfo info = infoStore.get(userId);
        info.start(remainingTime);
        log.info("산행 start. userId : {}", userId);
    }

    public void currentPosition(Long userId, Point userPoint, RemainingTime remainingTime, Double totalTravelDistance) {
        TravelTrackingInfo info = infoStore.get(userId);
        info.currentPosition(userPoint, totalTravelDistance, remainingTime);
        log.info("산행 정보 업데이트. userId : {}", userId);
    }

    public void pause(Long userId, Point userPoint, RemainingTime remainingTime, Double totalTravelDistance) {
        TravelTrackingInfo info = infoStore.get(userId);
        info.pause(userPoint, totalTravelDistance, remainingTime);
        log.info("산행 중지. userId : {}", userId);
    }

    public void reStart(Long userId, Point userPoint, RemainingTime remainingTime, Double totalTravelDistance) {
        TravelTrackingInfo info = infoStore.get(userId);
        info.reStart(userPoint, totalTravelDistance, remainingTime);
        log.info("산행 재개. userId : {}", userId);
    }

    public TravelTrackingInfo end(LocalDateTime endAt, Long userId, Point userPoint, RemainingTime remainingTime, Double totalTravelDistance, Duration totalTravelTime) {
        TravelTrackingInfo info = infoStore.get(userId);
        info.end(endAt, userPoint, totalTravelDistance, remainingTime, totalTravelTime);
        log.info("산행 종료. userId : {}", userId);
        return info;
    }

    public Point getLastPoint(Long userId) {
        return  Optional.ofNullable(infoStore.get(userId))
                .map(TravelTrackingInfo::getLastPoint)
                .orElseThrow(TravelTrackingInfoNotFoundException::new);
    }

    public Double getTotalTravelDistance(Long userId) {
        return Optional.ofNullable(infoStore.get(userId))
                .map(TravelTrackingInfo::getTotalTravelDistanceKm)
                .orElseThrow(TravelTrackingInfoNotFoundException::new);
    }

    public void deleteInfo(Long userId) {
        infoStore.remove(userId);
        log.info("산행 info 삭제. userId : {}", userId);
    }
}
