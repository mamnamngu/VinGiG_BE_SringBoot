package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.SubscriptionFee;

@Repository
public interface DepositRepository  extends JpaRepository<Deposit, Long>, JpaSpecificationExecutor<Deposit>{


	public List<Deposit> findByMethod(String method);
	
	public List<Deposit> findByProviderProviderIDAndDateBetween(long providerID, Date dateMin, Date dateMax);
	public List<Deposit> findByDateBetween(Date dateMin, Date dateMax);
}
