package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.repository.CountRepository;
import com.swp.VinGiG.view.CountObject;

@Service
public class CountService {
	@Autowired
	private CountRepository countRepo;
	
	//FIND
	public List<Count> findAll(){
		return countRepo.findByActiveIsTrue();
	}
	
	public List<Count> findDeletedCount(){
		return countRepo.findByActiveIsFalse();
	}
	
	public Count findById(long id) {
		return countRepo.findByCountIDAndActiveIsTrue(id);
	}
	
	public List<Count> findByProviderID(long providerID) {
		return countRepo.findByProviderProviderIDAndActiveIsTrue(providerID);
	}
	
	//Filtering Count under threshold to notify for new Subscription
	public List<Count> countUnderThreshold(){
		return countRepo.findCountCloseToEnd(com.swp.VinGiG.utilities.Constants.COUNT_DAY_LEFT_NOTI);
	}
	
	public List<Count> expiredCount(){
		return countRepo.findCountExpired();
	}
	
	//ADD
	public Count add(Count count) {
		return countRepo.save(count);
	}
	
	//UPDATE
	public Count update(Count newCount) {
		return countRepo.save(newCount);
	}
	
	//DELETE
	public boolean delete(long id) {
		Count count = findById(id);
		if(count == null) return false;
		count.setActive(false);
		update(count);
		return !count.isActive();
	}
	
	//DISPLAY
	public List<CountObject> display(List<Count> ls){
		List<CountObject> list = new ArrayList<>();
		for(Count x: ls) {
			CountObject y = new CountObject();
			y.setCountID(x.getCountID());
			y.setCount(x.getCount());
			y.setActive(x.isActive());
			
			Provider provider = x.getProvider();
			y.setProviderID(provider.getProviderID());
			y.setFullName(provider.getFullName());
			
			list.add(y);
		}
		return list;
	}
	
	//BUSINESS RULE
	public void dailyDecrement() {
		List<Count> ls = findAll();
		for(Count x: ls) {
			x.setCount(x.getCount() - 1);
			update(x);
		}
	}

}
