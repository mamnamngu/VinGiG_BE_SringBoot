package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

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
		return serviceCategoryRepo.findAll();
	}
	
	public ServiceCategory findById(int id) {
		Optional<ServiceCategory> serviceCategory = serviceCategoryRepo.findById(id);
		if(serviceCategory.isPresent()) return serviceCategory.get();
		else return null;
	}
	
	public List<ServiceCategory> findByKeyword(String keyword){
		return serviceCategoryRepo.findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword.trim(), keyword.trim());
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
		serviceCategoryRepo.deleteById(id);
		return serviceCategoryRepo.findById(id).isEmpty();
	}
	
}
