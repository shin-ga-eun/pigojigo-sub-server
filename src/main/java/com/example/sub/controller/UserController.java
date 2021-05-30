package com.example.sub.controller;

import com.example.sub.domain.dto.user.SignInDto;
import com.example.sub.domain.dto.user.SignUpDto;
import com.example.sub.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //로그인
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public SignInDto signIn(@RequestBody SignInDto loginDto) {
        return userService.signIn(loginDto);

    }

    //회원가입
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public void signUp(@RequestBody SignUpDto signUpDto) throws Exception {
        userService.signUp(signUpDto);
    }

    //회원정보수정
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public void update(@RequestBody SignUpDto updateDto) {
        userService.update(updateDto);
    }
}
