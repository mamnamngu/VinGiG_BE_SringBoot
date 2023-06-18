package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider>{
	
	public List<Provider> findByUsernameAndActiveIsTrue(String username);
	
	public List<Provider> findByBadgeBadgeIDAndActiveIsTrue(int badgeID);
	
	@Query("SELECT p FROM Provider p WHERE p.rating BETWEEN :lower AND :upper AND p.active = TRUE")
	public List<Provider> findByRatingIntervalAndActiveIsTrue(@Param("lower") double lower, @Param("upper")double upper);

	public List<Provider> findByFullNameIgnoreCaseAndActiveIsTrue(String fullName);
	
	public List<Provider> findByActiveIsFalse();
	
	public List<Provider> findByActiveTrue();
	
	public Provider findByProviderIDAndActiveIsTrue(long providerID);
	
	public List<Provider> findByCreateDateBetween(Date minDate, Date maxDate);
	
	public List<Provider> findByBadgeBadgeIDAndCreateDateBetween(int badgeID, Date minDate, Date maxDate);
}
