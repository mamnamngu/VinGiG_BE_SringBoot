package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.WalletRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.WalletObject;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepo;
	
	//FIND
	public List<Wallet> findAll(){
		return walletRepo.findByActiveIsTrue();
	}
	
	public List<Wallet> findDeletedWallet(){
		return walletRepo.findByActiveIsFalse();
	}
	
	public Wallet findById(long id) {
		return walletRepo.findByWalletIDAndActiveIsTrue(id);
	}
	
	public List<Wallet> findByProviderId(long providerID) {
		return walletRepo.findByProviderProviderIDAndActiveIsTrue(providerID);
	}
	
	public List<Wallet> findByBalanceBelowThreshold(){
		return walletRepo.findByBalanceBelowThreshold();
	}
	
	public List<Wallet> findByCreateDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return walletRepo.findByCreateDateBetweenAndActiveIsTrue(dateMin,dateMax);
	}
	
	public List<Wallet> findByBalanceInterval(Long lower, Long upper){
		if(lower == null) lower = Constants.DEFAULT_LOWER;
		if(upper == null) upper = Constants.DEFAULT_UPPER;		
		return walletRepo.findByBalanceBetweenAndActiveIsTrue(lower, upper);
	}
	
	//ADD
	public Wallet add(Wallet wallet) {
		return walletRepo.save(wallet);
	}
	
	//UPDATE
	public Wallet update(Wallet newWallet) {
		return add(newWallet);
	}
	
	//DELETE
	public boolean delete(long id) {
		Wallet wallet = findById(id);
		if(wallet == null) return false;
		wallet.setActive(false);
		update(wallet);
		return !wallet.isActive();
	}
	
	//DISPLAY
	public List<WalletObject> display(List<Wallet> ls){
		List<WalletObject> list = new ArrayList<>();
		for(Wallet x: ls) {
			WalletObject y = new WalletObject();
			y.setWalletID(x.getWalletID());
			y.setBalance(x.getBalance());
			y.setCreateDate(x.getCreateDate());
			y.setActive(x.isActive());
			
			Provider provider = x.getProvider();
			y.setProviderID(provider.getProviderID());
			y.setFullName(provider.getFullName());
			
			list.add(y);
		}
		return list;
	}
	
	//BUSINESS RULES
	public boolean transaction(Wallet wallet, long amount) {
		if (wallet.getBalance() + amount < 0) return false;
		wallet.setBalance(wallet.getBalance() + amount);
		return true;
	}
}
