package com.swp.VinGiG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.service.EmailService;

@RestController
public class EmailController {
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/email/test")
	public void sendEmail() {
        String to = "namngutn1@gmail.com";
        String subject = "Successful Registration";
        String body = "Hi, Welcome to our service";
        emailService.sendSimpleEmail(to, subject, body);
    } 
}
