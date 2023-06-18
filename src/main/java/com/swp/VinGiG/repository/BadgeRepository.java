package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Integer>, JpaSpecificationExecutor<Badge>{

	public Badge findByBadgeIDAndActiveIsTrue(int badgeID);
	
	public List<Badge> findByActiveIsTrue();
	
	public List<Badge> findByActiveIsFalse();
	
	public List<Badge> findByBadgeNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(String name, String description);

}
