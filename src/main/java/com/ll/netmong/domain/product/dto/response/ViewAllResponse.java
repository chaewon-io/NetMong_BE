package com.ll.netmong.domain.product.dto.response;

import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.util.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewAllResponse {
    private String productName;
    private String price;
    private String content;
    private Category category;

    public ViewAllResponse(Product product) {
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.content = product.getContent();
        this.category = product.getCategory();
    }

    public static ViewAllResponse pageByProduct(Product product) {
        return new ViewAllResponse(product);
    }
}
