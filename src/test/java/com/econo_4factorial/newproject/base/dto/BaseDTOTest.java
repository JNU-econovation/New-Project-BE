package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.base.domain.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseDTOTest {

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 베이스를_DTO로_변환한다() {
        Base base = mock(Base.class);
        Point geoPoint = geometryFactory.createPoint(new Coordinate(126.9876, 35.1234));
        when(base.getId()).thenReturn(4L);
        when(base.getName()).thenReturn("중머리재");
        when(base.getGeoPoint()).thenReturn(geoPoint);

        BaseDTO result = BaseDTO.from(base);

        assertThat(result.baseId()).isEqualTo(4L);
        assertThat(result.name()).isEqualTo("중머리재");
        assertThat(result.coordinate()).containsExactly(
                new java.math.BigDecimal("126.9876"),
                new java.math.BigDecimal("35.1234")
        );
    }
}
