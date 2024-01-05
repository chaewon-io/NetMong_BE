package com.ll.netmong.domain.member.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
    String repeatPassword;
}
