package com.ll.netmong.domain.product.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.util.Category;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE product SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Product extends BaseEntity {
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private String price;

    @Lob
    @Column(name = "product_content")
    private String content;

    @Column(name = "product_count")
    private Integer count;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private Category category;

    @Column(name = "deleted_at")
    private String deleteDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    @OneToMany(
            mappedBy = "product",
            orphanRemoval = true)
    @Builder.Default
    private List<Image> productImages = new ArrayList<>();

    public static Image createProductImage(String imageUrl) {
        return new Image(imageUrl);
    }

    public void addProductImage(Image productImage) {
        productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void modifyProduct(UpdateRequest updateRequest) {
        this.productName = updateRequest.getProductName();
        this.price = updateRequest.getPrice();
        this.count = updateRequest.getCount();
        this.content = updateRequest.getContent();
        this.category = updateRequest.getCategory();
    }

    public void removeStock(int quantity) {
        int restStock = this.count - quantity;

        if (restStock < 0) {
            throw new ProductException("남아있는 재고가 부족합니다.", ProductErrorCode.NOT_ENOUGH_PRODUCT_STOCK);
        }
        this.count = restStock;
    }
}
