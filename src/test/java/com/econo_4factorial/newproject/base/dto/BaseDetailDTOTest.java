package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.domain.BaseImage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseDetailDTOTest {

    @Test
    void 베이스_상세응답을_생성한다() {
        Base base = mock(Base.class);
        BaseImage firstImage = mock(BaseImage.class);
        BaseImage secondImage = mock(BaseImage.class);
        when(base.getId()).thenReturn(5L);
        when(base.getName()).thenReturn("장불재");
        when(base.getWeather()).thenReturn("Clouds");
        when(base.getTemperature()).thenReturn(18.0);
        when(firstImage.getImageUrl()).thenReturn("/bases/1.png");
        when(secondImage.getImageUrl()).thenReturn("/bases/2.png");

        BaseDetailDTO result = BaseDetailDTO.from(base, List.of(firstImage, secondImage));

        assertThat(result.baseId()).isEqualTo(5L);
        assertThat(result.name()).isEqualTo("장불재");
        assertThat(result.weather()).isEqualTo("Clouds");
        assertThat(result.temperature()).isEqualTo(18.0);
        assertThat(result.recommendedOutfit()).isNull();
        assertThat(result.images()).containsExactly("/bases/1.png", "/bases/2.png");
    }
}
