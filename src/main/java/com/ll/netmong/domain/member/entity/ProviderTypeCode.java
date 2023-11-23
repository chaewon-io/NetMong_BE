package com.ll.netmong.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProviderTypeCode {
	NETMONG("NETMONG"),
	KAKAO("KAKAO"),
	NAVER("NAVER"),
	GOOGLE("GOOGLE");

	private final String code;

	public static ProviderTypeCode codeOf(final String rawCode) {
		return Arrays.stream(values()).filter(e -> e.getCode().equals(rawCode)).findFirst().orElse(NETMONG);
	}
}