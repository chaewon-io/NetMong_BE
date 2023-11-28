package com.ll.netmong.domain.image.service;

import com.ll.netmong.domain.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void uploadImage(Product product, MultipartFile[] images) throws IOException;
}
