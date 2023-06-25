package com.swp.VinGiG.controller;

import java.util.ArrayList;
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
import com.swp.VinGiG.view.GiGServiceObject;

@RestController
public class GiGServiceController {

	@Autowired
	private GiGServiceService giGServiceService;
	
	@Autowired
	private ServiceCategoryService serviceCategoryService;
	
	@GetMapping("/giGServices")
	public ResponseEntity<List<GiGServiceObject>> retrieveAllGiGServices(){
		List<GiGService> ls = giGServiceService.findAll();
		List<GiGServiceObject> list = giGServiceService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/giGService/{id}")
	public ResponseEntity<GiGServiceObject> retrieveGiGService(@PathVariable int id) {
		GiGService giGService = giGServiceService.findById(id);
		if(giGService != null) {
			List<GiGService> ls = new ArrayList<>();
			ls.add(giGService);
			List<GiGServiceObject> list = giGServiceService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/serviceCategory/{id}/giGServices")
	public ResponseEntity<List<GiGServiceObject>> retrieveAllGiGServicesOfCategory(@PathVariable int id){
		ServiceCategory category = serviceCategoryService.findById(id);
		if(category != null) {
			List<GiGService> ls = giGServiceService.findByServiceCategory(id);
			List<GiGServiceObject> list = giGServiceService.display(ls);
			return ResponseEntity.ok(list);
		}
		else return ResponseEntity.notFound().header("message", "No Service Category found for such ID").build();
    }
	
	@GetMapping("/giGServices/{keyword}")
	public ResponseEntity<List<GiGServiceObject>> retrieveGiGServicesByKeyword(@PathVariable String keyword){
		List<GiGService> ls = giGServiceService.findByKeyword(keyword.trim());
		List<GiGServiceObject> list = giGServiceService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	//admin
	@GetMapping("/giGServices/deleted")
	public ResponseEntity<List<GiGServiceObject>> findDeletedServices(){
		List<GiGService> ls = giGServiceService.findDeletedServices();
		List<GiGServiceObject> list = giGServiceService.display(ls);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/serviceCategory/{id}/giGService")
	public ResponseEntity<GiGService> createGiGService(@PathVariable int id, @RequestBody GiGService giGService){
		try {
			ServiceCategory category = serviceCategoryService.findById(id);
			if(category == null) return ResponseEntity.notFound().header("message", "Service Category not found. Adding failed").build();
			
			if(giGServiceService.findById(giGService.getServiceID()) != null) 
				return ResponseEntity.badRequest().header("message", "GiGService with such ID already exists").build();
			
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
			ServiceCategory category = serviceCategoryService.findById(id);
			if(category == null) return ResponseEntity.notFound().header("message", "Service Category not found. Delete failed").build();
			
			giGServiceService.delete(id);
			return ResponseEntity.noContent().header("message", "GiGService deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "GiGService deletion failed").build();
		}
	}
}
