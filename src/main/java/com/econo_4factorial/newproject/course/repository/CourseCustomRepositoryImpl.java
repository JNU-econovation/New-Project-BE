package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(searchCondition.sortBy());

        return queryFactory.
                select(Projections.constructor(CourseWithBookmarkDTO.class,
                        course.id,
                        course.name,
                        course.displayName,
                        course.peakBase.id,
                        course.length,
                        course.duration,
                        course.difficulty,
                        new CaseBuilder()
                                .when(bookmark.id.isNotNull()).then(true)
                                .otherwise(false),
                        course.imageUrl,
                        course.mountain.id
                ))
                .from(course)
                .leftJoin(bookmark).on(
                        course.id.eq(bookmark.course.id)
                                .and(bookmark.user.id.eq(userId))
                )
                .where(mountainIdEq(mountainId))
                .orderBy(orderSpecifiers)
                .fetch();
    }

    private BooleanExpression mountainIdEq(Long mountainId) {
        return mountainId != null ? course.mountain.id.eq(mountainId) : null;
    }

    private OrderSpecifier[] createOrderSpecifier(String sortBy) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (DIFFICULTY.equalsIgnoreCase(sortBy))
            orderSpecifiers.add(orderByDifficultyAsc());
        else if (LENGTH.equalsIgnoreCase(sortBy))
            orderSpecifiers.add(orderByLengthAsc());
        else
            orderSpecifiers.add(orderByBookmarkAsc());

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

    private OrderSpecifier<Integer> orderByDifficultyAsc() {
        return new CaseBuilder()
                .when(course.difficulty.eq(Difficulty.EASY)).then(NUMBER_ONE)
                .when(course.difficulty.eq(Difficulty.NORMAL)).then(NUMBER_TWO)
                .otherwise(NUMBER_THREE)
                .asc();
    }

    private OrderSpecifier orderByLengthAsc() {
        return new OrderSpecifier(Order.ASC, course.length);
    }

    private OrderSpecifier<Integer> orderByBookmarkAsc() {
        return new CaseBuilder()
                .when(bookmark.id.isNotNull()).then(NUMBER_ONE)
                .otherwise(NUMBER_TWO)
                .asc();
    }

}
