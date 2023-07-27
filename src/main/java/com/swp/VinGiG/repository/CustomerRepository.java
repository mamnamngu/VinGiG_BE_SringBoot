package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>{

	@Query("SELECT c FROM Customer c WHERE c.rating BETWEEN :lower AND :upper AND c.active = TRUE")
	public List<Customer> findByRatingIntervalAndActiveIsTrue(@Param("lower") double lower, @Param("upper") double upper);
	
	@Query("SELECT c FROM Customer c WHERE c.createDate BETWEEN :dateMin AND :dateMax AND c.active = TRUE")
	public List<Customer> findByCreateDateIntervalAndActiveIsTrue(@Param("dateMin") Date dteMin, @Param("dateMax") Date dateMax);

	public List<Customer> findByUsernameAndActiveIsTrue(String username);
	
	public List<Customer> findByFullNameIgnoreCaseAndActiveIsTrue(String fullName);
	
	public Customer findByCustomerIDAndActiveIsTrue(long customerID);
	
	public List<Customer> findByActiveIsTrue();
	
	public List<Customer> findByActiveIsFalse();
	
	public Customer findByUsernameAndPassword(String username, String password);
	
	public Customer findByUsername(String username);
}
