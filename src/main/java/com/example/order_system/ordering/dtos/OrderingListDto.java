package com.example.order_system.ordering.dtos;

import com.example.order_system.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderingListDto {
    private Long id;
    private String memberEmail;
    private String orderStatus;
    private List<OrderingDetailDto> orderingDetails;

    public static OrderingListDto fromEntity(Ordering ordering) {
        return OrderingListDto.builder()
                .id(ordering.getId())
                .memberEmail(ordering.getMember().getEmail())
                .orderStatus(ordering.getOrderStatus().name())
                .orderingDetails(
                        ordering.getOrderingDetails().stream()
                                .map(OrderingDetailDto::fromEntity)
                                .toList()
                )
                .build();
    }
}
