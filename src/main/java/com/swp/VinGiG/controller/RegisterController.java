package com.swp.VinGiG.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.Badge;
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BadgeService;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.CustomerService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.Account;

@RestController
public class RegisterController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BadgeService badgeService;
	
	
	@PostMapping("/register/role/{role}")
	public ResponseEntity<Object> register(@RequestBody Account account, @PathVariable String role) {
		if(role.equalsIgnoreCase("customer")) {
			Customer customer = customerService.checkConflict(account.getUsername());
			if(customer != null) return null;
		}else if(role.equalsIgnoreCase("provider")) {
			Provider provider = providerService.checkConflict(account.getUsername());
			if(provider != null) return null;
		}else return null;
		
		Building building = buildingService.findById(account.getBuildingID());
		if(role.equalsIgnoreCase("customer")) {
			Customer newCustomer = new Customer();
			newCustomer.setUsername(account.getUsername());
			newCustomer.setRole(role);
			newCustomer.setRating(0);
			newCustomer.setPhone(account.getPhone());
			newCustomer.setPassword(account.getPassword());
			newCustomer.setGender(account.isGender());
			newCustomer.setFullName(account.getFullName());
			newCustomer.setEmail(account.getEmail());
			newCustomer.setDob(new Date());
			newCustomer.setCreateDate(Constants.currentDate());
			newCustomer.setBuilding(building);
			newCustomer.setAvatar(Constants.DEFAULT_AVATAR);
			newCustomer.setAddress(account.getApartment());
			newCustomer.setActive(true);
			return ResponseEntity.ok(newCustomer);
		}else {
			Provider newProvider = new Provider();
			newProvider.setUsername(account.getUsername());
			newProvider.setRole(role);
			newProvider.setRating(0);
			newProvider.setPhone(account.getPhone());
			newProvider.setPassword(account.getPassword());
			newProvider.setGender(account.isGender());
			newProvider.setFullName(account.getFullName());
			newProvider.setEmail(account.getEmail());
			newProvider.setCreateDate(Constants.currentDate());
			newProvider.setBuilding(building);
			newProvider.setAvatar(Constants.DEFAULT_AVATAR);
			newProvider.setAddress(account.getApartment());
			newProvider.setActive(true);
			Badge badge = badgeService.findById(account.getBadgeID());
			newProvider.setBadge(badge);
			return ResponseEntity.ok(newProvider);
		}
	}
}
