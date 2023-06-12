package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.SubscriptionPlan;
import com.swp.VinGiG.repository.SubscriptionPlanRepository;

@Service
public class SubscriptionPlanService {
	@Autowired
	private SubscriptionPlanRepository planRepo;
	
	//FIND
	public List<SubscriptionPlan> findAll(){
		return planRepo.findAll();
	}
	
	public SubscriptionPlan findById(int id) {
		Optional<SubscriptionPlan> plan = planRepo.findById(id);
		if(plan.isPresent()) return plan.get();
		else return null;
	}
	
	public List<SubscriptionPlan> findByKeyword(String keyword){
		return planRepo.findByDescriptionContainingIgnoreCase(keyword);
	}
	
	//ADD
	public SubscriptionPlan add(SubscriptionPlan plan) {
		return planRepo.save(plan);
	}
	
	//UPDATE
	public SubscriptionPlan update(SubscriptionPlan newPlan) {
		return add(newPlan);
	}
	
	//DELETE
	public boolean delete(int id) {
		planRepo.deleteById(id);
		return planRepo.findById(id).isEmpty();
	}
}
