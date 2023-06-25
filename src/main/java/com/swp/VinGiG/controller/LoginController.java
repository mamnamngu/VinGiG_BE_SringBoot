package com.swp.VinGiG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.CustomerService;
import com.swp.VinGiG.service.ProviderService;

@RestController
public class LoginController {

	@Autowired
	ProviderService providerService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/login/username/{username}/password/{password}/role/{role}")
	public ResponseEntity<Object> login(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("role") String role){
		if(role.equalsIgnoreCase("customer")){
			Customer customer = customerService.login(username, password);
			return ResponseEntity.ok(customer);
		}else if(role.equalsIgnoreCase("provider")) {
			Provider provider = providerService.login(username, password);
			return ResponseEntity.ok(provider);
		}
		return ResponseEntity.notFound().build();
	}
}
