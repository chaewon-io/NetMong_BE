package com.ll.netmong.domain.product.service;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.image.service.ImageService;
import com.ll.netmong.domain.product.dto.request.CreateRequest;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.dto.response.ViewAllResponse;
import com.ll.netmong.domain.product.dto.response.ViewSingleResponse;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.repository.ProductRepository;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public void createProductWithImage(CreateRequest createRequest, MultipartFile images) throws IOException {
        if (!isImageExists(images)) {
            initProduct(createRequest);
        }
        if (isImageExists(images)) {
            Product product = initProduct(createRequest);
            imageService.uploadImage(product, images);
        }
    }

    @Override
    public List<ViewAllResponse> viewAllProducts() {
        return getViewAllResponse();
    }

    @Override
    public Page<ViewAllResponse> readPageByProduct(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(ViewAllResponse::pageByProduct);
    }

    @Override
    public ViewSingleResponse findByProduct(Long productId) {
        return new ViewSingleResponse(validateExistProduct(productId));
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, UpdateRequest updateRequest) {
        Product findProduct = validateExistProduct(productId);
        findProduct.modifyProduct(updateRequest);
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long productId) {
        validateExistProduct(productId);
        productRepository.deleteById(productId);
    }

    private Product initProduct(CreateRequest createRequest) {
        return productRepository.save(Product.builder()
                .productName(createRequest.getProductName())
                .price(createRequest.getPrice())
                .content(createRequest.getContent())
                .count(createRequest.getCount())
                .category(createRequest.getCategory())
                .build());
    }

    private boolean isImageExists(MultipartFile image) {
        return !Objects.isNull(image);
    }

    private List<ViewAllResponse> getViewAllResponse() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(product -> product != null)
                .map(ViewAllResponse::new)
                .collect(Collectors.toList());
    }

    private Product validateExistProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductException("상품이 존재하지 않습니다.", ProductErrorCode.NOT_EXIST_PRODUCT));
    }
}
