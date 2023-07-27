package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Badge;
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.ProviderRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.ProviderObject;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepo;
	
	@Autowired
	BadgeService badgeService;
	
	@Autowired
	CountService countService;
	
	@Autowired
	WalletService walletService;
	
	//FIND
	public List<Provider> findAll(){
		return providerRepo.findByActiveTrue();
	}
	
	public List<Provider> findDeletedProviders(){
		return providerRepo.findByActiveIsFalse();
	}
	
	public Provider findById(long id) {
		return providerRepo.findByProviderIDAndActiveIsTrue(id);
	}
	
	public List<Provider> findByBadgeID(int badgeID){
		return providerRepo.findByBadgeBadgeIDAndActiveIsTrue(badgeID);
	}
	
	public List<Provider> findByRatingInterval(Double lower, Double upper){
		if(lower == null) lower = (double)Constants.REVIEW_MIN;
		if(upper == null) upper = (double)Constants.REVIEW_MAX;
		return providerRepo.findByRatingBetweenAndActiveIsTrue(lower, upper);
	}
	
	public List<Provider> findByCreateDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax =Constants.currentDate();
		return providerRepo.findByCreateDateBetween(dateMin, dateMax);
	}
	
	public List<Provider> findByUsername(String username) {
		return providerRepo.findByUsernameAndActiveIsTrue(username);
	}
	
	public List<Provider> findByFullNameIgnoreCase(String fullName){
		return providerRepo.findByFullNameIgnoreCaseAndActiveIsTrue(fullName);
	}
	
	private List<Provider> findExpiredNewProvider(){
		Date currentDate = Constants.currentDate();
		
		Calendar cal = Calendar.getInstance();  
		cal.add(Calendar.DAY_OF_YEAR, 30);
		Date cutoffDate = cal.getTime();
		return providerRepo.findByBadgeBadgeIDAndCreateDateBetween(Constants.NEW_PROVIDER_BADGEID, cutoffDate, currentDate);
	}
	
	public Provider login(String username, String password) {
		if(username == null || password == null) return null;
		return providerRepo.findByUsernameAndPassword(username, password);
	}
	
	public Provider checkConflict(String username) {
		if(username == null) return null;
		return providerRepo.findByUsername(username);
	}
	
	//ADD
	public Provider add(Provider provider) {
		Provider newProvider = providerRepo.save(provider);
		if(newProvider == null) return null;
		
		//Create count
		Count count = new Count();
		count.setCount(Constants.DEFAULT_COUNT);
		count.setProvider(newProvider);
		countService.add(count);
		
		//Create wallet
		Wallet wallet = new Wallet();
		wallet.setBalance(Constants.NEW_WALLET_BALANCE_PROMO);
		wallet.setCreateDate(Constants.currentDate());
		wallet.setProvider(newProvider);
		walletService.add(wallet);;
		
		return newProvider;
	}
	
	//UPDATE
	public Provider update(Provider newProvider) {
		return providerRepo.save(newProvider);
	}
	
	
	//DELETE
	public boolean delete(long id) {
		Provider provider = findById(id);
		if(provider == null) return false;
		provider.setActive(false);
		update(provider);
		return !provider.isActive();
	}
	
	//Business Rule
	public List<Provider> resetNewProviderBadge() {
		List<Provider> ls = findExpiredNewProvider();
		Badge badge = badgeService.findById(Constants.DEFAULT_BADGEID);
		for(Provider x: ls) {
			x.setBadge(badge);
			update(x);
		}
		return ls;
	}
	
	//DISPLAY
	public List<ProviderObject> display(List<Provider> ls){
		List<ProviderObject> list = new ArrayList<>();
		for(Provider x: ls) {
			ProviderObject y = new ProviderObject();
			y.setProviderID(x.getProviderID());
			y.setFullName(x.getFullName());
			y.setAvatar(x.getAvatar());
			y.setGender(x.isGender());
			y.setEmail(x.getEmail());
			y.setPhone(x.getPhone());
			y.setAddress(x.getAddress());
			y.setCreateDate(x.getCreateDate());
			y.setRating(x.getRating());
			y.setActive(x.isActive());
			y.setRole(x.getRole());
			
			Badge badge = x.getBadge();
			y.setBadgeID(badge.getBadgeID());
			y.setBadgeName(badge.getBadgeName());
			
			Building building = x.getBuilding();
			y.setBuildingID(building.getBuildingID());
			y.setBuildingName(building.getBuildingName());
			
			list.add(y);
		}
		return list;
	}
	
}
