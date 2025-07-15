package com.econo_4factorial.newproject.base.repository;

import com.econo_4factorial.newproject.base.domain.Base;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, Long> {
    //추후 customRepository로 변경될수도
}