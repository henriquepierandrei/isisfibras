package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Service.MessageSenderService.EmailService;
import com.pierandrei.isisfibras.Service.MessageSenderService.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final TwilioService service;


    @GetMapping("/send")
    public String sendTest(){
        this.service.sendMessage("+5532999701559", "Olá Henrique!");
        return "Enviado";
    }
}
