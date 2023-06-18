package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;

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
		return customerRepo.findByActiveIsTrue();
	}
	
	public Customer findById(long id) {
		return customerRepo.findByCustomerIDAndActiveIsTrue(id);
	}
	
	public List<Customer> findDeletedCustomers(){
		return customerRepo.findByActiveIsFalse();
	}
	
	public List<Customer> findByRatingInterval(Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return customerRepo.findByRatingIntervalAndActiveIsTrue(lower, upper);
	}
	
	public List<Customer> findByCreateDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return customerRepo.findByCreateDateIntervalAndActiveIsTrue(dateMin, dateMax);
	}
	
	public List<Customer> findByUsername(String username){
		return customerRepo.findByUsernameAndActiveIsTrue(username);
	}
	
	public List<Customer> findByFullNameIgnoreCase(String fullName){
		return customerRepo.findByFullNameIgnoreCaseAndActiveIsTrue(fullName);
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
		Customer customer = findById(id);
		if(customer == null) return false;
		customer.setActive(false);
		update(customer);
		return !customer.isActive();
	}
}
