package com.ll.netmong.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    <T> void uploadImage(T entity, MultipartFile image) throws IOException;
}
