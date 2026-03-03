package com.econo_4factorial.newproject.user.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-profile-key}")
    private String defaultProfileKey;

    private final AmazonS3Client amazonS3Client;
    private final UserService userService;

    public PresignedUrlDTO createPresignedUrl(Long userId, ImageFileFormat fileFormat) {
        String uploadFormat = fileFormat.getUploadExtension();
        log.info("upload format: {}", uploadFormat);
        String fileName = createFileName(userId, uploadFormat);
        log.info("file name: {}", fileName);
        log.info("file format: {}", fileFormat);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName, uploadFormat);
        URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return new PresignedUrlDTO(presignedUrl.toString(), fileName);
    }

    public ProfileImageUrlDTO getImageUrl(Long userId) {
        String fileName = userService.getUserProfileFileName(userId);

        if (fileName == null || fileName.isBlank()) {
            fileName = defaultProfileKey;
        }

        if (!amazonS3Client.doesObjectExist(bucket, fileName)) {
            fileName = defaultProfileKey;
        }

        String profileImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        return new ProfileImageUrlDTO(profileImageUrl);
    }

    @Transactional
    public void deleteImageUrl(Long userId) {
        String fileName = userService.getUserProfileFileName(userId);
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalStateException("삭제할 프로필 이미지가 없습니다");
        }
        isExistImageInBucket(fileName);
        amazonS3Client.deleteObject(bucket, fileName);
        userService.deleteUserProfileFileName(userId);
    }

    @Transactional
    public void saveFileNameToEntity(Long userId, String fileName) {
        isExistImageInBucket(fileName);
        String oldFileName = userService.getUserProfileFileName(userId);

        if (oldFileName != null && !oldFileName.isBlank() && !oldFileName.equals(defaultProfileKey) && !oldFileName.equals(fileName)) {
            amazonS3Client.deleteObject(bucket, oldFileName);
        }

        userService.updateUserProfileFileName(userId, fileName);
    }

    private void isExistImageInBucket(String fileName) {
        if (!amazonS3Client.doesObjectExist(bucket, fileName)) {
            throw new IllegalStateException("Image does not exist: " + fileName);
        }
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
