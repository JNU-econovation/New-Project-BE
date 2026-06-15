package com.econo_4factorial.newproject.mountain.service;

import com.econo_4factorial.newproject.mountain.domain.SearchKeyword;
import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuggestMountainService {

    private final MountainService mountainService;

    public List<SuggestedMountainDTO> suggestMountains(String keyword) {
        SearchKeyword searchKeyword = new SearchKeyword(keyword);

        if (searchKeyword.isEmpty()) {
            return List.of();
        }

        if (searchKeyword.isInitials()) {
            return mountainService.suggestMountainsByInitials(searchKeyword.value());
        }
        return mountainService.suggestMountainsByWords(searchKeyword.value());
    }
}
