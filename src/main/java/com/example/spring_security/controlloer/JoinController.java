package com.example.spring_security.controlloer;


import com.example.spring_security.dto.JoinDto;
import com.example.spring_security.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    //회원가입
    @PostMapping("/joinProc")
    public String joinProcess(JoinDto joinDTO){
        //회원가입 로직 작성
        joinService.joinProcess(joinDTO); //회원가입 수행
        return "redirect:/join";
    }
}
