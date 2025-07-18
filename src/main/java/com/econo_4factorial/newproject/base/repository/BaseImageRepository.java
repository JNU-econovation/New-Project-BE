package com.econo_4factorial.newproject.base.repository;

import com.econo_4factorial.newproject.base.domain.BaseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseImageRepository extends JpaRepository<BaseImage, Long> {
    List<BaseImage> findByBaseId(Long baseId);
}
