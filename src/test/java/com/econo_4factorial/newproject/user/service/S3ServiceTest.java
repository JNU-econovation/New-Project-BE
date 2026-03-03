package com.econo_4factorial.newproject.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Mock
    private UserService userService;

    private final String BUCKET = "test-bucket";
    private final String DEFAULT_PROFILE_KEY = "default/profile.png";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucket", BUCKET);
        ReflectionTestUtils.setField(s3Service, "defaultProfileKey", DEFAULT_PROFILE_KEY);
    }

    @Test
    @DisplayName("Presigned URL이 정상적으로 생성되어야 한다")
    void createPresignedUrl_Success() throws MalformedURLException {
        // given
        Long userId = 1L;
        ImageFileFormat fileFormat = ImageFileFormat.JPG;
        URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/test-path");
        given(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).willReturn(mockUrl);

        // when
        PresignedUrlDTO result = s3Service.createPresignedUrl(userId, fileFormat);

        // then
        assertThat(result.presignedUrl()).isEqualTo(mockUrl.toString());
        assertThat(result.fileName()).contains("profile/" + userId + "/");
        assertThat(result.fileName()).endsWith(".jpeg");
    }

    @Test
    @DisplayName("DB에 파일명이 없을 때 기본 이미지 URL을 반환해야 한다")
    void getImageUrl_ReturnsDefault_WhenNoFileName() throws MalformedURLException {
        // given
        Long userId = 1L;
        given(userService.getUserProfileFileName(userId)).willReturn(null);
        given(amazonS3Client.getUrl(BUCKET, DEFAULT_PROFILE_KEY)).willReturn(new URL("https://s3.com/" + DEFAULT_PROFILE_KEY));

        // when
        ProfileImageUrlDTO result = s3Service.getImageUrl(userId);

        // then
        assertThat(result.profileImageUrl()).contains(DEFAULT_PROFILE_KEY);
    }

    @Test
    @DisplayName("새 이미지를 저장할 때 기존 이미지가 있다면 삭제해야 한다")
    void saveFileNameToEntity_DeletesOldImage() {
        // given
        Long userId = 1L;
        String oldFileName = "profile/1/old-uuid.jpeg";
        String newFileName = "profile/1/new-uuid.jpeg";

        given(amazonS3Client.doesObjectExist(BUCKET, newFileName)).willReturn(true);
        given(userService.getUserProfileFileName(userId)).willReturn(oldFileName);

        // when
        s3Service.saveFileNameToEntity(userId, newFileName);

        // then
        verify(amazonS3Client).deleteObject(BUCKET, oldFileName);
        verify(userService).updateUserProfileFileName(userId, newFileName);
    }

    @Test
    @DisplayName("새 이미지를 저장할 때 기존 이미지가 기본 이미지라면 삭제하지 않아야 한다")
    void saveFileNameToEntity_DoesNotDeleteDefaultImage() {
        // given
        Long userId = 1L;
        String newFileName = "profile/1/new-uuid.jpeg";

        given(amazonS3Client.doesObjectExist(BUCKET, newFileName)).willReturn(true);
        given(userService.getUserProfileFileName(userId)).willReturn(DEFAULT_PROFILE_KEY);

        // when
        s3Service.saveFileNameToEntity(userId, newFileName);

        // then
        verify(amazonS3Client, never()).deleteObject(eq(BUCKET), anyString());
        verify(userService).updateUserProfileFileName(userId, newFileName);
    }

    @Test
    @DisplayName("S3에 존재하지 않는 파일명을 저장하려 하면 예외가 발생해야 한다")
    void saveFileNameToEntity_ThrowsException_WhenFileNotExists() {
        // given
        Long userId = 1L;
        String fileName = "non-existent-file.jpeg";
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> s3Service.saveFileNameToEntity(userId, fileName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Image does not exist");
    }

    @Test
    @DisplayName("프로필 이미지 삭제 시 S3와 DB 모두에서 지워져야 한다")
    void deleteImageUrl_Success() {
        // given
        Long userId = 1L;
        String fileName = "profile/1/uuid.jpeg";
        given(userService.getUserProfileFileName(userId)).willReturn(fileName);
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(true);

        // when
        s3Service.deleteImageUrl(userId);

        // then
        verify(amazonS3Client).deleteObject(BUCKET, fileName);
        verify(userService).deleteUserProfileFileName(userId);
    }
}
