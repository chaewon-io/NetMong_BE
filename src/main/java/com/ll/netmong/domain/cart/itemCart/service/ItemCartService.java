package com.ll.netmong.domain.cart.itemCart.service;

import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.cart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.cart.entity.Cart;

import java.util.List;

public interface ItemCartService {
    void addProductByCart(Cart cart, Long productId, ProductCountRequest productCountRequest);
    List<ViewCartResponse> readMemberCartByUser(String findMemberName);
}
