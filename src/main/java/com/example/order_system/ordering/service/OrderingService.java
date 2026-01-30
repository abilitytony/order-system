package com.example.order_system.ordering.service;

import com.example.order_system.member.domain.Member;
import com.example.order_system.member.repository.MemberRepository;
import com.example.order_system.ordering.domain.OrderStatus;
import com.example.order_system.ordering.domain.Ordering;
import com.example.order_system.ordering.domain.OrderingDetail;
import com.example.order_system.ordering.dtos.OrderingDetailDto;
import com.example.order_system.ordering.dtos.OrderingListDto;
import com.example.order_system.ordering.dtos.OrderingProductDto;
import com.example.order_system.ordering.repository.OrderingRepository;
import com.example.order_system.product.domain.Product;
import com.example.order_system.product.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public OrderingService(OrderingRepository orderingRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.orderingRepository = orderingRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public void order(List<OrderingProductDto> orderProducts) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Ordering ordering = Ordering.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .build();

        for (OrderingProductDto dto : orderProducts) {
            Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            if (product.getStockQuantity() < dto.getProductCount()) {
                throw new IllegalArgumentException("재고 부족");
            }

            product.decreaseStock(dto.getProductCount());

            OrderingDetail detail = OrderingDetail.builder()
                    .ordering(ordering)
                    .product(product)
                    .productCount(dto.getProductCount())
                    .build();

            ordering.getOrderingDetails().add(detail);
        }
        orderingRepository.save(ordering);
    }
    @Transactional(readOnly = true)
    public List<OrderingListDto> findAll() {
        List<Ordering> orderingList = orderingRepository.findAllFetch();

        return orderingList.stream()
                .map(OrderingListDto::fromEntity)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<OrderingListDto> findMyOrders() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("회원 없음"));

        List<Ordering> orderings = orderingRepository.findByMember(member);

        return orderings.stream()
                .map(OrderingListDto::fromEntity)
                .toList();
    }

}
