package com.econo_4factorial.newproject.mountain.dto.res;

import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import java.util.List;

public record GetSuggestedMountainRes(
        List<SuggestedMountainDTO> suggestedMountainDTOs
) {
    public static GetSuggestedMountainRes from(List<SuggestedMountainDTO> suggestedMountainDTOs) {
        return new GetSuggestedMountainRes(suggestedMountainDTOs);
    }
}
