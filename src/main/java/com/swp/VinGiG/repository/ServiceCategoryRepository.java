package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.ServiceCategory;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer>, JpaSpecificationExecutor<ServiceCategory>{

	public ServiceCategory findByCategoryIDAndActiveIsTrue(int categoryID);
	
	public List<ServiceCategory> findByActiveIsTrue();
	
	public List<ServiceCategory> findByActiveIsFalse();
	
	public List<ServiceCategory> findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(String categoryName, String description);
}
