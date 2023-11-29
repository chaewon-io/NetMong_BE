package com.ll.netmong.domain.product.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.product.dto.request.CreateRequest;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.dto.response.ViewAllResponse;
import com.ll.netmong.domain.product.dto.response.ViewSingleResponse;
import com.ll.netmong.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private static final String FIND_SUCCESS_PRODUCT = "상품 조회가 되었습니다.";
    private static final String POST_SUCCESS_PRODUCT = "상품이 등록 되었습니다.";
    private static final String MODIFY_SUCCESS_PRODUCT = "상품이 수정 되었습니다.";
    private static final String DELETE_SUCCESS_PRODUCT = "상품이 삭제 되었습니다.";
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RsData findByAllProducts() {
        List<ViewAllResponse> viewAllProducts = productService.viewAllProducts();
        return RsData.of("S-1", FIND_SUCCESS_PRODUCT, viewAllProducts);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RsData findByProduct(@PathVariable(name = "id") Long productId) {
        ViewSingleResponse viewSingleProduct = productService.findByProduct(productId);
        return RsData.of("S-1", FIND_SUCCESS_PRODUCT, viewSingleProduct);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RsData createProduct(@ModelAttribute CreateRequest createRequest,
                                @ModelAttribute("images") MultipartFile images) throws IOException {
        productService.createProductWithImage(createRequest, images);
        return RsData.of("S-1", POST_SUCCESS_PRODUCT, "create");
    }

    @PutMapping("/{id}")
    public RsData updateProduct(@PathVariable(name = "id") Long productId,
                                @ModelAttribute UpdateRequest updateRequest) {
        productService.updateProduct(productId, updateRequest);
        return RsData.of(MODIFY_SUCCESS_PRODUCT, "modify");
    }

    @DeleteMapping("/{id}")
    public RsData softDeleteProduct(@PathVariable(name = "id") Long productId) {
        productService.softDeleteProduct(productId);
        return RsData.of(DELETE_SUCCESS_PRODUCT, "delete");
    }
}
