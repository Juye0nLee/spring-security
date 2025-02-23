package com.example.spring_security.repository;

import com.example.spring_security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    //회원 이름 중복 방지용
    boolean existsByUsername(String username); //해당하는 회원이 존재하는지 확인

    //UserDetailService에서 사용하기 위한 유저 검증 함수
    Optional<UserEntity> findByUsername(String username);
}
