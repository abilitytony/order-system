package com.example.order_system.product.dtos;


import com.example.order_system.member.domain.Member;
import com.example.order_system.product.domain.Product;
import lombok.Data;

@Data
public class ProductCreateDto {
    private String name;
    private Long price;
    private String category;
    private Long stockQuantity;

    public Product toEntity(Member member) {
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .category(this.category)
                .stockQuantity(this.stockQuantity)
                .member(member)
                .build();
    }
}
