package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.SubscriptionPlan;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer>, JpaSpecificationExecutor<SubscriptionPlan>{

	public List<SubscriptionPlan> findByActiveIsTrue();
	
	public List<SubscriptionPlan> findByActiveIsFalse();
	
	public SubscriptionPlan findByPlanIDAndActiveIsTrue(int planID);
	
	public List<SubscriptionPlan> findByDescriptionContainingIgnoreCaseAndActiveIsTrue(String keyword);
}
