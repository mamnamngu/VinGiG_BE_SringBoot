package com.swp.VinGiG.controller;

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

import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.SubscriptionPlan;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.service.SubscriptionFeeService;
import com.swp.VinGiG.service.SubscriptionPlanService;

@RestController
public class SubscriptionFeeController {
	@Autowired
	private SubscriptionFeeService subscriptionFeeService;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@GetMapping("/subscriptionFees")
	public ResponseEntity<List<SubscriptionFee>> retrieveAllSubscriptionFees() {
		return ResponseEntity.ok(subscriptionFeeService.findAll());
	}

	@GetMapping("/subscriptionFee/{id}")
	public ResponseEntity<SubscriptionFee> retrieveSubscriptionFee(@PathVariable int id) {
		SubscriptionFee subscriptionFee = subscriptionFeeService.findById(id);
		if (subscriptionFee != null) {
			return ResponseEntity.status(HttpStatus.OK).body(subscriptionFee);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// admin
	@GetMapping("/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFee>> findByDateInterval(@PathVariable("dateMin") Date dateMin,
			@PathVariable("dateMax") Date dateMax) {
		return ResponseEntity.ok(subscriptionFeeService.findByDateInterval(dateMin, dateMax));
	}

	// admin
	@GetMapping("/subscriptionPlan/{id}/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFee>> findByPlanIDDateInterval(@PathVariable("id") int id,
			@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		SubscriptionPlan plan = subscriptionPlanService.findById(id);
		if (plan != null) {
			return ResponseEntity.ok(subscriptionFeeService.findByPlanPlanIDAndDateBetween(id, dateMin, dateMax));
		} else
			return ResponseEntity.notFound().header("message", "No Subscription Plan found for such ID").build();
	}

	// provider
	@GetMapping("/provider/{id}/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFee>> findByProviderIDDateInterval(@PathVariable("id") long id,
			@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		Provider provider = providerService.findById(id);
		if (provider != null) {
			return ResponseEntity.ok(subscriptionFeeService.findByProviderIDDateInterval(id, dateMin, dateMax));
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	@PostMapping("/subscriptionPlan/{planID}/provider/{providerID}/subscriptionFee")
	public ResponseEntity<SubscriptionFee> createSubscriptionFee(@PathVariable("planID") int planID, @PathVariable("providerID") long providerID, @RequestBody SubscriptionFee subscriptionFee) {
		try {
			SubscriptionPlan plan = subscriptionPlanService.findById(planID);
			if (plan == null)
				return ResponseEntity.notFound().header("message", "No Subscription Plan found for such planID").build();

			Provider provider = providerService.findById(providerID);
			if (provider == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such providerID").build();

			subscriptionFee.setPlan(plan);
			subscriptionFee.setProvider(provider);
			SubscriptionFee savedSubscriptionFee = subscriptionFeeService.add(subscriptionFee);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedSubscriptionFee);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new subscriptionFee").build();
		}
	}

	@PutMapping("/subscriptionPlan/{planID}/provider/{providerID}/subscriptionFee")
	public ResponseEntity<SubscriptionFee> updateSubscriptionFee(@PathVariable("planID") int planID, @PathVariable("providerID") long providerID, @RequestBody SubscriptionFee subscriptionFee) {
		try {
			if (subscriptionFeeService.findById(subscriptionFee.getSubID()) == null)
				return ResponseEntity.notFound().header("message", "No SubscriptionFee found for such ID").build();

			SubscriptionPlan plan = subscriptionPlanService.findById(planID);
			if (plan == null)
				return ResponseEntity.notFound().header("message", "No Subscription Plan found for such planID").build();

			Provider provider = providerService.findById(providerID);
			if (provider == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such providerID").build();

			subscriptionFee.setPlan(plan);
			subscriptionFee.setProvider(provider);
			SubscriptionFee updatedSubscriptionFee = subscriptionFeeService.update(subscriptionFee);
			
			if (updatedSubscriptionFee != null) 
				return ResponseEntity.ok(updatedSubscriptionFee);
			else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();		
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update subscriptionFee").build();
		}
	}

	@DeleteMapping("/subscriptionFee/{id}")
	public ResponseEntity<Void> deleteSubscriptionFee(@PathVariable int id) {
		try {
			subscriptionFeeService.delete(id);
			return ResponseEntity.noContent().header("message", "subscriptionFee deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "subscriptionFee deletion failed").build();
		}
	}
}
