package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.base.domain.QBase;
import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.econo_4factorial.newproject.pathway.domain.QCoursePathwaySequence.coursePathwaySequence;
import static com.econo_4factorial.newproject.pathway.domain.QPathway.pathway;

@Repository
@AllArgsConstructor
public class CoursePathwaySequenceCustomRepositoryImpl implements CoursePathwaySequenceCustomRepository{
    private final JPAQueryFactory queryFactory;
    private final QBase departureBase = new QBase("departureBase");
    private final QBase destinationBase = new QBase("destinationBase");

    @Override
    public List<CoursePathwaySequence> findByCourseIdOrderBySequence(Long courseId) {
        return queryFactory
                .selectFrom(coursePathwaySequence)
                .join(coursePathwaySequence.pathway, pathway).fetchJoin()
                .join(coursePathwaySequence.pathway.departure,departureBase).fetchJoin()
                .join(coursePathwaySequence.pathway.destination,destinationBase).fetchJoin()
                .where(courseIdEq(courseId))
                .orderBy(orderBySequenceAsc())
                .fetch();
    }

    private BooleanExpression courseIdEq(Long courseId) {
        return courseId != null ? coursePathwaySequence.course.id.eq(courseId) : null;
    }

    private OrderSpecifier<Long> orderBySequenceAsc() {
        return coursePathwaySequence.sequence.asc();
    }
}
