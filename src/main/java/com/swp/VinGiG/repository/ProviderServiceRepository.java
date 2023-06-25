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

	//Admin
	
	public List<ProviderService> findByActiveIsTrue();
	
	public List<ProviderService> findByActiveIsFalse();
	
	public ProviderService findByProServiceIDAndActiveIsTrue(long proServiceID);
	
	public List<ProviderService> findByServiceServiceIDAndActiveIsTrue(int serviceID);
	
	public List<ProviderService> findByRatingBetweenAndActiveIsTrue(double lower, double upper);

	//Provider + admin
	
	public List<ProviderService> findByProviderProviderIDAndActiveIsTrue(long providerID);
	
	public List<ProviderService> findByProviderProviderIDAndServiceServiceIDAndActiveIsTrue(long providerID, int serviceID);
	
	//Display all customer-visible ProviderService
	
	//Customer can view even inavailable services in a provider's profile
	public List<ProviderService> findByProviderProviderIDAndServiceServiceIDAndVisibleIsTrueAndActiveIsTrue(long providerID, int serviceID);
	
	public List<ProviderService> findByServiceServiceIDAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(int serviceID);
	
	@Query("SELECT p FROM ProviderService p WHERE p.service.getServiceID() = :serviceID AND p.rating BETWEEN :lower AND :upper AND p.availability IS TRUE AND p.visible IS TRUE AND p.active IS TRUE")
	public List<ProviderService> findByServiceIDByRatingIntervalAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(@Param("serviceID") int serviceID, @Param("lower") double lower, @Param("upper") double upper);
	
	@Query("SELECT p FROM ProviderService p WHERE p.service.getServiceID() = :serviceID AND p.unitPrice BETWEEN :lower AND :upper AND p.availability IS TRUE AND p.visible IS TRUE AND p.active IS TRUE")
	public List<ProviderService> findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(@Param("serviceID") int serviceID, @Param("lower") double lower, @Param("upper") double upper);
}
