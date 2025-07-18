package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.domain.BaseImage;

import java.util.List;

public record BaseDetailDTO(
        Long baseId,
        String name,
        String weather,
        Double temperature,
        String recommendedOutfit,
        List<String> images
) {
    public static BaseDetailDTO from(Base base, List<BaseImage> baseImages) {

        return new BaseDetailDTO(
                base.getId(),
                base.getName(),
                base.getWeather(),
                base.getTemperature(),
                null, // recommendedOutfit은 DB에 없으므로 null 처리
                baseImages.stream()
                        .map(BaseImage::getImageUrl)
                        .toList()
        );
    }
}
