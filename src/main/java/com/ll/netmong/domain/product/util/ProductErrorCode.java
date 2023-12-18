package com.ll.netmong.domain.product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductErrorCode {
    NOT_EXIST_PRODUCT(500, "상품이 존재 하지 않습니다."),
    NOT_EXIST_PRODUCT_CATEGORY(500, "존재하지 않는 카테고리 입니다."),
    NOT_EXIST_PRODUCT_NAME(500, "존재 하지 않는 상품 이름 입니다."),
    NOT_ENOUGH_PRODUCT_STOCK(500, "남아있는 재고가 부족합니다."),
    NOT_CHANGE_PRODUCT(500, "등록한 사용자가 아니면 변경 할 수 없습니다.");

    private final int statusCode;
    private final String message;
}
