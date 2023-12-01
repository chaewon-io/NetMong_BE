package com.ll.netmong.domain.productCart.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.productCart.dto.response.ViewCartResponse;

public interface CartService {
    void createCart(Member member);
//    void addItemByCart()

    ViewCartResponse viewCartResponse(Long productId);
}
