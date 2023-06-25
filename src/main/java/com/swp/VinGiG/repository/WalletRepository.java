package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.utilities.Constants;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet>{

	public List<Wallet> findByActiveIsTrue();
	
	public List<Wallet> findByActiveIsFalse();
	
	public List<Wallet> findByWalletIDAndActiveIsTrue(long walletID);
	
	public List<Wallet> findByProviderProviderIDAndActiveIsTrue(long providerID);
	
	@Query("SELECT w FROM Wallet w WHERE w.balance <= " + Constants.WALLET_BALANCE_THRESHOLD + " AND w.active IS TRUE")
	public List<Wallet> findByBalanceBelowThreshold();
	
	public List<Wallet> findByCreateDateBetweenAndActiveIsTrue(Date dateMin,  Date dateMax);

	public List<Wallet> findByBalanceBetweenAndActiveIsTrue(long lower, long upper);	
}
