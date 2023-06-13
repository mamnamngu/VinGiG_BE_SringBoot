package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Transaction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.TransactionRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepo;
	
	@Autowired
	WalletService walletService;
	
	// FIND
	public List<Transaction> findAll(){
			return transactionRepo.findAll();
		}

	public Transaction findById(long id) {
		Optional<Transaction> transaction = transactionRepo.findById(id);
		if (transaction.isPresent())
			return transaction.get();
		else
			return null;
	}
	
	//admin
	public List<Transaction> findByDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findByDateInterval(dateMin, dateMax);
	}
	
	public List<Transaction> findTypeDepositDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findTypeDepositDateInterval(dateMin, dateMax);
	}
	
	public List<Transaction> findTypeBookingFeeDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findTypeBookingFeeDateInterval(dateMin, dateMax);
	}
	
	public List<Transaction> findBySubscriptionFeeDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findBySubscriptionFeeDateInterval(dateMin, dateMax);
	}

	//provider
	public List<Transaction> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletIDDateInterval(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transaction> findByProviderIDTypeDepositDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletIDTypeDepositDateInterval(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transaction> findByProviderIDTypeBookingFeeDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletIDTypeBookingFeeDateInterval(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transaction> findByProviderIDBySubscriptionFeeDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletIDBySubscriptionFeeDateInterval(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	// ADD
	public Transaction add(Transaction transaction) {
		//Subtract from wallet an amount
		Wallet wallet = walletService.findById(transaction.getWallet().getWalletID());
		if(wallet == null) return null;
		if((wallet.getBalance() + transaction.getAmount()) < 0) return null;
		
		//transaction into wallet's balance
		wallet.setBalance(wallet.getBalance() + transaction.getAmount());
		walletService.update(wallet);
		
		return transactionRepo.save(transaction);
	}

	// UPDATE
	public Transaction update(Transaction transaction) {
		return transactionRepo.save(transaction);
	}

	// DELETE
	public boolean delete(long id) {
		transactionRepo.deleteById(id);
		return transactionRepo.findById(id).isEmpty();
	}

}
