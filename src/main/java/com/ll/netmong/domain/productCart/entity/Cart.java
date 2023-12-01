package com.ll.netmong.domain.productCart.entity;

import com.ll.netmong.common.BaseEntity;
import com.ll.netmong.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Table(name = "product_cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Cart extends BaseEntity {

    @Column(name = "item_count_in_cart")
    private Integer count;
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;
    // TODO :  상품, 회원과 연관관계 작성
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cart createCart(Member member) {
        return Cart.builder()
                .count(0)
                .member(member)
                .build();
    }
}
