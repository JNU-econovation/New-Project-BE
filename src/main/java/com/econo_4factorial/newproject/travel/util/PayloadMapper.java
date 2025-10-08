package com.econo_4factorial.newproject.travel.util;

import com.econo_4factorial.newproject.travel.dto.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
@RequiredArgsConstructor
public class PayloadMapper {
    private final ObjectMapper mapper;

    public Payload fromMessage(TextMessage message) throws JsonProcessingException {
        return mapper.readValue(message.getPayload(), Payload.class);
    }

    public <T> T extractDataToDTO(Payload payload, Class<T> RequestClass) {
        return mapper.convertValue(payload.data(), RequestClass);
    }
}
