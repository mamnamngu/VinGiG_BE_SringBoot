package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.ProviderService;
import com.swp.VinGiG.repository.ProviderServiceRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class ProviderServiceService {

	@Autowired
	private ProviderServiceRepository providerServiceRepo;
	
	//FIND
	public List<ProviderService> findAll(){
		return providerServiceRepo.findAll();
	}
	
	public ProviderService findById(long id) {
		Optional<ProviderService> providerService = providerServiceRepo.findById(id);
		if(providerService.isPresent()) return providerService.get();
		else return null;
	}
	
	//Display all admin and provider-visible ProviderService
	
	public List<com.swp.VinGiG.entity.ProviderService> findByProviderID(long providerID){
		return providerServiceRepo.findByProviderProviderID(providerID);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceID(int serviceID){
		return providerServiceRepo.findByServiceServiceID(serviceID);
	}

	public List<com.swp.VinGiG.entity.ProviderService> findByProviderProviderIDAndServiceServiceID(long providerID, int serviceID){
		return providerServiceRepo.findByProviderProviderIDAndServiceServiceID(providerID,serviceID);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByRatingInterval(Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return providerServiceRepo.findByRatingInterval(lower, upper);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByUnitPriceInterval(Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return providerServiceRepo.findByUnitPriceInterval(lower, upper);
	}
	
	//Display all customer-visible ProviderService
	
	public List<com.swp.VinGiG.entity.ProviderService> findByProviderProviderIDAndAvailabilityIsTrue(long providerID){
		return providerServiceRepo.findByProviderProviderIDAndAvailabilityIsTrue(providerID);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceServiceIDAndAvailabilityIsFalse(int serviceID){
		return providerServiceRepo.findByServiceServiceID(serviceID);
	}

	public List<com.swp.VinGiG.entity.ProviderService> findByProviderProviderIDAndServiceServiceIDAndAvailabilityIsTrue(long providerID, int serviceID){
		return providerServiceRepo.findByProviderProviderIDAndServiceServiceIDAndAvailabilityIsTrue(providerID,serviceID);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrue(int serviceID, Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return providerServiceRepo.findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrue(serviceID,lower, upper);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceIDByRatingIntervalAndAvailabilityIsTrue(int serviceID, Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return providerServiceRepo.findByServiceIDByRatingIntervalAndAvailabilityIsTrue(serviceID, lower, upper);
	}
	
	//ADD
	public ProviderService add(ProviderService providerService) {
		return providerServiceRepo.save(providerService);
	}
	
	//UPDATE
	public ProviderService update(ProviderService newProviderService) {
		return add(newProviderService);
	}
	
	//DELETE
	public boolean delete(long id) {
		providerServiceRepo.deleteById(id);
		return providerServiceRepo.findById(id).isEmpty();
	}
	
}