package com.example.spring_security.service;

import com.example.spring_security.dto.JoinDto;
import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;

    //회원가입 시 사용자 비밀번호 암호화
    private final BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(JoinDto joinDTO) {

        //회원 중복 검증
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser){
            return;
        }

        //저장할 사용자 생성
        UserEntity user = UserEntity.builder()
                .username(joinDTO.getUsername())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }
}
