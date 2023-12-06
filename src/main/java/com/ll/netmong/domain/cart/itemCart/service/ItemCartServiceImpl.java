package com.ll.netmong.domain.cart.itemCart.service;

import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.cart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.cart.itemCart.entity.ItemCart;
import com.ll.netmong.domain.cart.itemCart.repository.ItemCartRepository;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCartServiceImpl implements ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final ProductServiceImpl productService;

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

    private void addToCartForNewProduct(Cart cart, Long productId, ProductCountRequest productCountRequest) {
        Product product = productService.getProduct(productId);
        cart.addCount(productCountRequest.getCount());
        ItemCart newItemCart = ItemCart.createItemCart(cart, product, productCountRequest.getCount());
        newItemCart.removeStock(product, productCountRequest.getCount());
        itemCartRepository.save(newItemCart);
    }

    private void addToCartForExistingProduct(ItemCart findItemCart, Cart cart, ProductCountRequest productCountRequest) {
        Product product = findItemCart.getProduct();
        findItemCart.removeStock(product, productCountRequest.getCount());
        cart.addCount(productCountRequest.getCount());
        findItemCart.addCount(productCountRequest.getCount());
    }
}
