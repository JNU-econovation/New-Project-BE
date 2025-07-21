package com.econo_4factorial.newproject.pathway.mapper;

import com.econo_4factorial.newproject.pathway.domain.Pathway;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.exception.internalServerException.CoordinatesParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PathwayMapper {
    private final ObjectMapper objectMapper;

    public PathwayDTO toDTO (Pathway pathway) {
        return PathwayDTO.builder()
                .pathwayId(pathway.getId())
                .deptBaseId(pathway.getDeparture().getId())
                .destBaseId(pathway.getDestination().getId())
                .difficulty(pathway.getDifficulty())
                .coordinates(convertCoordinatesFromString(pathway.getCoordinates()))
                .build();
    }

    private List<List<BigDecimal>> convertCoordinatesFromString(String coordinates) {
        try {
            return objectMapper.readValue(coordinates, new TypeReference<List<List<BigDecimal>>>() {});
        } catch (JsonProcessingException e) {
            throw new CoordinatesParsingException();
        }
    }
}
