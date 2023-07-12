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

import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.SubscriptionPlan;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.service.SubscriptionFeeService;
import com.swp.VinGiG.service.SubscriptionPlanService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.SubscriptionFeeObject;

@RestController
public class SubscriptionFeeController {
	@Autowired
	private SubscriptionFeeService subscriptionFeeService;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@GetMapping("/subscriptionFees")
	public ResponseEntity<List<SubscriptionFeeObject>> retrieveAllSubscriptionFees() {
		List<SubscriptionFee> ls = subscriptionFeeService.findAll();
		List<SubscriptionFeeObject> list = subscriptionFeeService.display(ls);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/subscriptionFee/{id}")
	public ResponseEntity<SubscriptionFeeObject> retrieveSubscriptionFee(@PathVariable int id) {
		SubscriptionFee subscriptionFee = subscriptionFeeService.findById(id);
		if (subscriptionFee != null) {
			List<SubscriptionFee> ls = new ArrayList<>();
			ls.add(subscriptionFee);
			List<SubscriptionFeeObject> list = subscriptionFeeService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// admin
	@GetMapping("/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFeeObject>> findByDateInterval(@PathVariable("dateMin") String dateMinStr,
			@PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		List<SubscriptionFee> ls = subscriptionFeeService.findByDateInterval(dateMin, dateMax);
		List<SubscriptionFeeObject> list = subscriptionFeeService.display(ls);
		return ResponseEntity.ok(list);
	}

	// admin
	@GetMapping("/subscriptionPlan/{id}/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFeeObject>> findByPlanIDDateInterval(@PathVariable("id") int id,
			@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {	
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		SubscriptionPlan plan = subscriptionPlanService.findById(id);
		if (plan != null) {
			List<SubscriptionFee> ls = subscriptionFeeService.findByPlanPlanIDAndDateBetween(id, dateMin, dateMax);
			List<SubscriptionFeeObject> list = subscriptionFeeService.display(ls);
			return ResponseEntity.ok(list);
		} else
			return ResponseEntity.notFound().header("message", "No Subscription Plan found for such ID").build();
	}

	// provider
	@GetMapping("/provider/{id}/subscriptionFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<SubscriptionFeeObject>> findByProviderIDDateInterval(@PathVariable("id") long id,
			@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		Provider provider = providerService.findById(id);
		if (provider != null) {
			List<SubscriptionFee> ls = subscriptionFeeService.findByProviderIDDateInterval(id, dateMin, dateMax);
			List<SubscriptionFeeObject> list = subscriptionFeeService.display(ls);
			return ResponseEntity.ok(list);
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

			if (subscriptionFeeService.findById(subscriptionFee.getSubID()) == null)
				return ResponseEntity.badRequest().header("message", "SubscriptionFee with such ID already exists").build();

			subscriptionFee.setPlan(plan);
			subscriptionFee.setProvider(provider);
			subscriptionFee.setDate(Constants.currentDate());
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
			if (subscriptionFeeService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No SubscriptionFee found for such ID").build();

			subscriptionFeeService.delete(id);
			return ResponseEntity.noContent().header("message", "subscriptionFee deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "subscriptionFee deletion failed").build();
		}
	}
}
