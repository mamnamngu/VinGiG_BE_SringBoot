package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.SubscriptionFee;

@Repository
public interface SubscriptionFeeRepository extends JpaRepository<SubscriptionFee, Long>, JpaSpecificationExecutor<SubscriptionFee>{
	
	public List<SubscriptionFee> findByDateBetween(Date dateMin, Date dateMax);
	
	//admin
	public List<SubscriptionFee> findByPlanPlanIDAndDateBetweenOrderByDateDesc(int planID, Date dateMin, Date dateMax);
	
	//provider
	public List<SubscriptionFee> findByProviderProviderIDAndDateBetweenOrderByDateDesc(long providerID, Date dateMin, Date dateMax);
}
