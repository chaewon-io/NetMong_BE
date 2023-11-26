package com.ll.netmong.domain.product.repository;

import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void init() {
        product = Product.builder().productName("강아지 사료")
                .price("25_000")
                .content("유통기한 1년 남은 사료입니다.")
                .build();
    }

    @DisplayName("상품 저장 확인")
    @Test
    void create_Product() {
        Product saveProduct = productRepository.save(product);
        assertThat(saveProduct.getProductName())
                .isEqualTo(product.getProductName());
    }

    @DisplayName("등록된 상품 단일 조회 확인")
    @Test
    void find_By_Single_Product() {
        Product findProduct = productRepository.findById(1L).orElseThrow();
        assertThat(findProduct.getProductName()).isEqualTo("강아지 사료");
    }

    @DisplayName("등록된 상품 수정 확인")
    @Test
    void modify_Product() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setProductName("고양이 사료");
        product.modifyProduct(updateRequest);

        assertThat(product.getProductName()).isEqualTo("고양이 사료");
    }

    @DisplayName("등록된 상품 삭제 확인")
    @Test
    void product_delete() {
        productRepository.delete(product);

        Optional<Product> findProduct = productRepository.findById(1L);
        assertThat(findProduct).isEmpty();
    }

    @DisplayName("등록된 상품이 없을경우 예외 확인")
    @Test
    void product_Not_Exist() {
        Optional<Product> findProduct = productRepository.findById(2L);

        Assertions.assertThatThrownBy(findProduct::orElseThrow)
                .isInstanceOf(NoSuchElementException.class);
    }
}
