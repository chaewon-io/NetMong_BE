package com.ll.netmong.domain.image.entity;

import com.ll.netmong.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE image SET status = 'N' where id = ?")
public class Image extends BaseEntity {

    @Column(name = "image_url")
    private String imageUrl;

    @ColumnDefault("'Y'")
    private String status;

    public Image(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
