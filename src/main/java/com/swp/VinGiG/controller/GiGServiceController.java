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

import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.entity.ServiceCategory;
import com.swp.VinGiG.service.GiGServiceService;
import com.swp.VinGiG.service.ServiceCategoryService;

@RestController
public class GiGServiceController {

	@Autowired
	private GiGServiceService giGServiceService;
	
	@Autowired
	private ServiceCategoryService serviceCategoryService;
	
	@GetMapping("/giGServices")
	public ResponseEntity<List<GiGService>> retrieveAllGiGServices(){
		return ResponseEntity.ok(giGServiceService.findAll());
    }
	
	@GetMapping("/giGService/{id}")
	public ResponseEntity<GiGService> retrieveGiGService(@PathVariable int id) {
		GiGService giGService = giGServiceService.findById(id);
		if(giGService != null) {
			return ResponseEntity.status(HttpStatus.OK).body(giGService);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/serviceCategory/{id}/giGServices")
	public ResponseEntity<List<GiGService>> retrieveAllGiGServicesOfCategory(@PathVariable int id){
		ServiceCategory category = serviceCategoryService.findById(id);
		if(category != null)
			return ResponseEntity.ok(giGServiceService.findByServiceCategory(id));
		else return ResponseEntity.notFound().build();
    }
	
	@GetMapping("/giGServices/{keyword}")
	public ResponseEntity<List<GiGService>> retrieveGiGServicesByKeyword(@PathVariable String keyword){
		return ResponseEntity.ok(giGServiceService.findByKeyword(keyword.trim()));
    }
	
	@PostMapping("/serviceCategory/{id}/giGService")
	public ResponseEntity<GiGService> createGiGService(@PathVariable int id, @RequestBody GiGService giGService){
		try {
			ServiceCategory category = serviceCategoryService.findById(id);
			if(category == null) return ResponseEntity.notFound().header("message", "Service Category not found. Adding failed").build();
			
			giGService.setServiceCategory(category);
			GiGService savedGiGService = giGServiceService.add(giGService);
			if(savedGiGService != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedGiGService);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new giGService").build();
		}
	}
	
	@PutMapping("/serviceCategory/{id}/giGService")
	public ResponseEntity<GiGService> updateGiGService(@PathVariable int id, @RequestBody GiGService giGService){
		try {
			ServiceCategory category = serviceCategoryService.findById(id);
			if(category == null) return ResponseEntity.notFound().header("message", "Service Category not found. Update failed").build();
			
			if(giGServiceService.findById(giGService.getServiceID()) == null) return ResponseEntity.notFound().header("message", "GiGService with such ID not found. Update failed").build();
			giGService.setServiceCategory(category);
			GiGService savedGiGService = giGServiceService.update(giGService);
			if(savedGiGService != null)
				return ResponseEntity.status(HttpStatus.OK).body(savedGiGService);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update giGService").build();
		}
	}
	
	@DeleteMapping("/giGService/{id}")
	public ResponseEntity<Void> deleteGiGService(@PathVariable int id){
		try{
			giGServiceService.delete(id);
			return ResponseEntity.noContent().header("message", "GiGService deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "GiGService deletion failed").build();
		}
	}
}
