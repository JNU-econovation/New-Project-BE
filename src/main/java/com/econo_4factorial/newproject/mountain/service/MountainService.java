package com.econo_4factorial.newproject.mountain.service;

import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import com.econo_4factorial.newproject.mountain.repository.MountainRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MountainService {
    private final MountainRepository mountainRepository;

    public List<MountainDTO> findAll() {
        return mountainRepository.findAllByOrderByNameAsc()
                .stream()
                .map(MountainDTO::from)
                .toList();
    }

    public void isMountainExistOrThrow(Long mountainId) {
        if (!mountainRepository.existsById(mountainId)) {
            throw new MountainNotFoundException();
        }
    }

    public List<SuggestedMountainDTO> suggestMountainsByInitials(String initials) {
        return mountainRepository.findByInitialsStartingWith(initials)
                .stream()
                .map(SuggestedMountainDTO::from)
                .toList();
    }
    
    public List<SuggestedMountainDTO> suggestMountainsByWords(String words) {
        return mountainRepository.findByNameContaining(words)
                .stream()
                .map(SuggestedMountainDTO::from)
                .toList();
    }
}
