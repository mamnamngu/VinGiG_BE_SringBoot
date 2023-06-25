package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.repository.GiGServiceRepository;
import com.swp.VinGiG.view.GiGServiceObject;

@Service
public class GiGServiceService {
	@Autowired
	private GiGServiceRepository serviceRepo;
	
	//FIND
	public List<GiGService> findAll(){
		return serviceRepo.findByActiveIsTrue();
	}
	
	public GiGService findById(int id) {
		return serviceRepo.findByServiceIDAndActiveIsTrue(id);
	}
	
	public List<GiGService> findDeletedServices(){
		return serviceRepo.findByActiveIsFalse();
	}
	
	public List<GiGService> findByServiceCategory(int serviceCategoryID){
		return serviceRepo.findByServiceCategoryCategoryIDAndActiveIsTrue(serviceCategoryID);
	}
	
	public List<GiGService> findByKeyword(String keyword){
		return serviceRepo.findByServiceNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(keyword.trim(), keyword.trim());
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
		GiGService service = findById(id);
		if(service == null) return false;
		service.setActive(false);
		update(service);
		return !service.isActive();
	}
	
	//DISPLAY
	public List<GiGServiceObject> display(List<GiGService> ls){
		List<GiGServiceObject> list = new ArrayList<GiGServiceObject>();
		for(GiGService x: ls) {
			GiGServiceObject y = new GiGServiceObject();
			y.setServiceID(x.getServiceID());
			y.setServiceName(x.getServiceName());
			y.setDescription(x.getDescription());
			y.setUnit(x.getUnit());
			y.setPriceMin(x.getPriceMin());
			y.setPriceMax(x.getPriceMax());
			y.setFee(x.getFee());
			y.setActive(x.isActive());
			y.setCategoryID(x.getServiceCategory().getCategoryID());
			y.setCategoryName(x.getServiceCategory().getCategoryName());
			list.add(y);
		}
		return list;
	}
}
