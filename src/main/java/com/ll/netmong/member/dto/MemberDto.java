package com.ll.netmong.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MemberDto {
    private String username;
    private String password;

    private String realname;
    private String email;
    private String roles;
}
