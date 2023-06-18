package com.swp.VinGiG.service;

import java.util.List;

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
		return planRepo.findByActiveIsTrue();
	}
	
	public SubscriptionPlan findById(int id) {
		SubscriptionPlan plan = planRepo.findByPlanIDAndActiveIsTrue(id);
		return plan;
	}
	
	public List<SubscriptionPlan> findByKeyword(String keyword){
		return planRepo.findByDescriptionContainingIgnoreCaseAndActiveIsTrue(keyword);
	}
	
	public List<SubscriptionPlan> findDeletedSubscriptionPlans(){
		return planRepo.findByActiveIsFalse();
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
		SubscriptionPlan plan = findById(id);
		if(plan == null) return false;
		plan.setActive(false);
		update(plan);
		return !plan.isActive();
	}
}
