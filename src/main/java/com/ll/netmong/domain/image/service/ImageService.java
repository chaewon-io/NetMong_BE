package com.ll.netmong.domain.image.service;

import com.ll.netmong.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    <T> Optional<Image> uploadImage(T entity, MultipartFile image) throws IOException;
}
