package com.econo_4factorial.newproject.course.repository;


import com.econo_4factorial.newproject.course.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Bookmark> findAllByUserIdAndCourseId(Long userId, Long courseId);

    List<Bookmark> findByUserId(Long userId);
}
