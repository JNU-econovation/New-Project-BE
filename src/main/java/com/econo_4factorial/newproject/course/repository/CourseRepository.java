package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {
    Optional<Course> findByCourseId(Long courseId);
}
