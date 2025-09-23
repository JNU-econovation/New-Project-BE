package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class FailToSendSmsException extends InternalServerException {
  public FailToSendSmsException() {
    super(AuthErrorType.FAIL_TO_SEND_SMS_EXCEPTION);
  }
}
