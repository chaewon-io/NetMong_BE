package com.ll.netmong.domain.image.service;

import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.image.repository.ImageRepository;
import com.ll.netmong.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String productImagePath;

    @Transactional
    public void uploadImage(Product product, MultipartFile image) throws IOException {
        String imageLocation = productImagePath;
        String imageName = image.getOriginalFilename();
        String imagePath = imageLocation + imageName;

        validateCreateDirectory(imageLocation);
        validateTransferImage(imagePath, image);

        Image productImage = Product.createProductImage(imagePath);
        product.addProductImage(productImage);
        imageRepository.save(productImage);
    }

    private void validateCreateDirectory(String imageLocation) throws IOException {
        Files.createDirectories(Path.of(imageLocation));
    }

    private void validateTransferImage(String imagePath, MultipartFile image) throws IOException {
        image.transferTo(Path.of(imagePath));
    }
}
