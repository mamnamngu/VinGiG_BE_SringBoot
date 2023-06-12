package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Deposit;

@Repository
public interface DepositRepository  extends JpaRepository<Deposit, Long>, JpaSpecificationExecutor<Deposit>{

	@Query("SELECT d FROM Deposit d WHERE d.provider.getProviderID() = :providerID AND d.date BETWEEN :dateMin AND :dateMax")
	public List<Deposit> findByProviderIDInterval(@Param("providerID") long providerID, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);

	@Query("SELECT d FROM Deposit d WHERE d.date BETWEEN :dateMin AND :dateMax")
	public List<Deposit> findByDateInterval(@Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax);

	public List<Deposit> findByMethod(String method);
}
