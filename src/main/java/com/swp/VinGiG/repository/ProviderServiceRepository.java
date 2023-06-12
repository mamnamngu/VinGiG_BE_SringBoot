package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.ProviderService;

@Repository
public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long>, JpaSpecificationExecutor<ProviderService>{

	//Display all admin and provider-visible ProviderService
	
	public List<ProviderService> findByProviderProviderID(long providerID);
	
	public List<ProviderService> findByServiceServiceID(int serviceID);
	
	public List<ProviderService> findByProviderProviderIDAndServiceServiceID(long providerID, int serviceID);

	//Display all customer-visible ProviderService
	
	public List<ProviderService> findByProviderProviderIDAndAvailabilityIsTrue(int providerID);
	
	public List<ProviderService> findByServiceServiceIDAndAvailabilityIsTrue(int serviceID);
	
	public List<ProviderService> findByProviderProviderIDAndServiceServiceIDAndAvailabilityIsTrue(long providerID, int serviceID);
	
	//Display admin and provider-visible ProviderService by intervals
	
	@Query("SELECT p FROM ProviderService p WHERE p.rating BETWEEN :lower AND :upper")
	public List<ProviderService> findByRatingInterval(@Param("lower") double lower, @Param("upper") double upper);
	
	@Query("SELECT p FROM ProviderService p WHERE p.unitPrice BETWEEN :lower AND :upper")
	public List<ProviderService> findByUnitPriceInterval(@Param("lower") double lower, @Param("upper") double upper);
	
	//Display customer-visible ProviderService by intervals
	
	@Query("SELECT p FROM ProviderService p WHERE p.service.getServiceID() = :serviceID AND p.rating BETWEEN :lower AND :upper AND p.availability IS TRUE")
	public List<ProviderService> findByServiceIDByRatingIntervalAndAvailabilityIsTrue(@Param("serviceID") int serviceID, @Param("lower") double lower, @Param("upper") double upper);
	
	@Query("SELECT p FROM ProviderService p WHERE p.service.getServiceID() = :serviceID AND p.unitPrice BETWEEN :lower AND :upper AND p.availability IS TRUE")
	public List<ProviderService> findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrue(@Param("serviceID") int serviceID, @Param("lower") double lower, @Param("upper") double upper);
}
