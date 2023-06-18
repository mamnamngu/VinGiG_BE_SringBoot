package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.Transaction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.SubscriptionFeeRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class SubscriptionFeeService {

	@Autowired
	private SubscriptionFeeRepository subscriptionFeeRepo;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	//FIND
	public List<SubscriptionFee> findAll(){
		return subscriptionFeeRepo.findAll();
	}
	
	public SubscriptionFee findById(long id) {
		Optional<SubscriptionFee> subscriptionFee = subscriptionFeeRepo.findById(id);
		if(subscriptionFee.isPresent()) return subscriptionFee.get();
		else return null;
	}
	
	public List<SubscriptionFee> findByDateInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByDateBetween(dateMin, dateMax);
	}
	
	//provider
	public List<SubscriptionFee> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByProviderProviderIDAndDateBetween(providerID, dateMin, dateMax);
	}
	
	//admin
	public List<SubscriptionFee> findByPlanPlanIDAndDateBetween(int planID, Date dateMin,Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByPlanPlanIDAndDateBetween(planID, dateMin, dateMax);
	}
	
	//ADD
	public SubscriptionFee add(SubscriptionFee subscriptionFee) {
		subscriptionFee.setAmount(subscriptionFee.getPlan().getPrice());
		subscriptionFee.setDate(Constants.currentDate());
		
		//create transaction
//		Transaction transaction = new Transaction();
//		transaction.setAmount(0); //////////////////////////////////////////////////////////
//		transaction.setDate(Constants.currentDate());
//		transaction.setDeposit(null);
//		transaction.setSubscriptionFee(subscriptionFee);
//		transaction.setBookingFee(null);
		
//		List<Wallet> wallet = walletService.findByProviderId(subscriptionFee.getProvider().getProviderID());
//		if(wallet == null || wallet.size() == 0) return null;
//		transaction.setWallet(wallet.get(0));
//		transactionService.add(transaction);
		
		return subscriptionFeeRepo.save(subscriptionFee);
	}
	
	//UPDATE
	public SubscriptionFee update(SubscriptionFee newSubscriptionFee) {
		return add(newSubscriptionFee);
	}
	
	//DELETE
	public boolean delete(long id) {
		subscriptionFeeRepo.deleteById(id);
		return !subscriptionFeeRepo.findById(id).isPresent();
	}
}
