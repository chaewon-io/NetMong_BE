package com.ll.netmong.domain.cart.service;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.cart.itemCart.entity.ItemCart;
import com.ll.netmong.domain.cart.itemCart.service.ItemCartService;
import com.ll.netmong.domain.cart.repository.CartRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ItemCartService itemCartService;

    @Override
    @Transactional
    public void createCart(Member member) {
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addProductByCart(String findByMemberName, Long productId, ProductCountRequest productCountRequest) {
        Cart cart = validateExistMember(findByMemberName);

        Optional<ItemCart> findItemCart = Optional.ofNullable(itemCartService.getItemCart(cart, productId));

        if (findItemCart.isEmpty()) {
            itemCartService.addToCartForNewProduct(cart, productId, productCountRequest);
            return;
        }
        itemCartService.addToCartForExistingProduct(findItemCart.get(), cart, productCountRequest);
    }

    private Cart validateExistMember(String findByMemberName) {
        return cartRepository.findByMemberEmail(findByMemberName)
                .orElseThrow(() -> new ProductException("회원이 존재하지 않습니다.", ProductErrorCode.NOT_EXIST_PRODUCT));
    }
}
