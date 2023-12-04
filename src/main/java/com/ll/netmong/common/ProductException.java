package com.ll.netmong.common;

import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final ProductErrorCode productErrorCode;

    public ProductException(String message, ProductErrorCode productErrorCode) {
        super(message);
        this.productErrorCode = productErrorCode;
    }
}
