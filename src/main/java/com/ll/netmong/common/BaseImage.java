package com.ll.netmong.common;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseImage extends BaseEntity {
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "s3_image_url")
    private String s3ImageUrl;

    @ColumnDefault("'Y'")
    private String status;

    public BaseImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BaseImage(String imageUrl, String s3ImageUrl) {
        this.imageUrl = imageUrl;
        this.s3ImageUrl = s3ImageUrl;
    }
}
