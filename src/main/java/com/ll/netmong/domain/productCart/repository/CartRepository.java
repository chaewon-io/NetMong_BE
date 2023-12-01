package com.ll.netmong.domain.productCart.repository;

import com.ll.netmong.domain.productCart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
