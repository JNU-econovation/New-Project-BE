package com.econo_4factorial.newproject.user.dto;

public record PresignedUrlDTO(
        String presignedUrl,
        String filePath
) {
    public static PresignedUrlDTO of(String presignedUrl, String filePath) {
        return new PresignedUrlDTO(presignedUrl, filePath);
    }
}
