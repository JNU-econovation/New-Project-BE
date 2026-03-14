package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;

public record GetPresignedUrlRes(
        String presignedUrl,
        String fileName
) {
    public static GetPresignedUrlRes from(PresignedUrlDTO presignedUrlDTO) {
        return new GetPresignedUrlRes(presignedUrlDTO.presignedUrl(), presignedUrlDTO.fileName());
    }
}
