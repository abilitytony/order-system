package com.example.order_system.product.domain;


import com.example.order_system.common.common.domain.BaseTimeEntity;
import com.example.order_system.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    private String category;

    @Column(nullable = false)
    private Long stockQuantity;

    @Column(length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private Member member;

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void decreaseStock(Long quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }

}
