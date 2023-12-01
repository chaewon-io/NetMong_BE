package com.ll.netmong.domain.productCart.controller;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.productCart.entity.Cart;
import com.ll.netmong.domain.productCart.service.CartService;
import com.ll.netmong.domain.productCart.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public void readProductCartByUser() {
//        cartService.viewCartResponse();
    }

    @PostMapping("{productId}")
    public void addItemByCart(@PathVariable(name = "productId") String productId, Member foundMember){

        Cart.createCart(foundMember);
    }
}
