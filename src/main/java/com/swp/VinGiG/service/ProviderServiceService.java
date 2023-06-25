package com.swp.VinGiG.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Image;
import com.swp.VinGiG.entity.ProviderService;
import com.swp.VinGiG.repository.ProviderServiceRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.ProviderServiceObject;

@Service
public class ProviderServiceService {

	@Autowired
	private ProviderServiceRepository providerServiceRepo;
	
	@Autowired
	private ImageService imageService;
	
	//FIND
	//admin
	public List<ProviderService> findAll(){
		return providerServiceRepo.findByActiveIsTrue();
	}
	
	public List<ProviderService> findDeletedProviderService(){
		return providerServiceRepo.findByActiveIsFalse();
	}
	
	public ProviderService findById(long id) {
		return providerServiceRepo.findByProServiceIDAndActiveIsTrue(id);
	}
	
	public List<ProviderService> findByServiceID(int serviceID){
		return providerServiceRepo.findByServiceServiceIDAndActiveIsTrue(serviceID);
	}
	
	public List<ProviderService> findByRatingInterval(Double lower, Double upper){
		if(lower == null) lower = (double)Constants.REVIEW_MIN;
		if(upper == null) upper = (double)Constants.REVIEW_MAX;
		return providerServiceRepo.findByRatingIntervalAndActiveIsTrue(lower, upper);
	}
	
	//provider
	public List<com.swp.VinGiG.entity.ProviderService> findByProviderID(long providerID){
		return providerServiceRepo.findByProviderProviderIDAndActiveIsTrue(providerID);
	}
	

	public List<com.swp.VinGiG.entity.ProviderService> findByProviderProviderIDAndServiceServiceID(long providerID, int serviceID){
		return providerServiceRepo.findByProviderProviderIDAndServiceServiceIDAndActiveIsTrue(providerID,serviceID);
	}
	
	//Customer
	
	public List<com.swp.VinGiG.entity.ProviderService> findByProviderIDAndServiceIDAndVisible(long providerID, int serviceID){
		return providerServiceRepo.findByProviderProviderIDAndServiceServiceIDAndVisibleIsTrueAndActiveIsTrue(providerID,serviceID);
	}
	
	public List<ProviderService> findByServiceIDAndAvailabilityAndVisible(int serviceID){
		return providerServiceRepo.findByServiceServiceIDAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(serviceID);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceIDByUnitPriceIntervalAndAvailabilityAndVisible(int serviceID, Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;
		return providerServiceRepo.findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(serviceID,lower, upper);
	}
	
	public List<com.swp.VinGiG.entity.ProviderService> findByServiceIDByRatingIntervalAndAvailabilityAndVisible(int serviceID, Double lower, Double upper){
		if(lower == null) lower = (double)Constants.REVIEW_MIN;
		if(upper == null) upper = (double)Constants.REVIEW_MAX;
		return providerServiceRepo.findByServiceIDByRatingIntervalAndAvailabilityIsTrueAndVisibleIsTrueAndActiveIsTrue(serviceID, lower, upper);
	}
	
	//ADD
	public ProviderService add(ProviderService providerService) {
		return providerServiceRepo.save(providerService);
	}
	
	//UPDATE
	public ProviderService update(ProviderService newProviderService) {
		return providerServiceRepo.save(newProviderService);
	}
	
	//DELETE
	public boolean delete(long id) {
		ProviderService providerService = findById(id);
		if(providerService == null) return false;
		providerService.setActive(false);
		update(providerService);
		return !providerService.isActive();	
	}
	
	public List<ProviderService> deleteByProviderID(long providerID){
		List<ProviderService> ls = findByProviderID(providerID);
		for(ProviderService x: ls) {
			x.setActive(false);
			update(x);
		}
		return ls;
	}
	
	public List<ProviderService> deleteByServiceID(int serviceID){
		List<ProviderService> ls = findByServiceID(serviceID);
		for(ProviderService x: ls) {
			x.setActive(false);
			update(x);
		}
		return ls;
	}
	
	//Display
	public ProviderServiceObject displayRender(ProviderService x) {
		ProviderServiceObject object = new ProviderServiceObject();
		object.setProServiceID(x.getProServiceID());
		object.setRating(x.getRating());
		object.setBookingNo(x.getBookingNo());
		object.setUnitPrice(x.getUnitPrice());
		object.setDescription(x.getDescription());
		object.setAvailablity(x.isAvailability());
		object.setVisible(x.isVisible());
		object.setActive(x.isActive());
		
		//Provider
		object.setProviderID(x.getProvider().getProviderID());
		object.setFullName(x.getProvider().getFullName());
		object.setGender(x.getProvider().isGender());
		
		//Badge
		object.setBadgeID(x.getProvider().getBadge().getBadgeID());
		object.setBadgeName(x.getProvider().getBadge().getBadgeName());
		
		//Image
		List<Image> ls = imageService.findByProviderServiceID(x.getProServiceID());
		object.setLink(ls.get(0).getLink());
		return object;
	}	
	
	//Availability
	public List<ProviderService> setAvailability(long providerID, boolean status) {
		List<ProviderService> ls = findByProviderID(providerID);
		if(ls == null || ls.size() == 0) return null;
		for(ProviderService x: ls) {
			x.setAvailability(status);
			update(x);
		}
		return ls;
	}
}
