package com.ll.netmong.domain.productCart.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.productCart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.productCart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products/cart")
public class CartController {
    private static final String CART_SUCCESS_PRODUCT = "장바구니에 상품이 등록 되었습니다.";
    private final CartService cartService;

    @GetMapping
    public RsData readProductCartByUser(@AuthenticationPrincipal UserDetails userDetails) {
        String findMemberName = userDetails.getUsername();
        return RsData.successOf(cartService.readMemberCartByUser(findMemberName));
    }

    @PostMapping("{productId}")
    public RsData addMyCart(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable(name = "productId") Long productId,
                          @RequestBody ProductCountRequest productCountRequest) {
        String findMemberName = userDetails.getUsername();
        cartService.addProductByCart(findMemberName, productId, productCountRequest);

        return RsData.of("S-1", CART_SUCCESS_PRODUCT, "create");
    }
}
