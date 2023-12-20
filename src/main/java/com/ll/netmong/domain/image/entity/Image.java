package com.ll.netmong.domain.image.entity;

import com.ll.netmong.common.BaseImage;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE image SET status = 'N' where id = ?")
public class Image extends BaseImage {

    public Image(String imageUrl) {
        super(imageUrl);
    }

    public Image(String imageUrl, String s3ImageUrl) {
        super(imageUrl, s3ImageUrl);
    }
}
