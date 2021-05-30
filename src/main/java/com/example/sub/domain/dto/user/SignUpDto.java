package com.example.sub.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDto {

    private String email; //아이디
    private String password; //비밀번호
    private String nickname; //닉네임
    private String sex;
    private String celno;
    private String birth;
}
