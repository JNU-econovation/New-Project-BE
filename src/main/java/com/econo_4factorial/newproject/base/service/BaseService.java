package com.econo_4factorial.newproject.base.service;

import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.exception.BadRequestException.BaseNotFoundException;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final BaseRepository baseRepository;
    private final MountainService mountainService;

    @Transactional(readOnly = true)
    public List<BaseDTO> getBasesByMountainId(Long mountainId) {
        mountainService.isMountainExistOrThrow(mountainId);

        List<BaseDTO> baseDTOS = baseRepository.findByMountainId(mountainId)
                .stream()
                .map(BaseDTO::from)
                .toList();

        if (baseDTOS.isEmpty()) {
            throw new BaseNotFoundException();
        }

        return baseDTOS;
    }
}
