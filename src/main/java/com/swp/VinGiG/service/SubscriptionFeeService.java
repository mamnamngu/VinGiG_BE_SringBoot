package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.SubscriptionPlan;
import com.swp.VinGiG.entity.Transction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.SubscriptionFeeRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.SubscriptionFeeObject;

@Service
public class SubscriptionFeeService {

	@Autowired
	private SubscriptionFeeRepository subscriptionFeeRepo;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private CountService countService;
	
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
		if(dateMax == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByDateBetween(dateMin, dateMax);
	}
	
	//provider
	public List<SubscriptionFee> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByProviderProviderIDAndDateBetweenOrderByDateDesc(providerID, dateMin, dateMax);
	}
	
	//admin
	public List<SubscriptionFee> findByPlanPlanIDAndDateBetween(int planID, Date dateMin,Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return subscriptionFeeRepo.findByPlanPlanIDAndDateBetweenOrderByDateDesc(planID, dateMin, dateMax);
	}
	
	//ADD
	public SubscriptionFee add(SubscriptionFee subscriptionFee) {
		subscriptionFee.setAmount(subscriptionFee.getPlan().getPrice());
		subscriptionFee.setDate(Constants.currentDate());
		
		//Create a transaction
		Transction transction = new Transction();
		transction.setAmount(subscriptionFee.getAmount()*Constants.TRANSACTION_SUBTRACT_FACTOR);
		transction.setDate(Constants.currentDate());
		transction.setSubscriptionFee(subscriptionFee);
		
		Provider provider = subscriptionFee.getProvider();
		List<Wallet> wallet = walletService.findByProviderId(provider.getProviderID());
		if(wallet == null || wallet.size() == 0) return null;
		transction.setWallet(wallet.get(0));
		
		//update the count
		List<Count> countLs = countService.findByProviderID(provider.getProviderID());
		if(countLs == null || countLs.size() < 1) return null;
		Count count = countLs.get(0);
		count.setCount(count.getCount() + subscriptionFee.getPlan().getDuration());
		countService.update(count);
		
		//save father object
		SubscriptionFee tmp = subscriptionFeeRepo.save(subscriptionFee);
		
		//save Transaction
		Transction output = transactionService.add(transction);
		if(output == null) {
			delete(tmp.getSubID());
			return null;
		}
	
		return tmp;
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
	
	//DISPLAY
	public List<SubscriptionFeeObject> display(List<SubscriptionFee> ls){
		List<SubscriptionFeeObject> list = new ArrayList<>();
		for(SubscriptionFee x: ls) {
			SubscriptionFeeObject y = new SubscriptionFeeObject();
			y.setSubID(x.getSubID());
			y.setAmount(x.getAmount());
			y.setDate(x.getDate());
			
			SubscriptionPlan plan = x.getPlan();
			y.setPlanID(plan.getPlanID());
			y.setDescription(plan.getDescription());
			
			Provider provider = x.getProvider();
			y.setProviderID(provider.getProviderID());
			y.setFullName(provider.getFullName());
			
			list.add(y);
		}
		return list;
	}
	
	//BACKGROUND WORKER
	public List<SubscriptionFee> regularDelete(){
		Date currentDate = Constants.currentDate();	
		List<SubscriptionFee> ls = findByDateInterval(Constants.subtractDay(currentDate, Constants.SUBSCRIPTIONFEE_DELETION),currentDate);	
		for(SubscriptionFee x: ls) {
			delete(x.getSubID());
		}
		return ls;
	}
}
