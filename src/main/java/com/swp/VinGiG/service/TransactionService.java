package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Transction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.TransctionRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.TransactionObject;

@Service
public class TransactionService {
	
	@Autowired
	TransctionRepository transactionRepo;
	
	@Autowired
	WalletService walletService;
	
	// FIND
	public List<Transction> findAll(){
			return transactionRepo.findAll();
		}

	public Transction findById(long id) {
		Optional<Transction> transction = transactionRepo.findById(id);
		if (transction.isPresent())
			return transction.get();
		else
			return null;
	}
	
	//admin
	public List<Transction> findByDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findByDateBetween(dateMin, dateMax);
	}
	
	public List<Transction> findTypeDepositDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findByDepositNotNullAndDateBetween(dateMin, dateMax);
	}
	
	public List<Transction> findTypeBookingFeeDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findByBookingFeeNotNullAndDateBetween(dateMin, dateMax);
	}
	
	public List<Transction> findByTypeSubscriptionFeeDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return transactionRepo.findBySubscriptionFeeNotNullAndDateBetween(dateMin, dateMax);
	}

	//provider
	public List<Transction> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletWalletIDAndDateBetween(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transction> findByProviderIDTypeDepositDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletWalletIDAndDepositNotNullAndDateBetween(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transction> findByProviderIDTypeBookingFeeDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletWalletIDAndBookingFeeNotNullAndDateBetween(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	public List<Transction> findByProviderIDBySubscriptionFeeDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		List<Wallet> wallet = walletService.findByProviderId(providerID);
		if(wallet == null || wallet.size() == 0) return null;
		
		return transactionRepo.findByWalletWalletIDAndSubscriptionFeeNotNullAndDateBetween(wallet.get(0).getWalletID(), dateMin, dateMax);
	}
	
	// ADD
	public Transction add(Transction transction) {
		//Subtract from wallet an amount
		Wallet wallet = walletService.findById(transction.getWallet().getWalletID());
		if(wallet == null) return null;
		if((wallet.getBalance() + transction.getAmount()) < 0) return null;

		//transaction into wallet's balance
		wallet.setBalance(wallet.getBalance() + transction.getAmount());
		walletService.update(wallet);
		
		return transactionRepo.save(transction);
	}

	// UPDATE
	public Transction update(Transction transction) {
		return transactionRepo.save(transction);
	}

	// DELETE
	public boolean delete(long id) {
		transactionRepo.deleteById(id);
		return transactionRepo.findById(id).isEmpty();
	}

	//DISPLAY
	public List<TransactionObject> display(List<Transction> ls){
		List<TransactionObject> list = new ArrayList<>();
		for(Transction x: ls) {
			TransactionObject y = new TransactionObject();
			y.setTransactionID(x.getTransactionID());
			y.setAmount(x.getAmount());
			y.setDate(x.getDate());
			
			Provider provider = x.getWallet().getProvider();
			y.setProviderID(provider.getProviderID());
			y.setProviderFullName(provider.getFullName());
			
			y.setDepositID(x.getDeposit().getDepositID());
			
			y.setSubID(x.getSubscriptionFee().getSubID());
			
			y.setBookingFeeID(x.getBookingFee().getBookingFeeID());
			
			list.add(y);
		}
		return list;
	}
		
	//BACKGROUND WORKER
	public List<Transction> regularDelete(){
		Date currentDate = Constants.currentDate();
		List<Transction> lsDeposit = findTypeDepositDateInterval(Constants.subtractDay(currentDate, Constants.BOOKINGFEEE_DELETION),currentDate);
		List<Transction> lsBookingFee = findTypeBookingFeeDateInterval(Constants.subtractDay(currentDate, Constants.BOOKINGFEEE_DELETION),currentDate);
		List<Transction> lsSubscriptionFee = findByTypeSubscriptionFeeDateInterval(Constants.subtractDay(currentDate, Constants.BOOKINGFEEE_DELETION),currentDate);

		List<Transction> ls = new ArrayList<>();
		ls.addAll(lsDeposit);
		ls.addAll(lsBookingFee);
		ls.addAll(lsSubscriptionFee);
		
		for(Transction x: ls) {
			delete(x.getTransactionID());
		}
		return ls;
	}
}
