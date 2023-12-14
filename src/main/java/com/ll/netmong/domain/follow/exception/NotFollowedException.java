package com.ll.netmong.domain.follow.exception;

public class NotFollowedException extends RuntimeException {
    public NotFollowedException() {

    }

    public NotFollowedException(String msg) {
        super(msg);
    }

    public NotFollowedException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
