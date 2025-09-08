package com.econo_4factorial.newproject.mountain.repository;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Long> {
    List<Mountain> findAllByOrderByNameAsc();

    List<Mountain> findByNameContaining(String keyword);
}