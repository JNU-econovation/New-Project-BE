package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository{
}
