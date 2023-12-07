package com.ll.netmong.domain.product.repository;

import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.util.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByProductName(String productName);
}
