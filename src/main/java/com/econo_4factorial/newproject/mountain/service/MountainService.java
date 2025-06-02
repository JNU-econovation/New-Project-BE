package com.econo_4factorial.newproject.mountain.service;

import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MountainService {
    private final MountainRepository mountainRepository;

    @Transactional(readOnly = true)
    public List<MountainDTO> findAll() {
        return mountainRepository.findAllByOrderByNameAsc()
                .stream()
                .map(MountainDTO::from)
                .toList();
    }
}
