package com.econo_4factorial.newproject.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    void Presigned_URL을_정상적으로_생성한다() throws MalformedURLException {
        Long userId = 1L;
        ImageFileFormat fileFormat = ImageFileFormat.JPG;
        URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/test-path");
        given(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).willReturn(mockUrl);

        PresignedUrlDTO result = s3Service.createPresignedUrl(userId, fileFormat);

        assertThat(result.presignedUrl()).isEqualTo(mockUrl.toString());
        assertThat(result.fileName()).contains("profile/" + userId + "/");
        assertThat(result.fileName()).endsWith(".jpeg");
    }

    @Test
    void DB에_파일명이_없으면_기본_이미지_URL을_반환한다() throws MalformedURLException {
        Long userId = 1L;
        given(userService.getUserProfileFileName(userId)).willReturn(null);
        given(amazonS3Client.getUrl(BUCKET, DEFAULT_PROFILE_KEY)).willReturn(
                new URL("https://s3.com/" + DEFAULT_PROFILE_KEY));

        ProfileImageUrlDTO result = s3Service.getImageUrl(userId);

        assertThat(result.profileImageUrl()).contains(DEFAULT_PROFILE_KEY);
    }

    @Test
    void DB에_파일명이_있으면_해당_이미지_URL을_반환한다() throws MalformedURLException {
        Long userId = 1L;
        String fileName = "profile/1/custom.jpeg";
        given(userService.getUserProfileFileName(userId)).willReturn(fileName);
        given(amazonS3Client.getUrl(BUCKET, fileName)).willReturn(new URL("https://s3.com/" + fileName));

        ProfileImageUrlDTO result = s3Service.getImageUrl(userId);

        assertThat(result.profileImageUrl()).contains(fileName);
    }

    @Test
    void 새_이미지를_저장할_때_기존_이미지가_있으면_삭제한다() {
        Long userId = 1L;
        String oldFileName = "profile/1/old-uuid.jpeg";
        String newFileName = "profile/1/new-uuid.jpeg";

        given(amazonS3Client.doesObjectExist(BUCKET, newFileName)).willReturn(true);
        given(userService.getUserProfileFileName(userId)).willReturn(oldFileName);

        s3Service.saveFileNameToEntity(userId, newFileName);

        verify(amazonS3Client).deleteObject(BUCKET, oldFileName);
        verify(userService).updateUserProfileFileName(userId, newFileName);
    }

    @Test
    void 새_이미지를_저장할_때_기존_이미지가_기본_이미지면_삭제하지_않는다() {
        Long userId = 1L;
        String newFileName = "profile/1/new-uuid.jpeg";

        given(amazonS3Client.doesObjectExist(BUCKET, newFileName)).willReturn(true);
        given(userService.getUserProfileFileName(userId)).willReturn(DEFAULT_PROFILE_KEY);

        s3Service.saveFileNameToEntity(userId, newFileName);

        verify(amazonS3Client, never()).deleteObject(eq(BUCKET), anyString());
        verify(userService).updateUserProfileFileName(userId, newFileName);
    }

    @Test
    void S3에_존재하지_않는_파일명을_저장하려고_하면_예외가_발생한다() {
        Long userId = 1L;
        String fileName = "non-existent-file.jpeg";
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(false);

        assertThatThrownBy(() -> s3Service.saveFileNameToEntity(userId, fileName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Image does not exist");
    }

    @Test
    void 프로필_이미지를_삭제하면_S3와_DB에서_모두_삭제된다() {
        Long userId = 1L;
        String fileName = "profile/1/uuid.jpeg";
        given(userService.getUserProfileFileName(userId)).willReturn(fileName);
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(true);

        s3Service.deleteImageUrl(userId);

        verify(amazonS3Client).deleteObject(BUCKET, fileName);
        verify(userService).deleteUserProfileFileName(userId);
    }

    @Test
    void 기존_파일명과_새_파일명이_같으면_기존_이미지를_삭제하지_않는다() {
        Long userId = 1L;
        String fileName = "profile/1/uuid.jpeg";
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(true);
        given(userService.getUserProfileFileName(userId)).willReturn(fileName);

        s3Service.saveFileNameToEntity(userId, fileName);

        verify(amazonS3Client, never()).deleteObject(BUCKET, fileName);
        verify(userService).updateUserProfileFileName(userId, fileName);
    }

    @Test
    void 삭제할_프로필_이미지가_없으면_예외가_발생한다() {
        Long userId = 1L;
        given(userService.getUserProfileFileName(userId)).willReturn(" ");

        assertThatThrownBy(() -> s3Service.deleteImageUrl(userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제할 프로필 이미지가 없습니다");
    }

    @Test
    void S3에_없는_이미지를_삭제하려고_하면_예외가_발생한다() {
        Long userId = 1L;
        String fileName = "profile/1/missing.jpeg";
        given(userService.getUserProfileFileName(userId)).willReturn(fileName);
        given(amazonS3Client.doesObjectExist(BUCKET, fileName)).willReturn(false);

        assertThatThrownBy(() -> s3Service.deleteImageUrl(userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Image does not exist");
    }
}
