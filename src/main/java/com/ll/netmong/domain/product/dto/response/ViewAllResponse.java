package com.ll.netmong.domain.product.dto.response;

import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.util.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ViewAllResponse {
    private Long productId;
    private String productName;
    private String price;
    private String content;
    private Integer count;
    private Category category;
    private String imageUrl;

    public ViewAllResponse(Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.content = product.getContent();
        this.count = product.getCount();
        this.category = product.getCategory();
        this.imageUrl = Optional.ofNullable(product.getImage())
                .map(Image::getImageUrl)
                .orElse(null);
    }

    public static ViewAllResponse pageByProduct(Product product) {
        return new ViewAllResponse(product);
    }
}
