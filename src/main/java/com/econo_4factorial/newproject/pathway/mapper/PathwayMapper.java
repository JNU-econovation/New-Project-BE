package com.econo_4factorial.newproject.pathway.mapper;

import com.econo_4factorial.newproject.pathway.domain.Pathway;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PathwayMapper {
    public PathwayDTO toDTO(Pathway pathway) {
        return PathwayDTO.builder()
                .pathwayId(pathway.getId())
                .deptBaseId(pathway.getDeparture().getId())
                .destBaseId(pathway.getDestination().getId())
                .difficulty(pathway.getDifficulty())
                .coordinates(pathway.getCoordinates())
                .build();
    }
}
