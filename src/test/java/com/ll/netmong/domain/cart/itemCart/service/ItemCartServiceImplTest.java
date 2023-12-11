package com.ll.netmong.domain.cart.itemCart.service;

import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.repository.ProductRepository;
import com.ll.netmong.domain.product.util.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ItemCartServiceImplTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ItemCartServiceImpl itemCartService;

    private Product product;

    @BeforeEach
    void init() {
        product = Product.builder().productName("강아지 사료")
                .price("25_000")
                .content("유통기한 1년 남은 사료입니다.")
                .count(1000)
                .category(Category.PET_FEED)
                .status("Y")
                .build();
        productRepository.save(product);
    }

    @Test
    public void 동시에_20개_요청() throws InterruptedException {
        int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemCartService.removeStock(product.getId(), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        Assertions.assertThat(updatedProduct).isNotNull();
        Assertions.assertThat(updatedProduct.getCount()).isEqualTo(0);
    }
}