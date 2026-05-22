package com.econo_4factorial.newproject.base.service;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.domain.BaseImage;
import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.dto.BaseDetailDTO;
import com.econo_4factorial.newproject.base.repository.BaseImageRepository;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final BaseRepository baseRepository;
    private final BaseImageRepository baseImageRepository;
    private final MountainService mountainService;

    @Transactional(readOnly = true)
    public List<BaseDTO> getBasesByMountainId(Long mountainId) {
        mountainService.isMountainExistOrThrow(mountainId);

        return baseRepository.findByMountainId(mountainId)
                .stream()
                .map(BaseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BaseDetailDTO> getBaseDetailsByMountainId(Long mountainId) {
        mountainService.isMountainExistOrThrow(mountainId);

        List<Base> bases = baseRepository.findByMountainId(mountainId);

        return bases.stream()
                .map(base -> {
                    List<BaseImage> baseImages = baseImageRepository.findByBaseId(base.getId());
                    return BaseDetailDTO.from(base, baseImages);
                })
                .toList();
    }
}
