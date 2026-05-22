package com.econo_4factorial.newproject.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;

class GeometryConfigTest {

    @Test
    void SRID_4326_지오메트리팩토리를_생성한다() {
        GeometryFactory geometryFactory = new GeometryConfig().geometryFactory();

        assertThat(geometryFactory.getSRID()).isEqualTo(4326);
        assertThat(geometryFactory.getPrecisionModel()).isNotNull();
    }
}
