package com.example.order_system.ordering.controller;

import com.example.order_system.ordering.dtos.OrderingListDto;
import com.example.order_system.ordering.dtos.OrderingProductDto;
import com.example.order_system.ordering.service.OrderingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordering")
public class OrderingController {

    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> order(@RequestBody List<OrderingProductDto> orderProducts) {
        orderingService.order(orderProducts);
        return ResponseEntity.ok("주문 완료");
    }
    @GetMapping("/list")
    public ResponseEntity<List<OrderingListDto>> list() {
        return ResponseEntity.ok(orderingService.findAll());
    }
    @GetMapping("/myorders")
    public List<OrderingListDto> myOrders() {
        return orderingService.findMyOrders();
    }

}
