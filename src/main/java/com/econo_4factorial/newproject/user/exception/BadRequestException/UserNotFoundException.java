package com.econo_4factorial.newproject.user.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.user.exception.UserErrorType;

public class UserNotFoundException extends BadRequestException {
    public UserNotFoundException () {
        super(UserErrorType.USER_NOT_FOUND_EXCEPTION);
    }
}
