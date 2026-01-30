package com.example.order_system.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductSearchDto {
    private Long id;
    private String name;
    private String category;
    private Long price;
    private Long stockQuantity;
    private String imagePath;
}
