package com.ll.netmong.domain.cart.itemCart.service;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.cart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.cart.itemCart.entity.ItemCart;
import com.ll.netmong.domain.cart.itemCart.repository.ItemCartRepository;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.repository.ProductRepository;
import com.ll.netmong.domain.product.service.ProductServiceImpl;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCartServiceImpl implements ItemCartService {
    private static final int MINIMUM_STOCK_COUNT = 0;
    private final ItemCartRepository itemCartRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void addProductByCart(Cart cart, Long productId, ProductCountRequest productCountRequest) {
        ItemCart findItemCart = itemCartRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (findItemCart == null) {
            addToCartForNewProduct(cart, productId, productCountRequest);
            return;
        }
        addToCartForExistingProduct(findItemCart, cart, productCountRequest);
    }

    @Override
    public List<ViewCartResponse> readMemberCartByUser(String findMemberName) {
        List<ItemCart> findItemCart = itemCartRepository.findAll();
        List<ViewCartResponse> memberProducts = new ArrayList<>();

        for (ItemCart itemCart : findItemCart) {
            if (itemCart.getCart().getMember().getUsername().equals(findMemberName)) {
                ViewCartResponse viewCartResponse = new ViewCartResponse();

                viewCartResponse.setProductName(itemCart.getProduct().getProductName());
                viewCartResponse.setPrice(itemCart.getProduct().getPrice());
                viewCartResponse.setCount(itemCart.getStackCount());

                memberProducts.add(viewCartResponse);
            }
        }
        return memberProducts;
    }

    public void addToCartForNewProduct(Cart cart, Long productId, ProductCountRequest productCountRequest) {
        Product product = productService.getProduct(productId);
        cart.addCount(productCountRequest.getCount());
        ItemCart itemCart = ItemCart.createItemCart(cart, product, productCountRequest.getCount());
        removeStock(productId, productCountRequest.getCount());
        itemCartRepository.saveAndFlush(itemCart);

    }

    public void addToCartForExistingProduct(ItemCart findItemCart, Cart cart, ProductCountRequest productCountRequest) {
        Long productId = findItemCart.getProduct().getId();
        removeStock(productId, productCountRequest.getCount());
        cart.addCount(productCountRequest.getCount());
        findItemCart.addCount(productCountRequest.getCount());
    }

    @Transactional
    public void removeStock(Long productId, Integer count) {
        try {
            transactionTemplate.execute(status -> {
                // Pessimistic Lock을 걸고 상품을 조회합니다.
                Product findByProduct = productRepository.findByIdWithPessimisticLock(productId)
                        .orElseThrow(() -> new ProductException("존재하지 않는 상품입니다.", ProductErrorCode.NOT_EXIST_PRODUCT_NAME));

                int restStock = findByProduct.getCount() - count;

                // 재고가 충분하지 않다면, 예외를 발생시킵니다.
                validateRestStockIsEnough(restStock);

                // 재고가 충분하다면, 상품을 업데이트하고 저장합니다.
                findByProduct.setCount(restStock);
                productRepository.save(findByProduct);

                return null;
            });
        } catch (CannotAcquireLockException | TransactionTimedOutException e) {
            // 데드락이 발생하면 재귀적으로 메소드를 다시 호출하여 재시도합니다.
            removeStock(productId, count);
        }
    }

    private void validateRestStockIsEnough(int restStock) {
        if (restStock < MINIMUM_STOCK_COUNT) {
            throw new ProductException("남아있는 재고가 부족합니다.", ProductErrorCode.NOT_ENOUGH_PRODUCT_STOCK);
        }
    }
}
