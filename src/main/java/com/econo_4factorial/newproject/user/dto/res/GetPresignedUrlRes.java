package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;

public record GetPresignedUrlRes(
        PresignedUrlDTO presignedUrlDTO
) {
    public static GetPresignedUrlRes from(PresignedUrlDTO presignedUrlDTO) {
        return new GetPresignedUrlRes(presignedUrlDTO);
    }
}
