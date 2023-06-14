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
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.ProviderService;
import com.swp.VinGiG.service.GiGServiceService;
import com.swp.VinGiG.service.ProviderServiceService;
import com.swp.VinGiG.view.ProviderServiceObject;

@RestController
public class ProviderServiceController {

	@Autowired
	private ProviderServiceService providerServiceService;

	@Autowired
	private GiGServiceService giGServiceService;

	@Autowired
	private com.swp.VinGiG.service.ProviderService providerService;

	@GetMapping("/providerServices")
	public ResponseEntity<List<ProviderService>> retrieveAllProviderServices() {
		return ResponseEntity.ok(providerServiceService.findAll());
	}

	@GetMapping("/providerService/{id}")
	public ResponseEntity<ProviderService> retrieveProviderService(@PathVariable int id) {
		ProviderService providerService = providerServiceService.findById(id);
		if (providerService != null) {
			return ResponseEntity.status(HttpStatus.OK).body(providerService);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// customer-visible ProviderService
	// display all currently available ProviderServices by Provider
	@GetMapping("/provider/{id}/providerServices")
	public ResponseEntity<List<ProviderService>> findByProviderIDAvailable(@PathVariable long id) {
		Provider provider = providerService.findById(id);
		if (provider != null) {
			return ResponseEntity.ok(providerServiceService.findByProviderProviderIDAndAvailabilityIsTrue(id));
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	 //display all currently available ProviderServices by GiGService
	@GetMapping("/giGservice/{id}/providerServices")
	public ResponseEntity<List<ProviderServiceObject>> findByServiceIDAvailable(@PathVariable int id) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
			List<ProviderService> proServiceList = providerServiceService.findByServiceServiceIDAndAvailabilityIsFalse(id);
			for(ProviderService x: proServiceList) {
				ls.add(providerServiceService.displayRender(x));
			}
			return ResponseEntity.ok(ls);
		} else
			return ResponseEntity.notFound().header("message", "No GiGService found for such ID").build();
	}

	//display all currently available ProviderServices by both Provider and GiGService (in Provider's home page)
	@GetMapping("/provider/{providerID}/giGservice/{serviceID}/providerServices")
	public ResponseEntity<List<ProviderService>> findByProviderIDAndServiceIDAvailable(@PathVariable("providerID") long providerID, @PathVariable("serviceID") int serviceID) {
		Provider provider = providerService.findById(providerID);
		if (provider != null) return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		GiGService service = giGServiceService.findById(serviceID);
		if (service != null) return ResponseEntity.notFound().header("message", "No GiGService found for such ID").build();

		return ResponseEntity.ok(providerServiceService.findByProviderProviderIDAndServiceServiceIDAndAvailabilityIsTrue(providerID,serviceID));
	}

	// display all currently available ProviderServices by GiGService within Price interval
	@GetMapping("/giGService/{id}/providerServices/price/{lower}/{upper}")
	public ResponseEntity<List<ProviderService>> findByServiceIDPriceIntervalAvailable(@PathVariable("id") int id, @PathVariable("lower") long lower, @PathVariable("upper") long upper) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			return ResponseEntity.ok(providerServiceService.findByServiceIDByUnitPriceIntervalAndAvailabilityIsTrue(id,lower,upper));
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	// display all currently available ProviderServices by GiGService within Rating interval
	@GetMapping("/giGService/{id}/providerServices/rating/{lower}/{upper}")
	public ResponseEntity<List<ProviderService>> findByServiceIDRatingIntervalAvailable(@PathVariable("id") int id, @PathVariable("lower") long lower, @PathVariable("upper") long upper) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			return ResponseEntity.ok(providerServiceService.findByServiceIDByRatingIntervalAndAvailabilityIsTrue(id,lower,upper));
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	// admin and provider-visible ProviderService

	@PostMapping("/providerService")
	public ResponseEntity<ProviderService> createProviderService(@RequestBody ProviderService providerService) {
		try {
			ProviderService savedProviderService = providerServiceService.add(providerService);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedProviderService);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "Failed to add new providerService").build();
		}
	}

	@PutMapping("/providerService")
	public ResponseEntity<ProviderService> updateProviderService(@RequestBody ProviderService providerService) {
		if (providerServiceService.findById(providerService.getProServiceID()) == null)
			return ResponseEntity.notFound().header("message", "No ProviderService found for such ID").build();

		ProviderService updatedProviderService = providerServiceService.update(providerService);
		if (updatedProviderService != null)
			return ResponseEntity.ok(updatedProviderService);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@DeleteMapping("/providerService/{id}")
	public ResponseEntity<Void> deleteProviderService(@PathVariable int id) {
		try {
			providerServiceService.delete(id);
			return ResponseEntity.noContent().header("message", "providerService deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "providerService deletion failed").build();
		}
	}
}
