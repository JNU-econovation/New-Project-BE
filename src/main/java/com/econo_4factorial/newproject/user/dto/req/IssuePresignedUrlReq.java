package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.user.domain.ImageFileFormat;

public record IssuePresignedUrlReq(
        ImageFileFormat imageFileFormat
) {
}
