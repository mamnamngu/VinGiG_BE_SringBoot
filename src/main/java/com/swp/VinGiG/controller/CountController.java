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

import com.swp.VinGiG.entity.Count;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.CountService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.view.CountObject;

@RestController
public class CountController {
	
	@Autowired
	private CountService countService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/counts")
	public ResponseEntity<List<CountObject>> retrieveAllCounts(){
		List<Count> ls = countService.findAll();
		List<CountObject> list = countService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/counts/deleted")
	public ResponseEntity<List<CountObject>> retrieveAllDeletedCounts(){
		List<Count> ls = countService.findDeletedCount();
		List<CountObject> list = countService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/count/{id}")
	public ResponseEntity<CountObject> retrieveCount(@PathVariable long id) {
		Count count = countService.findById(id);
		if(count != null) {
			List<Count> ls = new ArrayList<>();
			ls.add(count);
			List<CountObject> list = countService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/counts")
	public ResponseEntity<List<CountObject>> findByProviderID(@PathVariable long id){
		Provider provider = providerService.findById(id);
		if(provider != null) {
			List<Count> ls = countService.findByProviderID(id);
			List<CountObject> list = countService.display(ls);
			return ResponseEntity.ok(list);
		}else return ResponseEntity.notFound().build();
    }
	
	@GetMapping("/provider/countUnderThreshold")
	public ResponseEntity<List<CountObject>> countUnderThreshold(){
		List<Count> ls = countService.countUnderThreshold();
		List<CountObject> list = countService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/provider/expiredCount")
	public ResponseEntity<List<CountObject>> expiredCount(){
		List<Count> ls = countService.expiredCount();
		List<CountObject> list = countService.display(ls);
		return ResponseEntity.ok(list);
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

			if(countService.findById(count.getCountID()) != null) return ResponseEntity.notFound().header("message", "Count with such ID already exists").build();

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
			if(countService.findById(id) == null) return ResponseEntity.notFound().header("message", "Count with such ID not found.").build();
			
			countService.delete(id);
			return ResponseEntity.noContent().header("message", "Count deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Count deletion failed").build();
		}
	}
}
