package com.wynd.vop.framework.exception;

import com.wynd.vop.framework.messages.MessageKey;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.springframework.http.HttpStatus;

public class SqsException extends BipRuntimeException {
    public SqsException(MessageKey key, MessageSeverity severity, HttpStatus status, String... params) {
        super(key, severity, status, params);
    }

    public SqsException(MessageKey key, MessageSeverity severity, HttpStatus status, Throwable cause, String... params) {
        super(key, severity, status, cause, params);
    }

}
