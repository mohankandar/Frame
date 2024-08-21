package com.wynd.vop.framework.exception;


import com.wynd.vop.framework.messages.MessageKey;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.springframework.http.HttpStatus;

public class S3Exception extends BipRuntimeException {

        public S3Exception(MessageKey key, MessageSeverity severity, HttpStatus status, String... params) {
            super(key, severity, status, params);
        }

        public S3Exception(MessageKey key, MessageSeverity severity, HttpStatus status, Throwable cause, String... params) {
            super(key, severity, status, cause, params);
        }
}