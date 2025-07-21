package com.econo_4factorial.newproject.pathway.dto;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PathwayDTO (
        Long pathwayId,
        Long deptBaseId,
        Long destBaseId,
        Difficulty difficulty,
        List<List<BigDecimal>> coordinates
){
}
