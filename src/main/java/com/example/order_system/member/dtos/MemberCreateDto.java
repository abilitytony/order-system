package com.example.order_system.member.dtos;


import com.example.order_system.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberCreateDto {
    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;
    @NotBlank(message = "email은 필수 값입니다.")
    private String email;
    @NotBlank(message = "password는 필수 값입니다.")
    @Size(min = 8, message = "패스워드의 길이가 너무 짧습니다.")
    private String password;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(encodedPassword)
                .build();
    }
}
