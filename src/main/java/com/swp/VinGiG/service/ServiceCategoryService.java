package com.swp.VinGiG.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.ServiceCategory;
import com.swp.VinGiG.repository.ServiceCategoryRepository;

@Service
public class ServiceCategoryService {
	@Autowired
	private ServiceCategoryRepository serviceCategoryRepo;
	
	//FIND
	public List<ServiceCategory> findAll(){
		return serviceCategoryRepo.findByActiveIsTrue();
	}
	
	public ServiceCategory findById(int id) {
		return serviceCategoryRepo.findByCategoryIDAndActiveIsTrue(id);
	}
	
	public List<ServiceCategory> findByKeyword(String keyword){
		return serviceCategoryRepo.findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(keyword.trim(), keyword.trim());
	}
	
	public List<ServiceCategory> findDeletedCategory(){
		return serviceCategoryRepo.findByActiveIsFalse();
	}
	
	//ADD
	public ServiceCategory add(ServiceCategory serviceCategory) {
		return serviceCategoryRepo.save(serviceCategory);
	}
	
	//UPDATE
	public ServiceCategory update(ServiceCategory newServiceCategory) {
		return add(newServiceCategory);
	}
	
	//DELETE
	public boolean delete(int id) {
		ServiceCategory category = findById(id);
		if(category == null) return false;
		category.setActive(false);
		update(category);
		return !category.isActive();
	}
	
}
