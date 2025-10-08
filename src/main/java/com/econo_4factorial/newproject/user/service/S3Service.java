package com.econo_4factorial.newproject.user.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public PresignedUrlDTO execute(Long userId, ImageFileFormat fileFormat) {
        String valueFileExtension = fileFormat.getUploadExtension();
        String fileName = createFileName(userId, String.valueOf(fileFormat));
        log.info(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, fileName, valueFileExtension);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return PresignedUrlDTO.of(url.toString(), fileName);
    }

    private String createFileName(Long userId, String fileExtension) {
        return "profile/" + userId + "/" + UUID.randomUUID() + "." + fileExtension;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
            String bucket, String fileName, String fileExtension
    ) {
        return new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withKey(fileName)
                .withContentType("image/" + fileExtension)
                .withExpiration(getPreSignedUrlExpiration());
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 60 * 1000;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

}
