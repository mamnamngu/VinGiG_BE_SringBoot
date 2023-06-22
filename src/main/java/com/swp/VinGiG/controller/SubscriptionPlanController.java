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

import com.swp.VinGiG.entity.SubscriptionPlan;
import com.swp.VinGiG.service.SubscriptionPlanService;

@RestController
public class SubscriptionPlanController {

	@Autowired
	private SubscriptionPlanService planService;
	
	@GetMapping("/subscriptionPlans")
	public ResponseEntity<List<SubscriptionPlan>> retrieveAllPlans(){
		return ResponseEntity.ok(planService.findAll());
	}
	
	@GetMapping("/subscriptionPlan/{id}")
	public ResponseEntity<SubscriptionPlan> retrievePlan(@PathVariable int id) {
		SubscriptionPlan plan = planService.findById(id);
		if(plan != null)
			return ResponseEntity.ok(plan);
		else return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/subscriptionPlans/keyword/{keyword}")
	public ResponseEntity<List<SubscriptionPlan>> retrievePlanByKeyword(@PathVariable String keyword) {
		return ResponseEntity.ok(planService.findByKeyword(keyword));
	}
	
	@GetMapping("/subscriptionPlans/deleted")
	public ResponseEntity<List<SubscriptionPlan>> retrieveDeletedPlans(){
		return ResponseEntity.ok(planService.findDeletedSubscriptionPlans());
	}
	
	@PostMapping("/subscriptionPlan")
	public ResponseEntity<SubscriptionPlan> createPlan(@RequestBody SubscriptionPlan plan){
		try {
			if(planService.findById(plan.getPlanID()) != null)
				return ResponseEntity.notFound().header("message", "Plan with such ID already exists").build();
			
			SubscriptionPlan newPlan = planService.add(plan);
			return ResponseEntity.status(HttpStatus.CREATED).body(newPlan);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new Plan").build();
		}
	}
	
	@PutMapping("/subscriptionPlan")
	public ResponseEntity<SubscriptionPlan> updatePlan(@RequestBody SubscriptionPlan plan){
		if(planService.findById(plan.getPlanID()) == null)
			return ResponseEntity.notFound().header("message", "No Plan found for such ID").build();
					
		SubscriptionPlan updatedPlan = planService.update(plan);
		if(updatedPlan != null) return ResponseEntity.ok(updatedPlan);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/subscriptionPlan/{id}")
	public ResponseEntity<Void> deletePlan(@PathVariable int id){
		try {
			if(planService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Plan found for such ID").build();
			
			planService.delete(id);
			return ResponseEntity.noContent().header("message", "Subscription Plan deleted successfully").build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Subscription Plan deletion failed").build();
		}
	}
}
