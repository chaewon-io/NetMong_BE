package com.ll.netmong.domain.productCart.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.productCart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.productCart.dto.response.ViewCartResponse;

import java.util.List;

public interface CartService {
    void createCart(Member member);

    void addProductByCart(String findByMemberName, Long productId, ProductCountRequest productCountRequest);

    List<ViewCartResponse> readMemberCartByUser(String findMemberName);
}
