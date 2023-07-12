package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Transction;

@Repository
public interface TransctionRepository  extends JpaRepository<Transction, Long>, JpaSpecificationExecutor<Transction>{
	//Admin queries

	public List<Transction> findByWalletWalletIDAndDateBetween(long walletID, Date dateMin, Date dateMax);
	
	public List<Transction> findByDateBetween(Date dateMin, Date dateMax);

	public List<Transction> findByDepositNotNullAndDateBetween(Date dateMin, Date dateMax);
	
	public List<Transction> findByBookingFeeNotNullAndDateBetween(Date dateMin, Date dateMax);
	
	public List<Transction> findBySubscriptionFeeNotNullAndDateBetween(Date dateMin, Date dateMax);

	//Provider's queries
	public List<Transction> findByWalletWalletIDAndDepositNotNullAndDateBetween(long walletID, Date dateMin, Date dateMax);
	
	public List<Transction> findByWalletWalletIDAndBookingFeeNotNullAndDateBetween(long walletID, Date dateMin, Date dateMax);
	
	public List<Transction> findByWalletWalletIDAndSubscriptionFeeNotNullAndDateBetween(long walletID, Date dateMin, Date dateMax);
}
