package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider>{
	
	public List<Provider> findByUsername(String username);
	
	public List<Provider> findByBadgeBadgeID(int badgeID);
	
	@Query("SELECT p FROM Provider p WHERE p.rating BETWEEN :lower AND :upper")
	public List<Provider> findByRatingInterval(@Param("lower") double lower, @Param("upper")double upper);

	public List<Provider> findByFullNameIgnoreCase(String fullName);
	
	//Update the badge of new Provider
//	@Query("SELECT p FROM Provider p  WHERE p.badge.badgeID = " + Constants.NEW_PROVIDER_BADGEID + " AND p.createDate < (SELECT DAYADD(DAY, -30, GETDATE()) AS Result)")
//	public List<Provider> expiredNewProvider();
}
