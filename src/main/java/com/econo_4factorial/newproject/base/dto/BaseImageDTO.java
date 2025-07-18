package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.base.domain.BaseImage;

import java.util.List;

public record BaseImageDTO(
        List<String> baseImages
) {
    public static List<String> toGetBaseImages(List<BaseImage> baseImages) {
        return baseImages.stream()
                .map(BaseImage::getImageUrl)
                .toList();
    }
}
