package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.repository.DepositRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class DepositService {

	@Autowired
	private DepositRepository depositRepo;
	
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
		if(dateMin == null) dateMax = Constants.currentDate();
		return depositRepo.findByProviderProviderIDAndDateBetween(ProviderID, dateMin, dateMax);
	}
	
	public List<Deposit> findByDateInterval(Date dateMin,Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMin == null) dateMax = Constants.currentDate();
		return depositRepo.findByDateBetween(dateMin, dateMax);
	}
	
	public List<Deposit> findByMethod(String method){
		return depositRepo.findByMethod(method);
	}
	
	//ADD
	public Deposit add(Deposit deposit) {
		
		//Add transaction
		return depositRepo.save(deposit);
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
}
