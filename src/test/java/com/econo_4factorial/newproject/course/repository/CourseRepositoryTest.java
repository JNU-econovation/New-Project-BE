package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.config.QueryDslConfig;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import com.econo_4factorial.newproject.support.MySqlContainerSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CourseRepositoryTest extends MySqlContainerSupport {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 도착지_근접여부를_판단한다() {
        Mountain mountain = 산을_생성한다("북한산", "BH", 37.6583, 126.9770);
        entityManager.persist(mountain);

        Base peakBase = 베이스를_생성한다("북한산 정상", mountain, 포인트를_생성한다(126.9770, 37.6583), 836L);
        Base destinationBase = 베이스를_생성한다("북한산 도착", mountain, 포인트를_생성한다(126.9800, 37.6590), 300L);
        entityManager.persist(peakBase);
        entityManager.persist(destinationBase);

        Course course = 코스를_생성한다("북한산 A", mountain, peakBase, destinationBase);
        entityManager.persist(course);
        entityManager.flush();
        entityManager.clear();

        long arrived = courseRepository.isUserArrivedDestination(
                course.getId(),
                포인트를_생성한다(126.9800, 37.6590)
        );
        long notArrived = courseRepository.isUserArrivedDestination(
                course.getId(),
                포인트를_생성한다(126.9000, 37.7000)
        );

        assertThat(arrived).isEqualTo(1L);
        assertThat(notArrived).isEqualTo(0L);
    }

    private Course 코스를_생성한다(String name, Mountain mountain, Base peakBase, Base destinationBase) {
        Course course = 새_인스턴스(Course.class);
        ReflectionTestUtils.setField(course, "mountain", mountain);
        ReflectionTestUtils.setField(course, "peakBase", peakBase);
        ReflectionTestUtils.setField(course, "destinationBase", destinationBase);
        ReflectionTestUtils.setField(course, "name", name);
        ReflectionTestUtils.setField(course, "length", 4.2);
        ReflectionTestUtils.setField(course, "duration", 120L);
        ReflectionTestUtils.setField(course, "difficulty", Difficulty.NORMAL);
        ReflectionTestUtils.setField(course, "imageUrl", "/course.png");
        ReflectionTestUtils.setField(course, "displayName", name + " 코스");
        ReflectionTestUtils.setField(course, "coordinates", 경로를_생성한다());
        return course;
    }

    private Base 베이스를_생성한다(String name, Mountain mountain, Point point, Long altitude) {
        Base base = 새_인스턴스(Base.class);
        ReflectionTestUtils.setField(base, "mountain", mountain);
        ReflectionTestUtils.setField(base, "name", name);
        ReflectionTestUtils.setField(base, "GeoPoint", point);
        ReflectionTestUtils.setField(base, "altitude", altitude);
        return base;
    }

    private Mountain 산을_생성한다(String name, String initials, double latitude, double longitude) {
        Mountain mountain = 새_인스턴스(Mountain.class);
        ReflectionTestUtils.setField(mountain, "name", name);
        ReflectionTestUtils.setField(mountain, "initials", initials);
        ReflectionTestUtils.setField(mountain, "latitude", BigDecimal.valueOf(latitude));
        ReflectionTestUtils.setField(mountain, "longitude", BigDecimal.valueOf(longitude));
        ReflectionTestUtils.setField(mountain, "location", "KOREA");
        return mountain;
    }

    private LineString 경로를_생성한다() {
        LineString lineString = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1)
        });
        lineString.setSRID(4326);
        return lineString;
    }

    private Point 포인트를_생성한다(double longitude, double latitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        return point;
    }

    private <T> T 새_인스턴스(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("인스턴스 생성 실패: " + type.getSimpleName(), e);
        }
    }
}
