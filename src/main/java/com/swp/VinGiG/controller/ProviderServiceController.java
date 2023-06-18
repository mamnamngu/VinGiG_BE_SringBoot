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

	//ADMIN
	@GetMapping("/providerServices")
	public ResponseEntity<List<ProviderService>> retrieveAllProviderServices() {
		return ResponseEntity.ok(providerServiceService.findAll());
	}
	
	@GetMapping("/providerServices/deleted")
	public ResponseEntity<List<ProviderService>> retrieveDeletedProviderServices() {
		return ResponseEntity.ok(providerServiceService.findDeletedProviderService());
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
	
	@GetMapping("/giGService/{id}/providerServices")
	public ResponseEntity<List<ProviderService>> findByServiceID(@PathVariable("id") int id){
		GiGService service = giGServiceService.findById(id);
		if(service == null)
			return ResponseEntity.notFound().header("message", "No GiGService found for such ID").build();
		
		return ResponseEntity.ok(providerServiceService.findByServiceID(id));
	}
	
	@GetMapping("/providerServices/rating/{lower}/{upper}")
	public ResponseEntity<List<ProviderService>> findByRatingInterval(@PathVariable("lower") Double lower, @PathVariable("upper") Double upper) {
		return ResponseEntity.ok(providerServiceService.findByRatingInterval(lower, upper));
	}

	//PROVIDER
	@GetMapping("/provider/{id}/providerServices")
	public ResponseEntity<List<ProviderService>> findByProviderID(@PathVariable long id) {
		Provider provider = providerService.findById(id);
		if (provider != null) {
			return ResponseEntity.ok(providerServiceService.findByProviderID(id));
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}
	
	@GetMapping("giGService/{serviceID}/provider/{providerID}/providerServices")
	public ResponseEntity<List<ProviderServiceObject>> findByProviderIDAndServiceID(@PathVariable("serviceID") int serviceID, @PathVariable("providerID") long providerID) {
		Provider provider = providerService.findById(providerID);
		if(provider == null) return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		GiGService service = giGServiceService.findById(serviceID);
		if(service == null) return ResponseEntity.notFound().header("message", "No GiG Service found for such ID").build();
		
		List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
		List<ProviderService> proServiceList = providerServiceService.findByProviderProviderIDAndServiceServiceID(providerID, serviceID);
		for(ProviderService x: proServiceList) {
			ls.add(providerServiceService.displayRender(x));
		}
		return ResponseEntity.ok(ls);	}
	
	//CUSTOMER
	//display all currently available ProviderServices by GiGService
	@GetMapping("/giGservice/{id}/providerServices")
	public ResponseEntity<List<ProviderServiceObject>> findByServiceIDAvailable(@PathVariable int id) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
			List<ProviderService> proServiceList = providerServiceService.findByServiceIDAndAvailabilityAndVisible(id);
			for(ProviderService x: proServiceList) {
				ls.add(providerServiceService.displayRender(x));
			}
			return ResponseEntity.ok(ls);
		} else
			return ResponseEntity.notFound().header("message", "No GiGService found for such ID").build();
	}

	//display all currently available ProviderServices by both Provider and GiGService (in Provider's home page)
	@GetMapping("/provider/{providerID}/giGservice/{serviceID}/providerServices")
	public ResponseEntity<List<ProviderServiceObject>> findByProviderIDAndServiceIDAvailable(@PathVariable("providerID") long providerID, @PathVariable("serviceID") int serviceID) {
		Provider provider = providerService.findById(providerID);
		if (provider != null) return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		GiGService service = giGServiceService.findById(serviceID);
		if (service != null) return ResponseEntity.notFound().header("message", "No GiGService found for such ID").build();

		List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
		List<ProviderService> proServiceList = providerServiceService.findByProviderIDAndServiceIDAndVisible(providerID, serviceID);
		for(ProviderService x: proServiceList) {
			ls.add(providerServiceService.displayRender(x));
		}
		return ResponseEntity.ok(ls);
	}

	// display all currently available ProviderServices by GiGService within Price interval
	@GetMapping("/giGService/{id}/providerServices/price/{lower}/{upper}")
	public ResponseEntity<List<ProviderServiceObject>> findByServiceIDPriceIntervalAvailable(@PathVariable("id") int id, @PathVariable("lower") Long lower, @PathVariable("upper") Long upper) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
			List<ProviderService> proServiceList = providerServiceService.findByServiceIDByUnitPriceIntervalAndAvailabilityAndVisible(id, lower, upper);
			for(ProviderService x: proServiceList) {
				ls.add(providerServiceService.displayRender(x));
			}
			return ResponseEntity.ok(ls);
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	// display all currently available ProviderServices by GiGService within Rating interval
	@GetMapping("/giGService/{id}/providerServices/rating/{lower}/{upper}")
	public ResponseEntity<List<ProviderServiceObject>> findByServiceIDRatingIntervalAvailable(@PathVariable("id") int id, @PathVariable("lower") Double lower, @PathVariable("upper") Double upper) {
		GiGService service = giGServiceService.findById(id);
		if (service != null) {
			List<ProviderServiceObject> ls = new ArrayList<ProviderServiceObject>();
			List<ProviderService> proServiceList = providerServiceService.findByServiceIDByRatingIntervalAndAvailabilityAndVisible(id, lower, upper);
			for(ProviderService x: proServiceList) {
				ls.add(providerServiceService.displayRender(x));
			}
			return ResponseEntity.ok(ls);
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	@PostMapping("/providerService")
	public ResponseEntity<ProviderService> createProviderService(@RequestBody ProviderService providerService) {
		if (providerServiceService.findById(providerService.getProServiceID()) != null)
			return ResponseEntity.notFound().header("message", "ProviderService with such ID already exists").build();

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

	//delete by proServiceID
	@DeleteMapping("/providerService/{id}")
	public ResponseEntity<Void> deleteProviderService(@PathVariable long id) {
		try {
			if (providerServiceService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No ProviderService found for such ID").build();

			providerServiceService.delete(id);
			return ResponseEntity.noContent().header("message", "providerService deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "providerService deletion failed").build();
		}
	}
	
	//delete by providerID
	@DeleteMapping("provider/{id}/providerServices")
	public ResponseEntity<Void> deleteProviderServicesByProviderID(@PathVariable long id) {
		try {
			if (providerService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();

			providerServiceService.deleteByProviderID(id);
			return ResponseEntity.noContent().header("message", "providerServices of such provider deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "providerService deletion failed").build();
		}
	}
	
	//BUSINESS
	@GetMapping("/provider/{id}/providerServices/availability/{status}")
	public ResponseEntity<Void> setAvailability(@PathVariable("id") long id, @PathVariable("status") boolean status) {
		try {
			if (providerService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
			
			providerServiceService.setAvailability(id, status);
			return ResponseEntity.noContent().header("message", "providerServices of such provider has been set to " + status).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "providerService of such provider failed to update availability status").build();
		}
	}
}
