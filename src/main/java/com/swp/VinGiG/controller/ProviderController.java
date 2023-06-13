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

import com.swp.VinGiG.entity.Badge;
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BadgeService;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.ProviderService;

@RestController
public class ProviderController {
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BadgeService badgeService;
	
	@GetMapping("/providers")
	public ResponseEntity<List<Provider>> retrieveAllProviders(){
		return ResponseEntity.ok(providerService.findAll());
    }
	
	@GetMapping("/provider/{id}")
	public ResponseEntity<Provider> retrieveProvider(@PathVariable int id) {
		Provider provider = providerService.findById(id);
		if(provider != null) {
			return ResponseEntity.status(HttpStatus.OK).body(provider);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("provider/username/{username}")
	public ResponseEntity<Provider> retrieveProviderByUserName(@PathVariable String username) {
		List<Provider> ls = providerService.findByUsername(username);
		if(ls.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ls.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("provider/fullName/{fullName}")
	public ResponseEntity<List<Provider>> retrieveProviderByFullName(@PathVariable String fullName) {
		List<Provider> ls = providerService.findByFullNameIgnoreCase(fullName);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("badge/{id}/providers")
	public ResponseEntity<List<Provider>> findProviderByBadge(@PathVariable("id") int id){
		if(badgeService.findById(id) == null) return ResponseEntity.notFound().header("message", "No Badge found for such ID").build();
		return ResponseEntity.ok(providerService.findByBadgeID(id));		
	}
	
	
//	@GetMapping("provider/createDate/{dateMin}/{dateMax}")
//	public ResponseEntity<List<Provider>> retrieveProviderByCreateDateInterval(@PathVariable Date dateMin, @PathVariable Date dateMax) {
//		List<Provider> ls = providerService.findByCreateDateInterval(dateMin, dateMax);
//		if(ls.size() > 0)
//			return ResponseEntity.status(HttpStatus.OK).body(ls);
//		else 
//			return ResponseEntity.notFound().build();
//	}
	
	@GetMapping("provider/rating/{lower}/{upper}")
	public ResponseEntity<List<Provider>> retrieveProviderByUserName(@PathVariable("lower") double lower, @PathVariable("upper") double upper) {
		List<Provider> ls = providerService.findByRatingInterval(lower, upper);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("building/{buildingID}/badge/{badgeID}/provider")
	public ResponseEntity<Provider> createProvider(@PathVariable int buildingID, @PathVariable int badgeID, @RequestBody Provider provider){
		try {
			Building building = buildingService.findById(buildingID);
			if(building == null)
				return ResponseEntity.notFound().header("message", "No Building found with such ID").build();
			
			Badge badge = badgeService.findById(badgeID);
			if(badge == null)
				return ResponseEntity.notFound().header("message", "No Badge found with such ID").build();
			
			provider.setBuilding(building);
			provider.setBadge(badge);
			Provider savedProvider = providerService.add(provider);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedProvider);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new provider").build();
		}
	}
	
	@PutMapping("building/{buildingID}/badge/{badgeID}/provider")
	public ResponseEntity<Provider> updateProvider(@PathVariable int buildingID, @PathVariable int badgeID, @RequestBody Provider provider){
		Building building = buildingService.findById(buildingID);
		if(building == null) return ResponseEntity.notFound().header("message", "Building not found. Update failed").build();
		
		Badge badge = badgeService.findById(badgeID);
		if(badge == null)
			return ResponseEntity.notFound().header("message", "No Badge found with such ID").build();
		
		if(providerService.findById(provider.getProviderID()) == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		Provider updatedProvider = providerService.update(provider);
		if(updatedProvider != null)
			return ResponseEntity.ok(updatedProvider);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/provider/{id}")
	public ResponseEntity<Void> deleteProvider(@PathVariable int id){
		try{
			providerService.delete(id);
			return ResponseEntity.noContent().header("message", "provider deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "provider deletion failed").build();
		}
	}
}