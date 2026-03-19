package com.econo_4factorial.newproject.travel.repository;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.config.QueryDslConfig;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import com.econo_4factorial.newproject.support.MySqlContainerSupport;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import com.econo_4factorial.newproject.user.domain.User;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TravelRecordRepositoryTest extends MySqlContainerSupport {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TravelRecordRepository travelRecordRepository;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 사용자_아이디와_기간으로_기록을_조회한다() {
        Mountain mountain = 산을_생성한다("지리산", "JR", 35.3187, 127.7320);
        entityManager.persist(mountain);

        Base peakBase = 베이스를_생성한다("지리산 정상", mountain, 포인트를_생성한다(127.7320, 35.3187), 1915L);
        Base destinationBase = 베이스를_생성한다("지리산 도착", mountain, 포인트를_생성한다(127.7400, 35.3200), 500L);
        entityManager.persist(peakBase);
        entityManager.persist(destinationBase);

        Course course = 코스를_생성한다("지리산 A", mountain, peakBase, destinationBase);
        entityManager.persist(course);

        User user = 카카오_유저를_생성한다("user1@example.com", "user1", 10L);
        User otherUser = 카카오_유저를_생성한다("user2@example.com", "user2", 20L);
        entityManager.persist(user);
        entityManager.persist(otherUser);

        TravelRecord inRange1 = 산행기록을_생성한다(user, course, LocalDateTime.of(2024, 5, 10, 9, 0));
        TravelRecord inRange2 = 산행기록을_생성한다(user, course, LocalDateTime.of(2024, 5, 20, 9, 0));
        TravelRecord outRange = 산행기록을_생성한다(user, course, LocalDateTime.of(2024, 4, 30, 9, 0));
        TravelRecord otherUserRecord = 산행기록을_생성한다(otherUser, course, LocalDateTime.of(2024, 5, 15, 9, 0));
        entityManager.persist(inRange1);
        entityManager.persist(inRange2);
        entityManager.persist(outRange);
        entityManager.persist(otherUserRecord);
        entityManager.flush();
        entityManager.clear();

        List<TravelRecord> result = travelRecordRepository.findAllByUserIdAndStartedAtBetween(
                user.getId(),
                LocalDateTime.of(2024, 5, 1, 0, 0),
                LocalDateTime.of(2024, 5, 31, 23, 59, 59)
        );

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TravelRecord::getId)
                .containsExactlyInAnyOrder(inRange1.getId(), inRange2.getId());
    }

    @Test
    void 기록_id와_사용자_id로_조회한다() {
        Mountain mountain = 산을_생성한다("한라산", "HL", 33.3617, 126.5292);
        entityManager.persist(mountain);

        Base peakBase = 베이스를_생성한다("한라산 정상", mountain, 포인트를_생성한다(126.5292, 33.3617), 1950L);
        Base destinationBase = 베이스를_생성한다("한라산 도착", mountain, 포인트를_생성한다(126.5310, 33.3600), 600L);
        entityManager.persist(peakBase);
        entityManager.persist(destinationBase);

        Course course = 코스를_생성한다("한라산 A", mountain, peakBase, destinationBase);
        entityManager.persist(course);

        User user = 카카오_유저를_생성한다("user3@example.com", "user3", 30L);
        User otherUser = 카카오_유저를_생성한다("user4@example.com", "user4", 40L);
        entityManager.persist(user);
        entityManager.persist(otherUser);

        TravelRecord record = 산행기록을_생성한다(user, course, LocalDateTime.of(2024, 6, 1, 9, 0));
        entityManager.persist(record);
        entityManager.flush();
        entityManager.clear();

        assertThat(travelRecordRepository.findByIdAndUserId(record.getId(), user.getId()))
                .isPresent();
        assertThat(travelRecordRepository.findByIdAndUserId(record.getId(), otherUser.getId()))
                .isEmpty();
    }

    private TravelRecord 산행기록을_생성한다(User user, Course course, LocalDateTime startedAt) {
        return TravelRecord.builder()
                .user(user)
                .course(course)
                .paths(경로를_생성한다())
                .startedAt(startedAt)
                .endAt(startedAt.plusHours(2))
                .totalTravelDistanceKm(5.2)
                .totalTravelTime(Duration.ofHours(2))
                .displayName("테스트 기록")
                .build();
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

    private User 카카오_유저를_생성한다(String email, String name, Long kakaoId) {
        return User.kakaoUserBuilder()
                .email(email)
                .name(name)
                .kakaoId(kakaoId)
                .build();
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
