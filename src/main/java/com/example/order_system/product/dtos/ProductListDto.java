package com.example.order_system.product.dtos;

import com.example.order_system.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductListDto {
    private Long id;
    private String name;
    private Long price;
    private String category;
    private Long stockQuantity;

    public static ProductListDto fromEntity(Product product) {
        return ProductListDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
