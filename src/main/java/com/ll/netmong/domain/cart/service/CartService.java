package com.ll.netmong.domain.cart.service;

import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.member.entity.Member;

public interface CartService {
    void createCart(Member member);

    void addProductByCart(String findByMemberName, Long productId, ProductCountRequest productCountRequest);
}
