package com.ll.netmong.domain.product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductErrorCode {
    NOT_EXIST_PRODUCT(500, "상품이 존재 하지 않습니다.");

    private final int statusCode;
    private final String message;
}
