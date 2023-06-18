package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.GiGService;

@Repository
public interface GiGServiceRepository extends JpaRepository<GiGService, Integer>, JpaSpecificationExecutor<GiGService>{

	public List<GiGService> findByActiveIsTrue();
	
	public List<GiGService> findByActiveIsFalse();
	
	public GiGService findByServiceIDAndActiveIsTrue(int serviceID);
	
	public List<GiGService> findByServiceCategoryCategoryIDAndActiveIsTrue(int categoryID);
	
	public List<GiGService> findByServiceNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(String serviceName, String description);
}
