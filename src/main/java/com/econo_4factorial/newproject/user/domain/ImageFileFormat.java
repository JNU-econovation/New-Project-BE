package com.econo_4factorial.newproject.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFileFormat
{
    JPG("jpeg"),
    PNG("png"),
    HEIC("heic");

    private final String uploadExtension;
}
