package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Deposit;

@Repository
public interface DepositRepository  extends JpaRepository<Deposit, Long>, JpaSpecificationExecutor<Deposit>{

	public List<Deposit> findByMethodAndDateBetween(String method, Date dateMin, Date dateMax);
	
	public List<Deposit> findByProviderProviderIDAndDateBetweenOrderByDateDesc(long providerID, Date dateMin, Date dateMax);
	
	public List<Deposit> findByProviderProviderIDAndSuccessIsFalseAndDateBetweenOrderByDateDesc(long providerID, Date dateMin, Date dateMax);
	
	public List<Deposit> findByDateBetweenOrderByDateDesc(Date dateMin, Date dateMax);
}
