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
import com.swp.VinGiG.service.BadgeService;

@RestController
public class BadgeController {
	@Autowired
	private BadgeService badgeService;
	
	@GetMapping("/badges")
	public ResponseEntity<List<Badge>> retrieveAllBadges(){
		return ResponseEntity.ok(badgeService.findAll());
    }
	
	@GetMapping("/badge/{id}")
	public ResponseEntity<Badge> retrieveBadge(@PathVariable int id) {
		Badge badge = badgeService.findById(id);
		if(badge != null) {
			return ResponseEntity.status(HttpStatus.OK).body(badge);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/badge")
	public ResponseEntity<Badge> createBadge(@RequestBody Badge badge){
		try {
			Badge savedBadge = badgeService.add(badge);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBadge);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new badge").build();
		}
	}
	
	@PutMapping("/badge")
	public ResponseEntity<Badge> updateBadge(@RequestBody Badge badge){
		if(badgeService.findById(badge.getBadgeID()) == null)
			return ResponseEntity.notFound().header("message", "No Badge found for such ID").build();
		
		Badge updatedBadge = badgeService.update(badge);
		if(updatedBadge != null)
			return ResponseEntity.ok(updatedBadge);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/badge/{id}")
	public ResponseEntity<Void> deleteBadge(@PathVariable int id){
		try{
			badgeService.delete(id);
			return ResponseEntity.noContent().header("message", "badge deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "badge deletion failed").build();
		}
	}
}
