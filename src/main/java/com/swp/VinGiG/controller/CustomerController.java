package com.swp.VinGiG.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> retrieveAllCustomers(){
		return ResponseEntity.ok(customerService.findAll());
    }
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> retrieveCustomer(@PathVariable int id) {
		Customer customer = customerService.findById(id);
		if(customer != null) {
			return ResponseEntity.status(HttpStatus.OK).body(customer);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("customer/username/{username}")
	public ResponseEntity<Customer> retrieveCustomerByUserName(@PathVariable String username) {
		List<Customer> ls = customerService.findByUsername(username);
		if(ls.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ls.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("customer/fullName/{fullName}")
	public ResponseEntity<List<Customer>> retrieveCustomerByFullName(@PathVariable String fullName) {
		List<Customer> ls = customerService.findByFullNameIgnoreCase(fullName);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("customer/createDate/{dateMin}/{dateMax}")
	public ResponseEntity<List<Customer>> retrieveCustomerByCreateDateInterval(@PathVariable Date dateMin, @PathVariable Date dateMax) {
		List<Customer> ls = customerService.findByCreateDateInterval(dateMin, dateMax);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("customer/rating/{lower}/{upper}")
	public ResponseEntity<List<Customer>> retrieveCustomerByUserName(@PathVariable double lower, @PathVariable double upper) {
		List<Customer> ls = customerService.findByRatingInterval(lower, upper);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("building/{id}/customer")
	public ResponseEntity<Customer> createCustomer(@PathVariable int id, @RequestBody Customer customer){
		try {
			Building building = buildingService.findById(id);
			if(building == null)
				return ResponseEntity.notFound().header("message", "No Building found with such ID").build();
			
			customer.setBuilding(building);
			Customer savedCustomer = customerService.add(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new customer").build();
		}
	}
	
	@PutMapping("building/{id}/customer")
	public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer){
		Building building = buildingService.findById(id);
		if(building == null) return ResponseEntity.notFound().header("message", "Building not found. Update failed").build();
		
		if(customerService.findById(customer.getCustomerID()) == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		
		Customer updatedCustomer = customerService.update(customer);
		if(updatedCustomer != null)
			return ResponseEntity.ok(updatedCustomer);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable int id){
		try{
			customerService.delete(id);
			return ResponseEntity.noContent().header("message", "customer deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "customer deletion failed").build();
		}
	}
}
