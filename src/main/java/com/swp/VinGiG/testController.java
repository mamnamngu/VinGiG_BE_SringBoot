package com.swp.VinGiG;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
	@GetMapping("/test")
	public User test() {
		return new User(1, "Phi", new Date());
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
}
