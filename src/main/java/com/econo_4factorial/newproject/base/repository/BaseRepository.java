package com.econo_4factorial.newproject.base.repository;

import com.econo_4factorial.newproject.base.domain.Base;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, Long> {
    List<Base> findByMountainId(Long mountainId);
}
