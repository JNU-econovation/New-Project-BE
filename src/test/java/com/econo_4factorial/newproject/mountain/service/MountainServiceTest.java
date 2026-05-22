package com.econo_4factorial.newproject.mountain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import com.econo_4factorial.newproject.mountain.repository.MountainRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MountainServiceTest {

    @Mock
    private MountainRepository mountainRepository;

    @InjectMocks
    private MountainService mountainService;

    @Test
    void 모든_산을_이름순으로_조회한다() {
        Mountain mountain = mock(Mountain.class);
        given(mountain.getId()).willReturn(1L);
        given(mountain.getName()).willReturn("무등산");
        given(mountain.getLocation()).willReturn("광주");
        given(mountain.getLongitude()).willReturn(new BigDecimal("126.9890"));
        given(mountain.getLatitude()).willReturn(new BigDecimal("35.1340"));
        given(mountainRepository.findAllByOrderByNameAsc()).willReturn(List.of(mountain));

        List<MountainDTO> result = mountainService.findAll();

        assertThat(result).containsExactly(
                new MountainDTO(1L, "무등산", "광주", List.of(new BigDecimal("126.9890"), new BigDecimal("35.1340")))
        );
    }

    @Test
    void 없는_산이면_예외가_발생한다() {
        given(mountainRepository.existsById(99L)).willReturn(false);

        assertThatThrownBy(() -> mountainService.isMountainExistOrThrow(99L))
                .isInstanceOf(MountainNotFoundException.class)
                .hasMessage("산을 찾을 수 없습니다");
    }

    @Test
    void 초성으로_산을_추천한다() {
        Mountain mountain = mock(Mountain.class);
        given(mountain.getId()).willReturn(2L);
        given(mountain.getName()).willReturn("북한산");
        given(mountain.getLongitude()).willReturn(new BigDecimal("126.9805"));
        given(mountain.getLatitude()).willReturn(new BigDecimal("37.6587"));
        given(mountainRepository.findByInitialsStartingWith("ㅂㅎ")).willReturn(List.of(mountain));

        List<SuggestedMountainDTO> result = mountainService.suggestMountainsByInitials("ㅂㅎ");

        assertThat(result).containsExactly(
                new SuggestedMountainDTO(2L, "북한산", List.of(new BigDecimal("126.9805"), new BigDecimal("37.6587")))
        );
    }

    @Test
    void 단어로_산을_추천한다() {
        Mountain mountain = mock(Mountain.class);
        given(mountain.getId()).willReturn(3L);
        given(mountain.getName()).willReturn("지리산");
        given(mountain.getLongitude()).willReturn(new BigDecimal("127.7300"));
        given(mountain.getLatitude()).willReturn(new BigDecimal("35.3360"));
        given(mountainRepository.findByNameContaining("지리")).willReturn(List.of(mountain));

        List<SuggestedMountainDTO> result = mountainService.suggestMountainsByWords("지리");

        assertThat(result).containsExactly(
                new SuggestedMountainDTO(3L, "지리산", List.of(new BigDecimal("127.7300"), new BigDecimal("35.3360")))
        );
    }
}
