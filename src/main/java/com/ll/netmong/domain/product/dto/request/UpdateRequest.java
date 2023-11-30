package com.ll.netmong.domain.product.dto.request;

import com.ll.netmong.domain.product.util.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@Setter
public class UpdateRequest {
    @NotBlank(message = "상품 이름은 필수 입니다.")
    private String productName;
    @NotBlank(message = "상품 가격은 필수 입니다.")
    private String price;
    @NotBlank(message = "상품 설명은 필수 입니다.")
    private String content;
    @NotNull(message = "상품 카테고리는 필수 입니다.")
    private Category category;
}
