package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.repository.CustomerRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepo;
	
	//FIND
	public List<Customer> findAll(){
		return customerRepo.findAll();
	}
	
	public Customer findById(long id) {
		Optional<Customer> customer = customerRepo.findById(id);
		if(customer.isPresent()) return customer.get();
		else return null;
	}
	
	public List<Customer> findByRatingInterval(double lower, double upper){
		return customerRepo.findByRatingInterval(lower, upper);
	}
	
	public List<Customer> findByCreateDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return customerRepo.findByCreateDateInterval(dateMin, dateMax);
	}
	
	public List<Customer> findByUsername(String username){
		return customerRepo.findByUsername(username);
	}
	
	public List<Customer> findByFullNameIgnoreCase(String fullName){
		return customerRepo.findByUsername(fullName);
	}
	
	//ADD
	public Customer add(Customer customer) {
		return customerRepo.save(customer);
	}
	
	//UPDATE
	public Customer update(Customer newCustomer) {
		return add(newCustomer);
	}
	
	//DELETE
	public boolean delete(long id) {
		customerRepo.deleteById(id);
		return customerRepo.findById(id).isEmpty();
	}
}
