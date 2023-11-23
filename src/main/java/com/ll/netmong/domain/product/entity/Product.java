package com.ll.netmong.domain.product.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.product.util.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Product extends BaseEntity {
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private String price;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private Category category;

    @Lob
    @Column(name = "product_content")
    private String content;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<Image> productImages = new ArrayList<>();

    public static Image createProductImage(String imageUrl) {
        return new Image(imageUrl);
    }

    public void addProductImage(Image productImage) {
        productImages.add(productImage);
        productImage.setProduct(this);
    }
}
