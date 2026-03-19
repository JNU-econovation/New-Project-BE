package com.econo_4factorial.newproject.mountain.service;

import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.Normalizer;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SuggestMountainServiceTest {

    @Mock
    private MountainService mountainService;

    @InjectMocks
    private SuggestMountainService suggestMountainService;

    @Test
    void 빈_키워드면_빈_목록을_반환한다() {
        List<SuggestedMountainDTO> result = suggestMountainService.suggestMountains("   ");

        assertThat(result).isEmpty();
        verifyNoInteractions(mountainService);
    }

    @Test
    void 초성_키워드면_초성검색을_위임한다() {
        List<SuggestedMountainDTO> suggestions = List.of(new SuggestedMountainDTO(1L, "무등산", List.of()));
        given(mountainService.suggestMountainsByInitials("ㅁㄷ")).willReturn(suggestions);

        List<SuggestedMountainDTO> result = suggestMountainService.suggestMountains("  ㅁㄷ  ");

        assertThat(result).containsExactlyElementsOf(suggestions);
        verify(mountainService).suggestMountainsByInitials("ㅁㄷ");
    }

    @Test
    void 일반_키워드면_이름검색을_위임한다() {
        String decomposed = Normalizer.normalize("무등", Normalizer.Form.NFD);
        List<SuggestedMountainDTO> suggestions = List.of(new SuggestedMountainDTO(2L, "무등산", List.of()));
        given(mountainService.suggestMountainsByWords("무등")).willReturn(suggestions);

        List<SuggestedMountainDTO> result = suggestMountainService.suggestMountains(decomposed);

        assertThat(result).containsExactlyElementsOf(suggestions);
        verify(mountainService).suggestMountainsByWords("무등");
    }
}
