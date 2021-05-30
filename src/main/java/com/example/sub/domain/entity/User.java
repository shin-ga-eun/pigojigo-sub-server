package com.example.sub.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true)
    private String email; //아이디

    private String password; //비밀번호
    private String nickname; //닉네임
    private String sex;
    private String celno;
    private String birth;


}