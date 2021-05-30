package com.example.sub.service.user;

import com.example.sub.domain.dto.user.SignInDto;
import com.example.sub.domain.dto.user.SignUpDto;
import com.example.sub.domain.entity.User;
import com.example.sub.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignInDto signIn(SignInDto signInDto) {

        SignInDto dto = new SignInDto();

        User user = userRepository.findByEmail(signInDto.getEmail());

        if (user != null) {
            dto.setEmail(user.getEmail());
            dto.setNickname(user.getNickname());
        } else {
            //exception
            dto.setEmail("error");
        }
        return dto;
    }

    public void signUp (SignUpDto signUpDto){

        User user = new User();

        user.setEmail(signUpDto.getEmail());
        user.setPassword(signUpDto.getPassword());
        user.setNickname(signUpDto.getNickname());
        user.setSex(signUpDto.getSex());
        user.setCelno(signUpDto.getCelno());
        user.setBirth(signUpDto.getBirth());

        userRepository.save(user);
    }

    public void update (SignUpDto updateDto) {

        User user = userRepository.findByEmail(updateDto.getEmail());

        user.setPassword(updateDto.getPassword());
        user.setNickname(updateDto.getNickname());
        user.setSex(updateDto.getSex());
        user.setCelno(updateDto.getCelno());
        user.setBirth(updateDto.getBirth());

        userRepository.save(user);
    }
}