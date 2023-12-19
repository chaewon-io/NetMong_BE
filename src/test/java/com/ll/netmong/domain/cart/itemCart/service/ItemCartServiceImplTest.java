package com.ll.netmong.domain.cart.itemCart.service;

import com.ll.netmong.domain.cart.dto.request.ProductCountRequest;
import com.ll.netmong.domain.cart.entity.Cart;
import com.ll.netmong.domain.cart.itemCart.entity.ItemCart;
import com.ll.netmong.domain.cart.itemCart.repository.ItemCartRepository;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.member.entity.Member;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemCartServiceImplTest {

    @InjectMocks
    private ItemCartServiceImpl itemCartService;

    @Mock
    private ItemCartRepository itemCartRepository;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private TransactionTemplate transactionTemplate;

    private String findMemberName;
    private Member mockMember;
    private Cart mockCart;
    private Product mockProduct;
    private ItemCart mockItemCart;
    private Image image;

    @BeforeEach
    public void setUp() {
        findMemberName = "shin";
        mockMember = Mockito.mock(Member.class);
        mockCart = Mockito.mock(Cart.class);
        mockProduct = Mockito.mock(Product.class);
        mockItemCart = Mockito.mock(ItemCart.class);
        image = Mockito.mock(Image.class);

        when(mockCart.getMember()).thenReturn(mockMember);
        when(mockMember.getUsername()).thenReturn(findMemberName);

        when(mockProduct.getId()).thenReturn(1L);
        when(mockProduct.getProductName()).thenReturn("강아지 간식");
        when(mockProduct.getContent()).thenReturn("1년 남은 간식 입니다.");
        when(mockProduct.getCount()).thenReturn(10);
        when(mockProduct.getImage()).thenReturn(image);

        when(itemCartRepository.findByCartIdAndProductId(anyLong(), anyLong()))
                .thenReturn(mockItemCart);
        when(mockItemCart.getProduct()).thenReturn(mockProduct);
        when(mockItemCart.getCart()).thenReturn(mockCart);
    }

    @DisplayName("저장된 아이템카트 가져오기")
    @Test
    public void 저장된_아이템카트_가져오기() {
        ItemCart itemCart = itemCartService.getItemCart(mockCart, mockProduct.getId());
        assertThat(itemCart.getId()).isEqualTo(mockItemCart.getId());
    }

    @DisplayName("저장된 아이템 카트의 상품 이름 확인")
    @Test
    public void 저장된_아이템카트의_상품_이름_확인() {
        ItemCart itemCart = itemCartService.getItemCart(mockCart, mockProduct.getId());
        assertThat(itemCart.getProduct().getProductName()).isEqualTo("강아지 간식");
    }

    @DisplayName("저장된 아이템 카트의 상품 내용 확인")
    @Test
    public void 저장된_아이템_카트의_상품_내용_확인() {
        ItemCart itemCart = itemCartService.getItemCart(mockCart, mockProduct.getId());
        assertThat(itemCart.getProduct().getContent()).isEqualTo("1년 남은 간식 입니다.");
    }

    @DisplayName("새로운 상품을 장바구니에 담을 경우 확인")
    @Test
    public void addToCartForNewProductTest() {
        ProductCountRequest productCountRequest = new ProductCountRequest();
        productCountRequest.setCount(1);

        when(productService.getProduct(mockProduct.getId())).thenReturn(mockProduct);

        ReflectionTestUtils.setField(itemCartService, "transactionTemplate", transactionTemplate);

        // When
        itemCartService.addToCartForNewProduct(mockCart, mockProduct.getId(), productCountRequest);

        // Then
        verify(itemCartRepository, times(1)).save(any(ItemCart.class));
    }
}
