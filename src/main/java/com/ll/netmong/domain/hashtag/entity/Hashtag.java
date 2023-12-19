package com.ll.netmong.domain.hashtag.entity;

import com.ll.netmong.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hashtag extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    public Hashtag(String name) {
        this.name = name;
    }
}
