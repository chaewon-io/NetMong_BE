package com.ll.netmong.domain.product.dto.request;

import com.ll.netmong.domain.product.util.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequest {
    private String productName;
    private String price;
    private String content;
    private Category category;
}
