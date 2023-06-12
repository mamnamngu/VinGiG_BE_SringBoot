package com.swp.VinGiG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Integer>, JpaSpecificationExecutor<Badge>{
//	@Modifying
//    @Query("UPDATE Badge b SET b.active = false WHERE b.id = :badgeId")
//    void deactivateUser(@Param("badgeId") int badgeId);
}
