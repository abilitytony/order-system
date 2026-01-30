package com.example.order_system.ordering.dtos;

import com.example.order_system.ordering.domain.OrderingDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderingDetailDto {
    private Long detailId;
    private String productName;
    private Long productCount;

    public static OrderingDetailDto fromEntity(OrderingDetail detail) {
        return OrderingDetailDto.builder()
                .detailId(detail.getId())
                .productName(detail.getProduct().getName())
                .productCount(detail.getProductCount())
                .build();
    }
}
