package com.econo_4factorial.newproject.facility.service;

import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import com.econo_4factorial.newproject.facility.exception.BadRequestException.FacilityNotFoundException;
import com.econo_4factorial.newproject.facility.repository.FacilityRepository;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final MountainService mountainService;

    @Transactional(readOnly = true)
    public List<FacilityDTO> getFacilitiesByMountainId(Long mountainId) {
        mountainService.isMountainExistThrow(mountainId);

        List<FacilityDTO> facilityDTOS = facilityRepository.findByMountainId(mountainId)
                .stream()
                .map(FacilityDTO::from)
                .toList();

        if(facilityDTOS.isEmpty()) {
            throw new FacilityNotFoundException();
        }
        return facilityDTOS;
    }
}
