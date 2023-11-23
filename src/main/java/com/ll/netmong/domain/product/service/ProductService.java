package com.ll.netmong.domain.product.service;

import com.ll.netmong.domain.product.dto.request.CreateRequest;
import com.ll.netmong.domain.product.dto.response.ViewAllResponse;
import com.ll.netmong.domain.product.dto.response.ViewSingleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void createProductWithImage(CreateRequest createRequest, MultipartFile[] image);

    List<ViewAllResponse> viewAllProducts();

    ViewSingleResponse findByProduct(Long productId);
}
