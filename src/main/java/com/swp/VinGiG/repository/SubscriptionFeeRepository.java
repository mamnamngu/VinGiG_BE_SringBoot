package com.swp.VinGiG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.SubscriptionFee;

@Repository
public interface SubscriptionFeeRepository extends JpaRepository<SubscriptionFee, Integer>, JpaSpecificationExecutor<SubscriptionFee>{

}
