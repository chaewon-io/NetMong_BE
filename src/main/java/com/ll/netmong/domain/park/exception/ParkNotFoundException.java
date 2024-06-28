package com.ll.netmong.domain.park.exception;

public class ParkNotFoundException extends RuntimeException {
    public ParkNotFoundException(String message) {
        super(message);
    }

}
