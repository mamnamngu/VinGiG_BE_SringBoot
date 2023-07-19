package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Transction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.DepositRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.DepositObject;

@Service
public class DepositService {

	@Autowired
	private DepositRepository depositRepo;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	//FIND
	public List<Deposit> findAll(){
		return depositRepo.findAll();
	}
	
	public Deposit findById(long id) {
		Optional<Deposit> deposit = depositRepo.findById(id);
		if(deposit.isPresent()) return deposit.get();
		else return null;
	}
	
	public List<Deposit> findByProviderIDDateInterval(long ProviderID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return depositRepo.findByProviderProviderIDAndDateBetweenOrderByDateDesc(ProviderID, dateMin, dateMax);
	}
	
	public List<Deposit> findByDateInterval(Date dateMin,Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return depositRepo.findByDateBetweenOrderByDateDesc(dateMin, dateMax);
	}
	
	public List<Deposit> findByMethod(String method, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return depositRepo.findByMethodAndDateBetween(method, dateMin, dateMax);
	}
	
	//ADD
	public Deposit add(Deposit deposit) {
		deposit.setDate(Constants.currentDate());
		Deposit tmp = depositRepo.save(deposit);
		return tmp;
	}
	
	public Deposit confirmDeposit(Deposit deposit) {
		//Update deposit status
		deposit.setSuccess(true);
		update(deposit);
		
		//Create a transaction
		Transction transction = new Transction();
		transction.setDeposit(deposit);
		transction.setDate(Constants.currentDate());
		transction.setAmount(deposit.getAmount());
		
		Provider provider = deposit.getProvider();
		List<Wallet> wallet = walletService.findByProviderId(provider.getProviderID());
		if(wallet == null || wallet.size() == 0) return null;
		transction.setWallet(wallet.get(0));
	
		//save Transaction
		transactionService.add(transction);
		return deposit;		
	}
	
	//UPDATE
	public Deposit update(Deposit newDeposit) {
		return add(newDeposit);
	}
	
	//DELETE
	public boolean delete(long id) {
		depositRepo.deleteById(id);
		return !depositRepo.findById(id).isPresent();
	}
	
	//DISPLAY
	public List<DepositObject> display(List<Deposit> ls){
		List<DepositObject> list = new ArrayList<>();
		for(Deposit x: ls) {
			DepositObject y = new DepositObject();
			y.setDepositID(x.getDepositID());
			y.setAmount(x.getAmount());
			y.setDate(x.getDate());
			y.setMethod(x.getMethod());
			
			Provider provider = x.getProvider();
			y.setProviderID(provider.getProviderID());
			y.setFullName(provider.getFullName());
			
			list.add(y);
		}
		return list;
	}
	
	//BACKGROUND WORKER
	public List<Deposit> regularDelete(){
		Date currentDate = Constants.currentDate();	
		List<Deposit> ls = findByDateInterval(Constants.subtractDay(currentDate, Constants.DEPOSIT_DELETION),currentDate);
		for(Deposit x: ls) {
			delete(x.getDepositID());
		}
		return ls;
	}
}
