package com.econo_4factorial.newproject.facility.repository;

import com.econo_4factorial.newproject.facility.domain.Facility;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByMountainId(Long mountainId);
}
