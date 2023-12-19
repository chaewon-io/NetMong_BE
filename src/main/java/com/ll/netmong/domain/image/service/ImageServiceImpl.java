package com.ll.netmong.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ll.netmong.domain.image.entity.Image;
import com.ll.netmong.domain.image.repository.ImageRepository;
import com.ll.netmong.domain.post.entity.Post;
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
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String imagePath;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public <T> void uploadImage(T requestType, MultipartFile image) throws IOException {
        String imageLocation = imagePath;
        String imageName = image.getOriginalFilename();
        String imagePath = imageLocation + imageName;

        validateCreateDirectory(imageLocation);
        validateTransferImage(imagePath, image);

        String fileName = requestType.getClass().getSimpleName() + "/" + image.getOriginalFilename();

        if (requestType instanceof Product product) {
            Image productImage = Product.createProductImage(imagePath);
            product.addProductImage(productImage);
            imageRepository.save(productImage);
            createS3Bucket(fileName, image);
        }

        if (requestType instanceof Post post) {
            Image postImage = Post.createProductImage(imagePath);
            post.addPostImage(postImage);
            imageRepository.save(postImage);
            createS3Bucket(fileName, image);
        }
    }

    private void createS3Bucket(String fileName, MultipartFile image) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        amazonS3Client.putObject(bucket, fileName, image.getInputStream(), metadata);
    }

    private void validateCreateDirectory(String imageLocation) throws IOException {
        Files.createDirectories(Path.of(imageLocation));
    }

    private void validateTransferImage(String imagePath, MultipartFile image) throws IOException {
        image.transferTo(Path.of(imagePath));
    }
}
