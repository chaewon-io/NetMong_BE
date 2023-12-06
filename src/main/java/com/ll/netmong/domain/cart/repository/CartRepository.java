package com.ll.netmong.domain.cart.repository;

import com.ll.netmong.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberUsername(String findMemberName);
}
