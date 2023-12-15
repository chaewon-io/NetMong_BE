package com.ll.netmong.domain.cart.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewCartResponse {
    private Long productId;
    private String productName;
    private String price;
    private Integer count;
}
