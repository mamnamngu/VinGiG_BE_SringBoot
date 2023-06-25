package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.WalletRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepo;
	
	//FIND
	public List<Wallet> findAll(){
		return walletRepo.findAll();
	}
	
	public Wallet findById(long id) {
		Optional<Wallet> wallet = walletRepo.findById(id);
		if(wallet.isPresent()) return wallet.get();
		else return null;
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
		walletRepo.deleteById(id);
		return walletRepo.findById(id).isEmpty();
	}
	
	public boolean transaction(Wallet wallet, long amount) {
		if (wallet.getBalance() < amount) return false;
		wallet.setBalance(wallet.getBalance() + amount);
		return true;
	}
}
