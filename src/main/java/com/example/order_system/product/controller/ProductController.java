package com.example.order_system.product.controller;

import com.example.order_system.member.dtos.MemberCreateDto;
import com.example.order_system.product.dtos.ProductCreateDto;
import com.example.order_system.product.dtos.ProductDetailDto;
import com.example.order_system.product.dtos.ProductListDto;
import com.example.order_system.product.dtos.ProductSearchDto;
import com.example.order_system.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @ModelAttribute ProductCreateDto dto,
            @RequestPart(required = false) MultipartFile productImage
    ) {
        productService.save(dto, productImage);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        ProductDetailDto dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/list")
    public Page<ProductListDto> postListDtoList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @ModelAttribute ProductSearchDto searchDto) {
        return productService.findAll(pageable, searchDto);
    }
}
