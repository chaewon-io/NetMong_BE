package com.ll.netmong.domain.productCart.service;

import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.productCart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.productCart.entity.Cart;
import com.ll.netmong.domain.productCart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void createCart(Member member) {
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    @Override
    public ViewCartResponse viewCartResponse(Long userId) {
        return null;

    }
}
