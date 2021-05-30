package com.example.sub.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInDto {

    private String email;
    private String password;
    private String nickname;
}
