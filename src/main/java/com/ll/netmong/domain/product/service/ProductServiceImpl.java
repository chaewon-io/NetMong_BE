package com.ll.netmong.domain.product.service;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.image.repository.ImageRepository;
import com.ll.netmong.domain.product.dto.request.CreateRequest;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.dto.response.ViewAllResponse;
import com.ll.netmong.domain.product.dto.response.ViewSingleResponse;
import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.repository.ProductRepository;
import com.ll.netmong.domain.product.util.ProductErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final List<ViewAllResponse> viewAllResponse = new ArrayList<>();
    @Value("${spring.servlet.multipart.location}")
    private String productImagePath;

    @Override
    @Transactional
    public void createProductWithImage(CreateRequest createRequest, MultipartFile[] images) throws IOException {
        if (!isImageExists(images)) {
            initProduct(createRequest);
        }
        if (isImageExists(images)) {
            String imageLocation = productImagePath;
            Product product = initProduct(createRequest);
            for (MultipartFile image : images) {
                String imageName = image.getOriginalFilename();
                String imagePath = imageLocation + imageName;

                validateCreateDirectory(imageLocation);
                validateTransferImage(imagePath, image);

                Image productImage = Product.createProductImage(imagePath);
                product.addProductImage(productImage);
                imageRepository.save(productImage);
            }
        }
    }

    @Override
    public List<ViewAllResponse> viewAllProducts() {
        return getViewAllResponse();
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
                .category(createRequest.getCategory())
                .build());
    }

    private boolean isImageExists(MultipartFile[] image) {
        return !Objects.isNull(image);
    }

    private List<ViewAllResponse> getViewAllResponse() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (!Objects.isNull(product)) {
                viewAllResponse.add(new ViewAllResponse(product));
            }
        }
        return viewAllResponse;
    }

    private Product validateExistProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductException("상품이 존재하지 않습니다.", ProductErrorCode.NOT_EXIST_PRODUCT));
    }

    private void validateCreateDirectory(String imageLocation) throws IOException {
        Files.createDirectories(Path.of(imageLocation));
    }

    private void validateTransferImage(String imagePath, MultipartFile image) throws IOException {
        image.transferTo(Path.of(imagePath));
    }
}
