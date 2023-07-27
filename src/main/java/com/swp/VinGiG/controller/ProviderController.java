package com.swp.VinGiG.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.swp.VinGiG.entity.Badge;
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BadgeService;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.ProviderObject;

@RestController
public class ProviderController {
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BadgeService badgeService;
	
	@GetMapping("/providers")
	public ResponseEntity<List<ProviderObject>> retrieveAllProviders(){
		List<Provider> ls = providerService.findAll();
		List<ProviderObject> list = providerService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/provider/{id}")
	public ResponseEntity<ProviderObject> retrieveProvider(@PathVariable int id) {
		Provider provider = providerService.findById(id);
		if(provider != null) {
			List<Provider> ls = new ArrayList<>();
			ls.add(provider);
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/providers/deleted")
	public ResponseEntity<List<ProviderObject>> retrieveDeletedProviders(){
		List<Provider> ls = providerService.findDeletedProviders();
		List<ProviderObject> list = providerService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/provider/username/{username}")
	public ResponseEntity<ProviderObject> retrieveProviderByUserName(@PathVariable String username) {
		List<Provider> ls = providerService.findByUsername(username);
		if(ls.size() > 0) {
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/fullName/{fullName}")
	public ResponseEntity<List<ProviderObject>> retrieveProviderByFullName(@PathVariable String fullName) {
		List<Provider> ls = providerService.findByFullNameIgnoreCase(fullName);
		if(ls.size() > 0) {
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/badge/{id}/providers")
	public ResponseEntity<List<ProviderObject>> findProviderByBadge(@PathVariable("id") int id){
		if(badgeService.findById(id) == null) return ResponseEntity.notFound().header("message", "No Badge found for such ID").build();
		List<Provider> ls = providerService.findByBadgeID(id);
		List<ProviderObject> list = providerService.display(ls);
		return ResponseEntity.ok(list);		
	}
	
	@GetMapping("/provider/rating/{lower}/{upper}")
	public ResponseEntity<List<ProviderObject>> retrieveProviderByRatingInterval(@PathVariable("lower") double lower, @PathVariable("upper") double upper) {
		List<Provider> ls = providerService.findByRatingInterval(lower, upper);
		if(ls.size() > 0) {
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/provider/createDate/{dateMin}/{dateMax}")
	public ResponseEntity<List<ProviderObject>> retrieveProviderByCreateDateInterval(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		List<Provider> ls = providerService.findByCreateDateInterval(dateMin, dateMax);
		if(ls.size() > 0) {
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/providers/resetExpiredNewProvider")
	public ResponseEntity<List<ProviderObject>> resetExpiredNewProvider(){
		try {
			List<Provider> ls = providerService.resetNewProviderBadge();
			List<ProviderObject> list = providerService.display(ls);
			return ResponseEntity.ok(list);
		}catch(Exception e) {
			return ResponseEntity.internalServerError().header("message", "Error occured during the rest process").build();
		}
	}
	
	@PostMapping("/building/{buildingID}/badge/{badgeID}/provider")
	public ResponseEntity<Provider> createProvider(@PathVariable int buildingID, @PathVariable int badgeID, @RequestBody Provider provider){
//		try {
			Building building = buildingService.findById(buildingID);
			if(building == null)
				return ResponseEntity.notFound().header("message", "No Building found with such ID").build();
			
			Badge badge = badgeService.findById(badgeID);
			if(badge == null)
				return ResponseEntity.notFound().header("message", "No Badge found with such ID").build();
			
			if(providerService.findById(provider.getProviderID()) != null)
				return ResponseEntity.badRequest().header("message", "Provider with such ID already exists").build();
			
			provider.setBuilding(building);
			provider.setBadge(badge);
			Provider savedProvider = providerService.add(provider);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedProvider);
//		}catch(Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new provider").build();
//		}
	}
	
	@PutMapping("/building/{buildingID}/badge/{badgeID}/provider")
	public ResponseEntity<Provider> updateProvider(@PathVariable int buildingID, @PathVariable int badgeID, @RequestBody Provider provider){
		Building building = buildingService.findById(buildingID);
		if(building == null) return ResponseEntity.notFound().header("message", "Building not found. Update failed").build();
		
		Badge badge = badgeService.findById(badgeID);
		if(badge == null)
			return ResponseEntity.notFound().header("message", "No Badge found with such ID").build();
		
		if(providerService.findById(provider.getProviderID()) == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		provider.setBadge(badge);
		provider.setBuilding(building);
		
		Provider updatedProvider = providerService.update(provider);
		if(updatedProvider != null)
			return ResponseEntity.ok(updatedProvider);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/provider/{id}")
	public ResponseEntity<Void> deleteProvider(@PathVariable int id){
		try{
			if(providerService.findById(id) == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
			if(providerService.delete(id))
				return ResponseEntity.noContent().header("message", "provider deleted successfully").build();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "provider deletion failed").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "provider deletion failed").build();
		}
	}
}
