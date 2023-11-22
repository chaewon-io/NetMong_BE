package com.ll.netmong.common;

import com.ll.netmong.member.exception.NotMatchPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.auth.login.AccountNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RsData handleAccountNotFound(AccountNotFoundException e) {
        return RsData.failOf(e.getMessage());
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RsData handleNotMatchPassword(NotMatchPasswordException e) {
        return RsData.failOf(e.getMessage());
    }

    // 그 외 예외
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RsData handleException(Exception e) {
        return RsData.failOf("Unexpected error");
    }
}