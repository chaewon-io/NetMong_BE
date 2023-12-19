package com.ll.netmong.domain.cart.itemCart.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Table(name = "product_item_cart", indexes = {
        @Index(name = "idx_cart_id", columnList = "cart_id"),
        @Index(name = "idx_product_id", columnList = "product_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ItemCart extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "item_stack_count")
    private Integer stackCount;

    @Column(name = "image_url")
    private String imageUrl;

    public static ItemCart createItemCart(Cart cart, Product product, Integer count) {
        return ItemCart.builder()
                .cart(cart)
                .product(product)
                .stackCount(count)
                .imageUrl(product.getImage().getImageUrl())
                .build();
    }

    public void addCount(int number) {
        this.stackCount += number;
    }
}
