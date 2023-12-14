package com.ll.netmong.domain.product.service;

import com.ll.netmong.domain.product.dto.request.CreateRequest;
import com.ll.netmong.domain.product.dto.request.UpdateRequest;
import com.ll.netmong.domain.product.dto.response.ViewAllResponse;
import com.ll.netmong.domain.product.dto.response.ViewSingleResponse;
import com.ll.netmong.domain.product.util.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProductWithImage(CreateRequest createRequest, MultipartFile images) throws IOException;

    List<ViewAllResponse> viewAllProducts();

    ViewSingleResponse findByProduct(Long productId);

    List<ViewAllResponse> findByProductCategory(Category category);

    List<ViewAllResponse> findByProductName(String productName);

    Page<ViewAllResponse> readPageByProduct(Pageable pageable);

    void updateProduct(Long productId, UpdateRequest updateRequest);

    void softDeleteProduct(Long productId);
}
