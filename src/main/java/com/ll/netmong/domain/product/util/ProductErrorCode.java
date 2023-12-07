package com.ll.netmong.domain.product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductErrorCode {
    NOT_EXIST_PRODUCT(500, "상품이 존재 하지 않습니다."),
    NOT_ENOUGH_PRODUCT_STOCK(500, "남아있는 재고가 부족합니다.");

    private final int statusCode;
    private final String message;
}
