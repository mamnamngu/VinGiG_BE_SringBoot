package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.repository.GiGServiceRepository;

@Service
public class GiGServiceService {
	@Autowired
	private GiGServiceRepository serviceRepo;
	
	//FIND
	public List<GiGService> findAll(){
		return serviceRepo.findAll();
	}
	
	public GiGService findById(int id) {
		Optional<GiGService> service = serviceRepo.findById(id);
		if(service.isPresent()) return service.get();
		else return null;
	}
	
	public List<GiGService> findByServiceCategory(int serviceCategoryID){
		return serviceRepo.findByServiceCategoryCategoryID(serviceCategoryID);
	}
	
	public List<GiGService> findByKeyword(String keyword){
		return serviceRepo.findByServiceNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword.trim(), keyword.trim());
	}
	
	//ADD
	public GiGService add(GiGService service) {
		return serviceRepo.save(service);
	}
	
	//UPDATE
	public GiGService update(GiGService newService) {
		return add(newService);
	}
	
	//DELETE
	public boolean delete(int id) {
		serviceRepo.deleteById(id);
		return serviceRepo.findById(id).isEmpty();
	}
	
}
