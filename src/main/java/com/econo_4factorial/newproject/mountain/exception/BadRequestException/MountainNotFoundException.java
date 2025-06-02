package com.econo_4factorial.newproject.mountain.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.mountain.exception.MountainErrorType;

public class MountainNotFoundException extends BadRequestException {
    public MountainNotFoundException() {
        super(MountainErrorType.MOUNTAIN_NOT_FOUND_EXCEPTION);
    }
}
