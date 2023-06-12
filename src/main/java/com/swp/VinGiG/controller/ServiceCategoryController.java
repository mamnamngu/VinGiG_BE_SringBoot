package com.swp.VinGiG.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.ServiceCategory;
import com.swp.VinGiG.service.ServiceCategoryService;

@RestController
public class ServiceCategoryController {
	@Autowired
	private ServiceCategoryService serviceCategoryService;
	
	@GetMapping("/serviceCategories")
	public ResponseEntity<List<ServiceCategory>> retrieveAllServiceCategorys(){
		return ResponseEntity.ok(serviceCategoryService.findAll());
    }
	
	@GetMapping("/serviceCategory/{id}")
	public ResponseEntity<ServiceCategory> retrieveServiceCategory(@PathVariable int id) {
		ServiceCategory serviceCategory = serviceCategoryService.findById(id);
		if(serviceCategory != null) {
			return ResponseEntity.status(HttpStatus.OK).body(serviceCategory);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/serviceCategory")
	public ResponseEntity<ServiceCategory> createServiceCategory(@RequestBody ServiceCategory serviceCategory){
		try {
			ServiceCategory savedServiceCategory = serviceCategoryService.add(serviceCategory);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedServiceCategory);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new serviceCategory").build();
		}
	}
	
	@PutMapping("/serviceCategory")
	public ResponseEntity<ServiceCategory> updateServiceCategory(@RequestBody ServiceCategory serviceCategory){
		if(serviceCategoryService.findById(serviceCategory.getCategoryID()) == null)
			return ResponseEntity.notFound().header("message", "No Service Category found for such ID").build();
		
		ServiceCategory updatedServiceCategory = serviceCategoryService.update(serviceCategory);
		if(updatedServiceCategory != null)
			return ResponseEntity.ok(updatedServiceCategory);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/serviceCategory/{id}")
	public ResponseEntity<Void> deleteServiceCategory(@PathVariable int id){
		try{
			serviceCategoryService.delete(id);
			return ResponseEntity.noContent().header("message", "serviceCategory deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "serviceCategory deletion failed").build();
		}
	}
}
