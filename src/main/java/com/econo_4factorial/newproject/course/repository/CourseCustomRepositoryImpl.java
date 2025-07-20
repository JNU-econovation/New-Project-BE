package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.domain.Difficulty;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.econo_4factorial.newproject.course.domain.QBookmark.bookmark;
import static com.econo_4factorial.newproject.course.domain.QCourse.course;

@Repository
@AllArgsConstructor
public class CourseCustomRepositoryImpl implements CourseCustomRepository {
    private final String DIFFICULTY = "difficulty";
    private final String LENGTH = "length";
    private final Integer NUMBER_ONE = 1;
    private final Integer NUMBER_TWO = 2;
    private final Integer NUMBER_THREE = 3;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CourseWithBookmarkDTO> findAllByMountainIdWithBookmark(CourseSearchCondition searchCondition, Long mountainId, Long userId) {
        return queryFactory.
                select(Projections.constructor(CourseWithBookmarkDTO.class,
                        course.id,
                        course.name,
                        course.length,
                        course.duration,
                        course.difficulty,
                        new CaseBuilder()
                                .when(bookmark.id.isNotNull()).then(true)
                                .otherwise(false)
                ))
                .from(course)
                .leftJoin(bookmark).on(
                        course.id.eq(bookmark.course.id)
                                .and(bookmark.user.id.eq(userId))
                )
                .where(mountainIdEq(searchCondition.mountainId()))
                .orderBy(getSortOrder(searchCondition.sortBy()))
                .fetch();
    }

    private BooleanExpression mountainIdEq(Long mountainId) {
        return mountainId != null ? course.mountain.id.eq(mountainId) : null;
    }

    private OrderSpecifier<?> getSortOrder(String sortBy) {
        if (sortBy == null)
            return course.id.asc();
        if (DIFFICULTY.equalsIgnoreCase(sortBy))
            return orderByDifficultyAsc();
        if (LENGTH.equalsIgnoreCase(sortBy))
            return course.length.asc();
        return course.id.asc();
    }

    private OrderSpecifier<Integer> orderByDifficultyAsc() {
        return new CaseBuilder()
                .when(course.difficulty.eq(Difficulty.EASY)).then(NUMBER_ONE)
                .when(course.difficulty.eq(Difficulty.NORMAL)).then(NUMBER_TWO)
                .otherwise(NUMBER_THREE)
                .asc();

    }

}
