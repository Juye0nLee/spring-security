package com.example.spring_security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain 커스텀 빈 스프링 빈으로 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // Spring Security 버전에 따라 구현 방식이 다름
        http
                .authorizeHttpRequests(auth -> auth // 람다 식으로 작성 해야함
                        // 메인페이지와 로그인 페이지로의 접근은 모두 허용(인증 필요x)
                        .requestMatchers("/", "/login","/join","joinProc").permitAll()
                        // admin 페이지로의 접근은 ADMIN 권한을 가진 경우에만 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // my 경로 이후의 모든 경로에 대해서 ADMIN, 혹은 USER권한 보유시 접근 가능
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // 위에서 설정하지 않은 모든 요청에 대해서는 인증(authenticated)된 사용자에 한해
                        .anyRequest().authenticated()
                )
                    // form 로그인 방식을 사용할 것이다.
                    .formLogin(auth -> auth
                            .loginPage("/login") // 로그인은 /login에서 수행한다.
                            .loginProcessingUrl("/loginProc")
                            .permitAll()
                    ) // 로그인 프로세싱은 해당 경로로 수행한다.
                    // 폼 로그인 방식을 사용하게 되면 csrf 설정이 디폴트로 동작하게 된다.
                    // csrf가 동작하면 POST 요청을 보낼 때 csrf 토큰도 함께 보내야 요청이 수행된다.
                    // 우리의 개발환경에서는 토큰을 보내주도록 설정하지 않았기 때문에 임시로 비활성화 한다.
                    .csrf(auth -> auth.disable());
        return http.build();
    }
}
