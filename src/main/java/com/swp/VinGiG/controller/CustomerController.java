package com.swp.VinGiG.controller;

import java.util.ArrayList;
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
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.CustomerObject;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping("/customers")
	public ResponseEntity<List<CustomerObject>> retrieveAllCustomers(){
		List<Customer> ls = customerService.findAll();
		List<CustomerObject> list = customerService.display(ls);
		return ResponseEntity.ok(list);
    }

	@GetMapping("/customers/deleted")
	public ResponseEntity<List<CustomerObject>> retrieveDeletedCustomers(){
		List<Customer> ls = customerService.findDeletedCustomers();
		List<CustomerObject> list = customerService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<CustomerObject> retrieveCustomer(@PathVariable int id) {
		Customer customer = customerService.findById(id);
		if(customer != null) {
			List<Customer> ls = new ArrayList<>();
			ls.add(customer);
			List<CustomerObject> list = customerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/customer/username/{username}")
	public ResponseEntity<CustomerObject> retrieveCustomerByUserName(@PathVariable String username) {
		List<Customer> ls = customerService.findByUsername(username);
		if(ls.size() > 0) {
			List<CustomerObject> list = customerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/customer/fullName/{fullName}")
	public ResponseEntity<List<CustomerObject>> retrieveCustomerByFullName(@PathVariable String fullName) {
		List<Customer> ls = customerService.findByFullNameIgnoreCase(fullName);
		if(ls.size() > 0) {
			List<CustomerObject> list = customerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/customer/createDate/{dateMin}/{dateMax}")
	public ResponseEntity<List<CustomerObject>> retrieveCustomerByCreateDateInterval(@PathVariable String dateMinStr, @PathVariable String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Customer> ls = customerService.findByCreateDateInterval(dateMin, dateMax);
		if(ls.size() > 0) {
			List<CustomerObject> list = customerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/customer/rating/{lower}/{upper}")
	public ResponseEntity<List<CustomerObject>> retrieveCustomerByUserName(@PathVariable long lower, @PathVariable long upper) {
		List<Customer> ls = customerService.findByRatingInterval(lower, upper);
		if(ls.size() > 0) {
			List<CustomerObject> list = customerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/building/{id}/customer")
	public ResponseEntity<Customer> createCustomer(@PathVariable int id, @RequestBody Customer customer){
		try {
			Building building = buildingService.findById(id);
			if(building == null)
				return ResponseEntity.notFound().header("message", "No Building found with such ID").build();
			
			if(customerService.findById(customer.getCustomerID()) != null)
				return ResponseEntity.badRequest().header("message", "Customer with such ID already exists").build();
			
			
			customer.setBuilding(building);
			Customer savedCustomer = customerService.add(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new customer").build();
		}
	}
	
	@PutMapping("/building/{id}/customer")
	public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer){
		Building building = buildingService.findById(id);
		if(building == null) return ResponseEntity.notFound().header("message", "Building not found. Update failed").build();
		
		if(customerService.findById(customer.getCustomerID()) == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		
		customer.setBuilding(building);
		Customer updatedCustomer = customerService.update(customer);
		if(updatedCustomer != null)
			return ResponseEntity.ok(updatedCustomer);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable int id){
		try{
			if(customerService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
			
			customerService.delete(id);
			return ResponseEntity.noContent().header("message", "customer deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "customer deletion failed").build();
		}
	}
}
