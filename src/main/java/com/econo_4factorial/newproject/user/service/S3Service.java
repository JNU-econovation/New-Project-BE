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

    private final AmazonS3Client amazonS3Client;
    private final UserService userService;

    @Transactional
    public PresignedUrlDTO createPresignedUrl(Long userId, ImageFileFormat fileFormat) {
        String uploadFormat = fileFormat.getUploadExtension();
        String fileName = createFileName(userId, String.valueOf(fileFormat));
        log.info(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName, uploadFormat);
        URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return PresignedUrlDTO.of(presignedUrl.toString(), fileName);
    }

    public ProfileImageUrlDTO getFileUrl(Long userId) {
        String fileName = userService.getUserProfileImageName(userId);
        String profileImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        return new ProfileImageUrlDTO(profileImageUrl);
    }

    public void deleteFileUrl(Long userId) {
        String userProfileImageName = userService.getUserProfileImageName(userId);
        amazonS3Client.deleteObject(bucket, userProfileImageName);
        userService.deleteUserProfileImageName(userId);
    }

    public void saveFileNameToEntity(Long userId, String fileName) {
        isExistImageInBucket(fileName);
        userService.updateUserProfileImageName(userId, fileName);
    }

    private void isExistImageInBucket(String imageName) {
        if (!amazonS3Client.doesObjectExist(bucket, imageName)) {
            throw new IllegalStateException("Image does not exist: " + imageName);
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
