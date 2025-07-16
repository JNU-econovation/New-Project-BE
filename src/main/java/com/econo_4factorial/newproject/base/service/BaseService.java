package com.econo_4factorial.newproject.base.service;

import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final BaseRepository baseRepository;

    @Transactional(readOnly = true)
    public List<BaseDTO> getBasesByMountainId(Long mountainId) {
        return baseRepository.findByMountainId(mountainId)
                .stream()
                .map(BaseDTO::from)
                .toList();
    }
}
