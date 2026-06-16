package com.econo_4factorial.newproject.mountain.repository;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MountainRepository extends JpaRepository<Mountain, Long> {
    List<Mountain> findAllByOrderByNameAsc();

    List<Mountain> findByInitialsStartingWith(String keyword);

    List<Mountain> findByNameContaining(String keyword);
}
