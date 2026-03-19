package com.econo_4factorial.newproject.travel.dto;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.common.util.TimeMapper;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;

public record TravelRecordDTO (
        Long id,
        Long date,
        String displayName,
        String image,
        Double length,
        Long duration,
        Difficulty difficulty
){
    public static TravelRecordDTO from(TravelRecord travelRecord){
        Long date = TimeMapper.toEpochMilli(travelRecord.getStartedAt());
        Course course =travelRecord.getCourse();

        return new TravelRecordDTO(
                travelRecord.getId(),
                date,
                course.getDisplayName(),
                course.getImageUrl(),
                course.getLength(),
                course.getDuration(),
                course.getDifficulty()
        );
    }
}
