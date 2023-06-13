package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Transaction;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>{
	//Admin queries
	@Query("SELECT t FROM Transaction t WHERE t.wallet.getWalletID() = :walletID AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findByWalletIDDateInterval(@Param("walletID") long walletID, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
	
	@Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findByDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);

	@Query("SELECT t FROM Transaction t WHERE t.deposit IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findTypeDepositDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
	
	@Query("SELECT t FROM Transaction t WHERE t.bookingFee IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findTypeBookingFeeDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
	
	@Query("SELECT t FROM Transaction t WHERE t.subscriptionFee IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findBySubscriptionFeeDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);

	//Provider's queries
	@Query("SELECT t FROM Transaction t WHERE t.wallet.getWalletID() = :walletID AND t.deposit IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findByWalletIDTypeDepositDateInterval(@Param("walletID") long walletID, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
	
	@Query("SELECT t FROM Transaction t WHERE t.wallet.getWalletID() = :walletID AND t.bookingFee IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findByWalletIDTypeBookingFeeDateInterval(@Param("walletID") long walletID, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
	
	@Query("SELECT t FROM Transaction t WHERE t.wallet.getWalletID() = :walletID AND t.subscriptionFee IS NOT NULL AND t.date BETWEEN :dateMin AND :dateMax")
	public List<Transaction> findByWalletIDBySubscriptionFeeDateInterval(@Param("walletID") long walletID, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);
}
