package com.example.order_system.ordering.domain;


import com.example.order_system.common.common.domain.BaseTimeEntity;
import com.example.order_system.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity
public class Ordering extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder.Default
    @OneToMany(mappedBy = "ordering", cascade = CascadeType.ALL)
    private List<OrderingDetail> orderingDetails = new ArrayList<>();
}
