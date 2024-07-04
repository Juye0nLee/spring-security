Spring security 👊🏻
=======================
- 웹 사이트를 이용할 때, 회원가입을 한 사용자에게만 접근을 허용하게 하거나, 관리자 페이지로의 접근은 관리자 권한을 가진 사용자만 허용하고 싶은 경우가 있다.
- 이때 사용하는 것이, **스프링 시큐리티 프레임 워크**
- 사용자의 **인증**과 **인가**를 통해 허가 받은 사용자만 접근을 허용할 수 있게 한다.  


#### 스프링의 기본적인 구조

![img.png](img.png)
- 사용자의 요청이 발생하면 서블릿 컨테이너의 여러 필터들을 통과한 후 Dispatcher Servlet에 의해 적절한  Controller로 배치
- 이후 부터는 우리가 알고 있는 흐름대로 Controller -> Service -> Repository 등의 계층을 거쳐 요청이 진행됨.

#### Spring Security 프레임워크 사용시
- FilterChain의 Delegating Filter Proxy에 의해 사용자의 요청을 가로챔
- 그리고 Security Filter Chain에 의해 사용자 인증과 인가를 거쳐 접근 권한이 있는 사용자에게 접근을 허용하게 됨
- ![img_1.png](img_1.png)
- 클라이언트가 요청을 보내게 되면 Spring Security 프레임워크에 의해 Security Filter가 자동으로 생성
- 이 필터를 거쳐 사용자 인증, 인가 작업이 완료
- 그 후 스프링 프레임워크는 사용자 정보를 Session에 저장함 

#### SecurityConfig 
- 별도의 설정을 하지 않으면 스프링 시큐리티 프레임워크는 기본 시큐리티 필터를 생성하여 모든 페이지에 대해 인증을 요구하며, 인증된 사용자(로그인에 성공한 사용자)에 한해 접근을 허용하게 된다. 
  - 시큐리티 필터를 커스텀하여 특정 페이지로의 접근만 허용하는 것이 아닌, 추가로 특정한 권한을 갖춘 사용자에 한해서만 접근을 허용하도록 인증, 인가 절차를 설정한다. 
    - SecurityConfig.class
        ```
          package com.example.spring_security.config;
          import org.springframework.context.annotation.Bean;
          import org.springframework.context.annotation.Configuration;
          import org.springframework.security.config.annotation.web.builders.HttpSecurity;
          import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
          import org.springframework.security.web.SecurityFilterChain;
          @Configuration
          @EnableWebSecurity
          public class SecurityConfig {
          // SecurityFilterChain 커스텀 빈 스프링 빈으로 등록
          @Bean
          public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
          // Spring Security 버전에 따라 구현 방식이 다름
          http
          .authorizeHttpRequests(auth -> auth // 람다 식으로 작성 해야함
          // 메인페이지와 로그인 페이지로의 접근은 모두 허용(인증 필요x)
          .requestMatchers("/", "/login").permitAll()
          // admin 페이지로의 접근은 ADMIN 권한을 가진 경우에만 허용
          .requestMatchers("/admin").hasRole("ADMIN")
          // my 경로 이후의 모든 경로에 대해서 ADMIN, 혹은 USER권한 보유시 접근 가능
          .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
          // 위에서 설정하지 않은 모든 요청에 대해서는 인증(authenticated)된 사용자에 한해
          .anyRequest().authenticated()
          );
          return http.build();
          }
          }
        ``` 
    - requestMatchers() : 사용자의 요청에 따른 인증,인가 조건 설정
    - permitAll() : 모든 사용자에 대해 접근을 하용함, 위 코드에선 루트 디렉토리와 로그인 페이지는 인증을 수행하지 않은 사용자도 접근을 허용하도록 함.
    - hasRole(ROLE) : 인증 절차를 마친 사용자에 대해 ROLE 권한을 가진 사용자의 접근을 허용.
    - hasAnyRole(ROLE1, ROLE2 ..) : ROLE1, ROLE2 .. 중 하나의 권한이라도 가진 사용자의 접근을 허용한다. 
    - anyRequest() : requestMatchers()를 통해 설정하지 않는 모든 요청 경로에 대해 설정한다.
    - authenticated() : 인증된 사용자라면 모두 허용한다. 


#### 암호화 메서드 
    - 스프링 시큐리티는 회원가입 된 사용자의 비밀번호가 암호화 되어 있다는 것을 전제로 함.
    - 사용자 정보를 DB에 저장하기 전에 비밀번호를 암호화 한 뒤 저장해두어야 나중에 인증 작업을 수행할 수 있음. 
    - 암호화 방법에는 여러가지 방법이 있는데, 스프링 시큐리티는 암호화를 위해 해 BCrypt Password Encoder를 권장함. 