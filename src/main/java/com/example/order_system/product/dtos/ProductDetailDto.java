package com.example.order_system.product.dtos;

import com.example.order_system.product.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDto {
    private Long id;
    private String name;
    private String category;
    private Long price;
    private Long stockQuantity;
    private String imagePath;

    public static ProductDetailDto fromEntity(Product product) {
        return ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imagePath(product.getImageUrl()) // 아직 이미지 필드 없으니까 null
                .build();
    }
}
