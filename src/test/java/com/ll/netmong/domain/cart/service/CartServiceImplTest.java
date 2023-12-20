package com.ll.netmong.domain.cart.service;

import com.ll.netmong.domain.cart.dto.response.ViewCartResponse;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.cart.itemCart.entity.ItemCart;
import com.ll.netmong.domain.cart.itemCart.repository.ItemCartRepository;
import com.ll.netmong.domain.cart.itemCart.service.ItemCartServiceImpl;
import com.ll.netmong.domain.cart.repository.CartRepository;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;
    @InjectMocks
    private ItemCartServiceImpl itemCartService;

    @Mock
    private ItemCartRepository itemCartRepository;


    @DisplayName("카트 생성 확인")
    @Test
    void create_Cart() {
        Member member = Member.builder()
                .username("shin")
                .password("qwer1234")
                .build();

        cartService.createCart(member);

        verify(cartRepository, times(1)).save(any());
    }

    @DisplayName("유저의 장바구니 조회 확인")
    @Test
    public void read_Member_Cart_ByUser() {
        String findMemberName = "shin";
        List<ItemCart> mockItemCartList = new ArrayList<>();

        // Mockito.mock(사용할 클래스.class) -> 객체 생성, 의존성 주입
        ItemCart mockItemCart = Mockito.mock(ItemCart.class);
        Cart mockCart = Mockito.mock(Cart.class);
        Member mockMember = Mockito.mock(Member.class);
        Product mockProduct = Mockito.mock(Product.class);

        // .thenReturn() -> 반환하는 값을 지정
        when(mockItemCart.getProduct()).thenReturn(mockProduct);
        when(mockItemCart.getCart()).thenReturn(mockCart);

        when(mockCart.getMember()).thenReturn(mockMember);
        when(mockMember.getEmail()).thenReturn(findMemberName);

        when(mockProduct.getProductName()).thenReturn("강아지 간식");
        when(mockProduct.getPrice()).thenReturn("25_000");
        when(mockItemCart.getStackCount()).thenReturn(3);

        // 모의 객체를 리스트에 추가
        mockItemCartList.add(mockItemCart);

        // itemCartRepository.findAll() 호출 시 모의 객체 리스트 반환하도록 설정
        when(itemCartRepository.findAll()).thenReturn(mockItemCartList);

        // When
        List<ViewCartResponse> result = itemCartService.readMemberCartByUser(findMemberName);

        // Then
        assertThat(1).isEqualTo(result.size());
        ViewCartResponse viewCartResponse = result.get(0);
        assertThat("강아지 간식").isEqualTo(viewCartResponse.getProductName());
        assertThat("25_000").isEqualTo(viewCartResponse.getPrice());
        Assertions.assertThat(3).isEqualTo(viewCartResponse.getCount());
    }

}