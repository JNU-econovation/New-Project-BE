package com.econo_4factorial.newproject.facility.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.econo_4factorial.newproject.facility.domain.Facility;
import com.econo_4factorial.newproject.facility.domain.FacilityType;
import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import com.econo_4factorial.newproject.facility.repository.FacilityRepository;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private MountainService mountainService;

    @InjectMocks
    private FacilityService facilityService;

    @Test
    void 산의_시설목록을_조회한다() {
        Facility facility = mock(Facility.class);
        given(facility.getId()).willReturn(1L);
        given(facility.getName()).willReturn("중머리재 화장실");
        given(facility.getType()).willReturn(FacilityType.TOILET);
        given(facility.getLongitude()).willReturn(new BigDecimal("126.9931"));
        given(facility.getLatitude()).willReturn(new BigDecimal("35.1402"));
        given(facilityRepository.findByMountainId(10L)).willReturn(List.of(facility));

        List<FacilityDTO> result = facilityService.getFacilitiesByMountainId(10L);

        assertThat(result).containsExactly(
                new FacilityDTO(1L, "중머리재 화장실", FacilityType.TOILET,
                        List.of(new BigDecimal("126.9931"), new BigDecimal("35.1402")))
        );
        verify(mountainService).isMountainExistOrThrow(10L);
    }

    @Test
    void 없는_산의_시설을_조회하면_예외가_발생한다() {
        MountainNotFoundException exception = new MountainNotFoundException();
        doThrow(exception).when(mountainService).isMountainExistOrThrow(20L);

        assertThatThrownBy(() -> facilityService.getFacilitiesByMountainId(20L))
                .isSameAs(exception);
        verifyNoInteractions(facilityRepository);
    }
}
