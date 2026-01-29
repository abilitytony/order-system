package com.example.order_system.common.common.init;

import com.example.order_system.member.domain.Member;
import com.example.order_system.member.domain.Role;
import com.example.order_system.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


//CommandLineRunner를 구현함으로서 아래 run메서드가 스프링 빈으로 등록되는 시점에 자동 실행
@Component
@Transactional
public class InitialDataLoad implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public InitialDataLoad(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByEmail("admin@naver.com").isPresent()) {
            return;
        }
        memberRepository.save(Member.builder()
                .name("admin")
                .email("admin@naver.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("12341234"))
                .build());

    }
}
