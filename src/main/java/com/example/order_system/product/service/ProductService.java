package com.example.order_system.product.service;

import com.example.order_system.member.domain.Member;
import com.example.order_system.member.dtos.MemberCreateDto;
import com.example.order_system.member.repository.MemberRepository;
import com.example.order_system.product.domain.Product;
import com.example.order_system.product.dtos.ProductCreateDto;
import com.example.order_system.product.dtos.ProductDetailDto;
import com.example.order_system.product.dtos.ProductListDto;
import com.example.order_system.product.dtos.ProductSearchDto;
import com.example.order_system.product.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket1}")
    private String bucket;

    public ProductService(ProductRepository productRepository,
                          MemberRepository memberRepository,
                          S3Client s3Client) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.s3Client = s3Client;
    }

    public void save(ProductCreateDto dto, MultipartFile productImage) {

        // 로그인한 사용자
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Product product = dto.toEntity(member);
        productRepository.save(product);

        if (productImage != null && !productImage.isEmpty()) {
            uploadImage(product, productImage);
        }
    }

    @Transactional(readOnly = true)
    public ProductDetailDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        return ProductDetailDto.fromEntity(product);
    }


    private void uploadImage(Product product, MultipartFile image) {
        String fileName = "product-" + product.getId() + "-" + image.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(image.getContentType())
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(image.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String imageUrl = s3Client.utilities()
                .getUrl(a -> a.bucket(bucket).key(fileName))
                .toExternalForm();

        product.updateImageUrl(imageUrl);
    }

    @Transactional(readOnly = true)
    public Page<ProductListDto> findAll(Pageable pageable, ProductSearchDto searchDto) {
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (searchDto.getId() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("id"), "%" + searchDto.getId() + "%"));
                }
                if (searchDto.getName() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("name"), searchDto.getName()));
                }
                if (searchDto.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                }
                if (searchDto.getPrice() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("price"), "%" + searchDto.getPrice() + "%"));
                }
                if (searchDto.getStockQuantity() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("stockQuantity"), searchDto.getStockQuantity()));
                }
                if (searchDto.getImagePath() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("imagePath"), searchDto.getImagePath()));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateArr.length; i++) {
                    predicateArr[i] = predicateList.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Product> postList = productRepository.findAll(specification, pageable);
        return postList.map(p->ProductListDto.fromEntity(p));
    }
}