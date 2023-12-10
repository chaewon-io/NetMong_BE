package com.ll.netmong.domain.image.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@DynamicInsert
@Table(name = "image", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE image SET status = 'N' where id = ?")
public class Image extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url")
    private String imageUrl;

    @ColumnDefault("'Y'")
    private String status;

    public Image(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getProductImages().add(this);
    }
}
