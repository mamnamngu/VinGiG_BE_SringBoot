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

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.CountService;
import com.swp.VinGiG.service.ProviderService;

@RestController
public class CountController {
	
	@Autowired
	private CountService countService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/counts")
	public ResponseEntity<List<Count>> retrieveAllCounts(){
		return ResponseEntity.ok(countService.findAll());
    }
	
	@GetMapping("/count/{id}")
	public ResponseEntity<Count> retrieveCount(@PathVariable long id) {
		Count count = countService.findById(id);
		if(count != null) {
			return ResponseEntity.status(HttpStatus.OK).body(count);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/counts")
	public ResponseEntity<List<Count>> retrieveAllCountsOfCategory(@PathVariable long id){
		Provider provider = providerService.findById(id);
		if(provider != null)
			return ResponseEntity.ok(countService.findByProviderID(id));
		else return ResponseEntity.notFound().build();
    }
	
	@GetMapping("/provider/countUnderThreshold")
	public ResponseEntity<List<Count>> countUnderThreshold(){
		return ResponseEntity.ok(countService.countUnderThreshold());
    }
	
	@GetMapping("/provider/expiredCount")
	public ResponseEntity<List<Count>> expiredCount(){
		return ResponseEntity.ok(countService.expiredCount());
    }
	
	@GetMapping("/provider/dailyDecrement")
	public ResponseEntity<Void> dailyDecrement(){
		countService.dailyDecrement();
		return ResponseEntity.ok().build();
    }
	
	@PostMapping("/provider/{id}/count")
	public ResponseEntity<Count> createCount(@PathVariable long id, @RequestBody Count count){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "Provider not found. Adding failed").build();
			
			count.setProvider(provider);
			Count savedCount = countService.add(count);
			if(savedCount != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedCount);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new count").build();
		}
	}
	
	@PutMapping("/provider/{id}/count")
	public ResponseEntity<Count> updateCount(@PathVariable long id, @RequestBody Count count){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "Provider not found. Update failed").build();
			
			if(countService.findById(count.getCountID()) == null) return ResponseEntity.notFound().header("message", "Count with such ID not found. Update failed").build();
			
			count.setProvider(provider);
			Count savedCount = countService.update(count);
			if(savedCount != null)
				return ResponseEntity.status(HttpStatus.OK).body(savedCount);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update count").build();
		}
	}
	
	@DeleteMapping("/count/{id}")
	public ResponseEntity<Void> deleteCount(@PathVariable long id){
		try{
			countService.delete(id);
			return ResponseEntity.noContent().header("message", "Count deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Count deletion failed").build();
		}
	}
}
