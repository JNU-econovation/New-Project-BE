package com.econo_4factorial.newproject.course.repository;


import com.econo_4factorial.newproject.course.domain.Bookmark;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Bookmark> findAllByUserIdAndCourseId(Long userId, Long courseId);

    List<Bookmark> findByUserId(Long userId);
}
