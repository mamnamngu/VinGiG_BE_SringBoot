package com.swp.VinGiG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableWebMvc
public class VinGiGApplication {

	public static void main(String[] args) {
		SpringApplication.run(VinGiGApplication.class, args);
	}
}
