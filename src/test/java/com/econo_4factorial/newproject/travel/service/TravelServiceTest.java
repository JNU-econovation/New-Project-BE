package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.dto.Payload;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import com.econo_4factorial.newproject.travel.dto.req.CurrentPositionEventReq;
import com.econo_4factorial.newproject.travel.dto.req.EndEventReq;
import com.econo_4factorial.newproject.travel.dto.req.PauseEventReq;
import com.econo_4factorial.newproject.travel.dto.req.RestartEventReq;
import com.econo_4factorial.newproject.travel.dto.req.StartEventReq;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import com.econo_4factorial.newproject.travel.exception.NotAllowedEventForStatusException;
import com.econo_4factorial.newproject.travel.util.EventPolicy;
import com.econo_4factorial.newproject.travel.util.PayloadMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @Mock
    private PayloadMapper payloadMapper;

    @Mock
    private EventPolicy eventPolicy;

    @Mock
    private TravelTrackingInfoStore travelTrackingInfoStore;

    @Mock
    private TravelDomainService travelDomainService;

    @Mock
    private TravelRecordService travelRecordService;

    private TravelService travelService;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        travelService = new TravelService(payloadMapper, eventPolicy, travelTrackingInfoStore, travelDomainService, travelRecordService);
        travelService.init();
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 시작_이벤트를_처리한다() {
        Payload payload = new Payload("start", Map.of());
        StartEventReq request = new StartEventReq(new double[]{126.0, 37.0}, 10L, 0L);
        Point point = 포인트를_생성한다(126.0, 37.0);
        TravelAnalysisResult result = 분석결과를_생성한다();

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.UNSTARTED);
        given(eventPolicy.isAllowed(Status.UNSTARTED, com.econo_4factorial.newproject.travel.TravelEvent.START)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, StartEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(point);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(0.0);
        given(travelDomainService.analyzeTravelStatus(10L, point, point, 0.0)).willReturn(result);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.START);
        verify(travelTrackingInfoStore).makeInfo(eq(1L), eq(10L), eq(LocalDateTime.of(1970, 1, 1, 9, 0)), any(Point.class));
        verify(travelTrackingInfoStore).start(1L, result.travelRemainingTime());
    }

    @Test
    void 현재위치_이벤트는_정상상태에서_store를_업데이트한다() {
        Payload payload = new Payload("current-position", Map.of());
        CurrentPositionEventReq request = new CurrentPositionEventReq(new double[]{126.1, 37.1}, 10L);
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.1, 37.1);
        TravelTrackingInfo info = 산행정보를_생성한다(Status.TRAVEL, prevPoint);
        TravelAnalysisResult result = 분석결과를_생성한다();

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.TRAVEL);
        given(eventPolicy.isAllowed(Status.TRAVEL, com.econo_4factorial.newproject.travel.TravelEvent.CURRENT_POSITION)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, CurrentPositionEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getInfo(1L)).willReturn(info);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(prevPoint);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(1.2);
        given(travelDomainService.analyzeTravelStatus(10L, prevPoint, currentPoint, 1.2)).willReturn(result);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.CURRENT_POSITION);
        verify(travelTrackingInfoStore).currentPosition(1L, currentPoint, result.travelRemainingTime(), result.totalTravelDistance());
    }

    @Test
    void 현재위치_이벤트는_info가_PAUSED면_store를_업데이트하지_않는다() {
        Payload payload = new Payload("current-position", Map.of());
        CurrentPositionEventReq request = new CurrentPositionEventReq(new double[]{126.1, 37.1}, 10L);
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.1, 37.1);
        TravelTrackingInfo info = 산행정보를_생성한다(Status.PAUSED, prevPoint);
        TravelAnalysisResult result = 분석결과를_생성한다();

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.TRAVEL);
        given(eventPolicy.isAllowed(Status.TRAVEL, com.econo_4factorial.newproject.travel.TravelEvent.CURRENT_POSITION)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, CurrentPositionEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getInfo(1L)).willReturn(info);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(prevPoint);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(1.2);
        given(travelDomainService.analyzeTravelStatus(10L, prevPoint, currentPoint, 1.2)).willReturn(result);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.CURRENT_POSITION);
        verify(travelTrackingInfoStore, never()).currentPosition(eq(1L), any(Point.class), any(RemainingTime.class), any(Double.class));
    }

    @Test
    void 일시정지_이벤트를_처리한다() {
        Payload payload = new Payload("pause", Map.of());
        PauseEventReq request = new PauseEventReq(new double[]{126.2, 37.2}, 11L, null);
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.2, 37.2);
        TravelAnalysisResult result = 분석결과를_생성한다();

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.TRAVEL);
        given(eventPolicy.isAllowed(Status.TRAVEL, com.econo_4factorial.newproject.travel.TravelEvent.PAUSE)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, PauseEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(prevPoint);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(2.0);
        given(travelDomainService.analyzeTravelStatus(11L, prevPoint, currentPoint, 2.0)).willReturn(result);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.PAUSE);
        verify(travelTrackingInfoStore).pause(1L, currentPoint, result.travelRemainingTime(), result.totalTravelDistance());
    }

    @Test
    void keep_alive_이벤트를_처리한다() {
        Payload payload = new Payload("keep-alive", Map.of());

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.PAUSED);
        given(eventPolicy.isAllowed(Status.PAUSED, com.econo_4factorial.newproject.travel.TravelEvent.KEEP_ALIVE)).willReturn(true);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.KEEP_ALIVE);
        assertThat(response.getData()).isNull();
    }

    @Test
    void 재시작_이벤트를_처리한다() {
        Payload payload = new Payload("restart", Map.of());
        RestartEventReq request = new RestartEventReq(new double[]{126.3, 37.3}, 12L, null);
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.3, 37.3);
        TravelAnalysisResult result = 분석결과를_생성한다();

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.PAUSED);
        given(eventPolicy.isAllowed(Status.PAUSED, com.econo_4factorial.newproject.travel.TravelEvent.RESTART)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, RestartEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(prevPoint);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(2.5);
        given(travelDomainService.analyzeTravelStatus(12L, prevPoint, currentPoint, 2.5)).willReturn(result);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.RESTART);
        verify(travelTrackingInfoStore).reStart(1L, currentPoint, result.travelRemainingTime(), result.totalTravelDistance());
    }

    @Test
    void 종료_이벤트를_처리하면_기록을_저장하고_info를_삭제한다() {
        Payload payload = new Payload("end", Map.of());
        EndEventReq request = new EndEventReq(new double[]{126.4, 37.4}, 13L, 0L, Duration.ofHours(2).toMillis());
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.4, 37.4);
        TravelAnalysisResult result = 분석결과를_생성한다();
        TravelTrackingInfo endedInfo = 산행정보를_생성한다(Status.END, prevPoint);

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.TRAVEL);
        given(eventPolicy.isAllowed(Status.TRAVEL, com.econo_4factorial.newproject.travel.TravelEvent.END)).willReturn(true);
        given(payloadMapper.extractDataToDTO(payload, EndEventReq.class)).willReturn(request);
        given(travelTrackingInfoStore.getLastPoint(1L)).willReturn(prevPoint);
        given(travelTrackingInfoStore.getTotalTravelDistance(1L)).willReturn(3.0);
        given(travelDomainService.analyzeTravelStatus(13L, prevPoint, currentPoint, 3.0)).willReturn(result);
        given(travelTrackingInfoStore.end(eq(LocalDateTime.of(1970, 1, 1, 9, 0)), eq(1L), eq(currentPoint),
                eq(result.travelRemainingTime()), eq(result.totalTravelDistance()), eq(Duration.ofHours(2)))).willReturn(endedInfo);

        TravelEventResponse response = travelService.execute(payload, 1L);

        assertThat(response.getEvent()).isEqualTo(com.econo_4factorial.newproject.travel.TravelEvent.END);
        verify(travelRecordService).saveTravelRecord(endedInfo);
        verify(travelTrackingInfoStore).deleteInfo(1L);
    }

    @Test
    void 현재_상태에서_허용되지_않는_이벤트면_예외가_발생한다() {
        Payload payload = new Payload("end", Map.of());

        given(travelTrackingInfoStore.getStatus(1L)).willReturn(Status.UNSTARTED);
        given(eventPolicy.isAllowed(Status.UNSTARTED, com.econo_4factorial.newproject.travel.TravelEvent.END)).willReturn(false);

        assertThatThrownBy(() -> travelService.execute(payload, 1L))
                .isInstanceOf(NotAllowedEventForStatusException.class);
    }

    @Test
    void 남아있는_산행정보가_있으면_삭제한다() {
        given(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).willReturn(true);

        travelService.deleteIfExistTravelTrackingInfo(1L);

        verify(travelTrackingInfoStore).deleteInfo(1L);
    }

    @Test
    void 남아있는_산행정보가_없으면_삭제하지_않는다() {
        given(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).willReturn(false);

        travelService.deleteIfExistTravelTrackingInfo(1L);

        verify(travelTrackingInfoStore, never()).deleteInfo(1L);
    }

    private TravelAnalysisResult 분석결과를_생성한다() {
        return new TravelAnalysisResult(
                3,
                false,
                false,
                4.2,
                new RemainingTime(Duration.ofMinutes(12), Duration.ofMinutes(24))
        );
    }

    private TravelTrackingInfo 산행정보를_생성한다(Status status, Point initialPoint) {
        TravelTrackingInfo info = TravelTrackingInfo.builder()
                .userId(1L)
                .courseId(1L)
                .startedAt(LocalDateTime.of(2024, 1, 1, 9, 0))
                .paths(new ArrayList<>(java.util.List.of(initialPoint)))
                .build();
        if (status == Status.PAUSED) {
            info.pause(initialPoint, 0.0, new RemainingTime(Duration.ZERO, Duration.ZERO));
        } else if (status == Status.TRAVEL) {
            info.currentPosition(initialPoint, 0.0, new RemainingTime(Duration.ZERO, Duration.ZERO));
        } else if (status == Status.RESTARTED) {
            info.reStart(initialPoint, 0.0, new RemainingTime(Duration.ZERO, Duration.ZERO));
        } else if (status == Status.END) {
            info.end(LocalDateTime.of(2024, 1, 1, 10, 0), initialPoint, 0.0,
                    new RemainingTime(Duration.ZERO, Duration.ZERO), Duration.ofHours(1));
        }
        return info;
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
