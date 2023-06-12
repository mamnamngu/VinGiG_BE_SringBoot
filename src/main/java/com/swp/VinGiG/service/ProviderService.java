package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.ProviderRepository;
import com.swp.VinGiG.utilities.Constants;

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
		return providerRepo.findAll();
	}
	
	public Provider findById(long id) {
		Optional<Provider> provider = providerRepo.findById(id);
		if(provider.isPresent()) return provider.get();
		else return null;
	}
	
	public List<Provider> findByBadgeID(int badgeID){
		return providerRepo.findByBadgeBadgeID(badgeID);
	}
	
	public List<Provider> findByRatingInterval(Double lower, Double upper){
		if(lower == null) lower = (double)Constants.REVIEW_MIN;
		if(upper == null) upper = (double)Constants.REVIEW_MAX;
		return providerRepo.findByRatingInterval(lower, upper);
	}
	
	public List<Provider> findByUsername(String username) {
		return providerRepo.findByUsername(username);
	}
	
	public List<Provider> findByFullNameIgnoreCase(String fullName){
		return providerRepo.findByFullNameIgnoreCase(fullName);
	}
	
	//ADD
	public Provider add(Provider provider) {
		//Create count
		Count count = new Count();
		count.setCount(1);
		count.setProvider(provider);
		countService.add(count);
		
		//Create wallet
		Wallet wallet = new Wallet();
		wallet.setBalance(Constants.NEW_WALLET_BALANCE_PROMO);
		wallet.setCreateDate(Constants.currentDate());
		wallet.setProvider(provider);
		walletService.add(wallet);;
		
		return providerRepo.save(provider);
	}
	
	//UPDATE
	public Provider update(Provider newProvider) {
		return providerRepo.save(newProvider);
	}
	
	//Update the badge of new Provider
//	public List<Provider> updateBadgeOfExpiredNewProvider(){
//		List<Provider> ls = providerRepo.expiredNewProvider();
//		for(Provider p : ls) {
//			p.setBadge(badgeService.findById(Constants.DEFAULT_BADGEID));
//			update(p);
//		}
//		return ls;
//	}
	
	//DELETE
	public boolean delete(long id) {
		providerRepo.deleteById(id);
		return providerRepo.findById(id).isEmpty();
	}
	
}
