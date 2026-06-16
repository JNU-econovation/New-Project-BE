package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.pathway.domain.Pathway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathwayRepository extends JpaRepository<Pathway, Long> {
}
