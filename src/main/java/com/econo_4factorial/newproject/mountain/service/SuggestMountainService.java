package com.econo_4factorial.newproject.mountain.service;

import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestMountainService {
    private static final String INITIAL_CONSTANTS = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    private final MountainService mountainService;

    public List<SuggestedMountainDTO> suggestMountains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        final String normalizedKeyword = normalize(keyword);

        if (isInitials(normalizedKeyword)) {
            return mountainService.suggestMountainsByInitials(normalizedKeyword);
        } else {
            return mountainService.suggestMountainsByWords(normalizedKeyword);
        }
    }

    private String normalize(String beforeNormalized) {
        return Normalizer.normalize(beforeNormalized.trim(), Normalizer.Form.NFC);
    }

    private boolean isInitials(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (INITIAL_CONSTANTS.indexOf(c) < 0) return false;
        }
        return true;
    }
}
