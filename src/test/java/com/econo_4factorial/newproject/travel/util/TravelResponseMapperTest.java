package com.econo_4factorial.newproject.travel.util;

import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import com.econo_4factorial.newproject.travel.dto.res.CurrentPositionEventRes;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TravelResponseMapperTest {

    @Test
    void 시작_응답을_생성한다() {
        TravelAnalysisResult result = 분석결과를_생성한다();

        TravelEventResponse response = TravelResponseMapper.toStartEventRes(result);

        응답을_검증한다(response, TravelEvent.START);
    }

    @Test
    void 현재위치_응답을_생성한다() {
        TravelAnalysisResult result = 분석결과를_생성한다();

        CurrentPositionEventRes response = TravelResponseMapper.toCurrentPositionEventRes(result);

        응답을_검증한다(response, TravelEvent.CURRENT_POSITION);
    }

    @Test
    void 일시정지_재시작_종료_응답을_생성한다() {
        TravelAnalysisResult result = 분석결과를_생성한다();

        응답을_검증한다(TravelResponseMapper.toPauseEventRes(result), TravelEvent.PAUSE);
        응답을_검증한다(TravelResponseMapper.toRestartEventRes(result), TravelEvent.RESTART);
        응답을_검증한다(TravelResponseMapper.toEndEventRes(result), TravelEvent.END);
    }

    @Test
    void keep_alive_응답은_data가_null이다() {
        TravelEventResponse response = TravelResponseMapper.toKeepAliveEventRes();

        assertThat(response.getEvent()).isEqualTo(TravelEvent.KEEP_ALIVE);
        assertThat(response.getData()).isNull();
    }

    private TravelAnalysisResult 분석결과를_생성한다() {
        return new TravelAnalysisResult(
                7,
                true,
                false,
                3.25,
                new RemainingTime(Duration.ofMinutes(15), Duration.ofMinutes(30))
        );
    }

    private void 응답을_검증한다(TravelEventResponse response, TravelEvent event) {
        assertThat(response.getEvent()).isEqualTo(event);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getIndex()).isEqualTo(7);
        assertThat(response.getData().getIsArrived()).isTrue();
        assertThat(response.getData().getIsDeviation()).isFalse();
        assertThat(response.getData().getTravelDistance()).isEqualTo(3.25);
        assertThat(response.getData().getRemainTimeToStopover()).isEqualTo(Duration.ofMinutes(15).toMillis());
        assertThat(response.getData().getRemainTimeToEnd()).isEqualTo(Duration.ofMinutes(30).toMillis());
    }
}
