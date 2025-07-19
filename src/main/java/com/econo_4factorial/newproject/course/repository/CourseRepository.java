package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {
    List<Course> findByCourseId(Long courseId);
}
