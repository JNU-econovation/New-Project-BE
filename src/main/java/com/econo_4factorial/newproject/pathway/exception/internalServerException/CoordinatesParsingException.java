package com.econo_4factorial.newproject.pathway.exception.internalServerException;

import com.econo_4factorial.newproject.common.exception.InternalServerException;
import com.econo_4factorial.newproject.pathway.exception.PathwayErrorType;

public class CoordinatesParsingException extends InternalServerException {
    public CoordinatesParsingException() {
        super(PathwayErrorType.COORDINATES_PARSING_EXCEPTION);
    }
}
