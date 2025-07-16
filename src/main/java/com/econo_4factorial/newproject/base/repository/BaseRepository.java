package com.econo_4factorial.newproject.base.repository;

import com.econo_4factorial.newproject.base.domain.Base;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseRepository extends JpaRepository<Base, Long> {
    List<Base> findByMountainId(Long mountainId);
}