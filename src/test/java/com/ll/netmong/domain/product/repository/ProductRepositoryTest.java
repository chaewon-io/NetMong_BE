package com.ll.netmong.domain.product.repository;

import com.ll.netmong.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품 저장 확인")
    @Test
    void create_Product() {
        productRepository.save(Product.builder().productName("강아지 사료")
                .price("25_000")
                .content("유통기한 1년 남은 사료입니다.")
                .build());

        assertThat(productRepository.findAll().size()).isEqualTo(1);
    }
}
