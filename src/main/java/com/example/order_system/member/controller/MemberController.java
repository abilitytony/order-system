package com.example.order_system.member.controller;

import com.example.order_system.common.common.auth.JwtTokenProvider;
import com.example.order_system.member.domain.Member;
import com.example.order_system.member.dtos.MemberCreateDto;
import com.example.order_system.member.dtos.MemberDetailDto;
import com.example.order_system.member.dtos.MemberListDto;
import com.example.order_system.member.dtos.MemberLoginDto;
import com.example.order_system.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid MemberCreateDto dto) {
        memberService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @PostMapping("/doLogin")
    public String login(@RequestBody MemberLoginDto dto) {
        Member member = memberService.login(dto);

        String token = jwtTokenProvider.createToken(member);
        return token;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberListDto>  findAll() {
        List<MemberListDto> dtoList = memberService.findAll();
        return dtoList;
    }
    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal) {
        System.out.println(principal);
        MemberDetailDto dto = memberService.myinfo();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("detail/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MemberDetailDto findById(@PathVariable Long id) {
        MemberDetailDto dto = memberService.findById(id);
        return dto;

    }


}
