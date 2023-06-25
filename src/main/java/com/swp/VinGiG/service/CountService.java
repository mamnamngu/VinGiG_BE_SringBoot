package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.repository.CountRepository;

@Service
public class CountService {
	@Autowired
	private CountRepository countRepo;
	
	//FIND
	public List<Count> findAll(){
		return countRepo.findAll();
	}
	
	public Count findById(long id) {
		Optional<Count> count = countRepo.findById(id);
		if(count.isPresent()) return count.get();
		else return null;
	}
	
	public List<Count> findByProviderID(long providerID) {
		return countRepo.findByProviderProviderIDAndActiveIsTrue(providerID);
	}
	
	//ADD
	public Count add(Count count) {
		return countRepo.save(count);
	}
	
	//UPDATE
	public Count update(Count newCount) {
		return add(newCount);
	}
	
	//DELETE
	public boolean delete(long id) {
		countRepo.deleteById(id);
		return countRepo.findById(id).isEmpty();
	}
	
	public void dailyDecrement() {
		List<Count> ls = findAll();
		for(Count x: ls) {
			x.setCount(x.getCount() - 1);
			update(x);
		}
	}
	
	//Filtering Count under threshold to notify for new Subscription
	public List<Count> countUnderThreshold(){
		return countRepo.findCountCloseToEnd(com.swp.VinGiG.utilities.Constants.COUNT_DAY_LEFT_NOTI);
	}
	
	public List<Count> expiredCount(){
		return countRepo.findCountExpired();
	}
}
