package com.swp.VinGiG.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin
@EnableWebMvc
public class CustomWebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Specify the allowed origin(s)
                .allowedMethods("*")  // Specify the allowed HTTP methods
                .allowedHeaders("*")  // Specify the allowed request headers
                .maxAge(3600);  // Specify the maximum age of the CORS preflight request
    }

}
