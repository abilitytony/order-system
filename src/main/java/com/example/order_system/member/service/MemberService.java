package com.example.order_system.member.service;

import com.example.order_system.member.domain.Member;
import com.example.order_system.member.dtos.MemberCreateDto;
import com.example.order_system.member.dtos.MemberDetailDto;
import com.example.order_system.member.dtos.MemberListDto;
import com.example.order_system.member.dtos.MemberLoginDto;
import com.example.order_system.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket1}")
    private String bucket;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public void save(MemberCreateDto dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email 입니다.");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        Member memberDb = memberRepository.save(member);
    }

    public Member login(MemberLoginDto dto) {
        Optional<Member> opt_Member = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!opt_Member.isPresent()) {
            check = false;
        } else {
            if (!passwordEncoder.matches(dto.getPassword(), opt_Member.get().getPassword())) {
                check = false;
            }
        }
        if (!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return opt_Member.get();


    }

    @Transactional(readOnly = true)
    public List<MemberListDto> findAll() {
        return memberRepository.findAll().stream().map(a -> MemberListDto.fromEntity(a)).collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    public MemberDetailDto myinfo() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        System.out.println("myinfo email = " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("entity is not found"));

        return MemberDetailDto.fromEntity(member);
    }


    @Transactional(readOnly = true)
    public MemberDetailDto findById(Long id) {
        Optional<Member> optMember = memberRepository.findById(id);
        Member member = optMember.orElseThrow(()-> new NoSuchElementException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }
}
