package com.ll.netmong.domain.product.dto.response;

import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private String message;

    public ErrorResponse(ProductErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }
}
