package com.example.order_system.member.domain;

import com.example.order_system.common.common.domain.BaseTimeEntity;
import com.example.order_system.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity

public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> productList = new ArrayList<>();
}
