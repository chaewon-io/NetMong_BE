package com.ll.netmong.domain.post.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostImg {
    @Id
    private long id;
    private String originalImgName;
    private String uploadImgName;
    private String imgPath;
    private String imgUrl;
}
