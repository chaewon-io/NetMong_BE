package com.ll.netmong.domain.productCart.service;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.repository.ProductRepository;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import com.ll.netmong.domain.productCart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.productCart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.productCart.entity.Cart;
import com.ll.netmong.domain.productCart.entity.ItemCart;
import com.ll.netmong.domain.productCart.repository.CartRepository;
import com.ll.netmong.domain.productCart.repository.ItemCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ItemCartRepository itemCartRepository;

    @Override
    @Transactional
    public void createCart(Member member) {
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addProductByCart(String findByMemberName, Long productId, ProductCountRequest productCountRequest) {
        Product product = validateExistProduct(productId);
        Cart cart = validateExistMember(findByMemberName);
        ItemCart findItemCart = itemCartRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (findItemCart == null) {
            cart.addCount(productCountRequest.getCount());
            findItemCart = ItemCart.createItemCart(cart, product, productCountRequest.getCount());
            findItemCart.removeStock(product, productCountRequest.getCount());
            itemCartRepository.save(findItemCart);
            return;
        }

        findItemCart.removeStock(product, productCountRequest.getCount());
        cart.addCount(productCountRequest.getCount());
        findItemCart.addCount(productCountRequest.getCount());
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

    private Cart validateExistMember(String findByMemberName) {
        return cartRepository.findByMemberUsername(findByMemberName)
                .orElseThrow(() -> new ProductException("회원이 존재하지 않습니다.", ProductErrorCode.NOT_EXIST_PRODUCT));
    }

    private Product validateExistProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("상품이 존재하지 않습니다.", ProductErrorCode.NOT_EXIST_PRODUCT));
    }
}
