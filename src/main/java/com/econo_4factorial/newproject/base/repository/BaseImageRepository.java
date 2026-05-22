package com.econo_4factorial.newproject.base.repository;

import com.econo_4factorial.newproject.base.domain.BaseImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseImageRepository extends JpaRepository<BaseImage, Long> {
    List<BaseImage> findByBaseId(Long baseId);
}
