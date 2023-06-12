package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Count;

@Repository
public interface CountRepository extends JpaRepository<Count, Long>, JpaSpecificationExecutor<Count>{

	public List<Count> findByProviderProviderID(long providerID);
	
	@Query("SELECT c FROM Count c WHERE c.count <= :threshold")
	public List<Count> findCountCloseToEnd(@Param("threshold") int threshold);
	
	@Query("SELECT c FROM Count c WHERE c.count <= " + com.swp.VinGiG.utilities.Constants.COUNT_DAY_OVERDUE)
	public List<Count> findCountExpired();
	
}
