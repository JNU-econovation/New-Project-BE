package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.domain.Course;
import feign.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {

    @Query (value = """
        SELECT ST_Distance_Sphere(:userPoint, b.geo_point) <= 10
        FROM course c
        JOIN base b ON c.destination_base_id = b.id
        WHERE c.id = :courseId
        """,nativeQuery = true)
    long isUserArrivedDestination(
            @Param("courseId") Long courseId,
            @Param("userPoint") Point userPoint
    );

    /* mysql ST_Distance_Sphere 함수에서 Point와 Linestring간 연산 미지원
    @Query (value = """
        SELECT ST_Distance_Sphere(:userPoint, c.coordinates ) >= 50
        FROM course c
        WHERE c.id = :courseId
        """,nativeQuery = true)
    long isUserDeviated(
            @Param("courseId") Long courseId,
            @Param("userPoint") Point userPoint
    );
     */

    /* ST_LineSubString 미지원
    @Query (value = """
        SELECT ST_Length(
            ST_LineSubString(
                c.coordinates,
                ST_LineLocatePoint(c.coordinates, :userPoint),
                ST_LineLocatePoint(c.coordinates, b.geo_point)
            ),'metre'
        )
        FROM course c
        JOIN base b ON c.peak_base_id = b.id
        WHERE c.id = :courseId
        """, nativeQuery = true)
    Double getRemainingDistanceToPeak(
            @Param("courseId") Long courseId,
            @Param("userPoint") Point userPoint
    );

    @Query (value = """
        SELECT ST_Length(
            ST_LineSubString(
                c.coordinates,
                ST_LineLocatePoint(c.coordinates, :userPoint),
                ST_LineLocatePoint(c.coordinates, b.geo_point)
            ),'metre'
        )
        FROM course c
        JOIN base b ON c.destination_base_id = b.id
        WHERE c.id = :courseId
        """, nativeQuery = true)
    Double getRemainingDistanceToDestination(
            @Param("courseId") Long courseId,
            @Param("userPoint") Point userPoint
    );
     */
}
