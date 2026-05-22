package com.econo_4factorial.newproject.facility.service;

import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import com.econo_4factorial.newproject.facility.repository.FacilityRepository;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final MountainService mountainService;

    @Transactional(readOnly = true)
    public List<FacilityDTO> getFacilitiesByMountainId(Long mountainId) {
        mountainService.isMountainExistOrThrow(mountainId);

        return facilityRepository.findByMountainId(mountainId)
                .stream()
                .map(FacilityDTO::from)
                .toList();
    }
}
