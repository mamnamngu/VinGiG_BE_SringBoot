package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.utilities.Constants;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet>{

	public List<Wallet> findByProviderProviderID(long providerID);
	
	@Query("SELECT w FROM Wallet w WHERE w.balance <= " + Constants.WALLET_BALANCE_THRESHOLD)
	public List<Wallet> findByBalanceBelowThreshold();
	
	@Query("SELECT w FROM Wallet w WHERE w.createDate BETWEEN :dateMin AND :dateMax")
	public List<Wallet> findByCreateDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);

	@Query("SELECT w FROM Wallet w WHERE w.balance BETWEEN :lower AND :upper")
	public List<Wallet> findByBalanceInterval(@Param("lower") long lower, @Param("upper") long upper);	
}
