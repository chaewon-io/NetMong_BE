package com.ll.netmong.domain.productCart.repository;

import com.ll.netmong.domain.productCart.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    ItemCart findByCartIdAndProductId(Long cartId, Long productId);
}
