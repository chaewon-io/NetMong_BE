package com.ll.netmong.domain.product.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.util.Category;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Where(clause = "status = 'Y'")
@SQLDelete(sql = "UPDATE product SET status = 'N' where id = ?")
public class Product extends BaseEntity {
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private String price;

    @Lob
    @Column(name = "product_content")
    private String content;

    @Column(name = "product_count")
    @Setter
    private Integer count;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private Category category;

    @ColumnDefault("'Y'")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(orphanRemoval = true)
    @Builder.Default
    private List<Image> productImages = new ArrayList<>();

    public static Image createProductImage(String imageUrl) {
        return new Image(imageUrl);
    }

    public void addProductImage(Image productImage) {
        productImages.add(productImage);
    }

    public void modifyProduct(UpdateRequest updateRequest) {
        this.productName = updateRequest.getProductName();
        this.price = updateRequest.getPrice();
        this.count = updateRequest.getCount();
        this.content = updateRequest.getContent();
        this.category = updateRequest.getCategory();
    }
}
