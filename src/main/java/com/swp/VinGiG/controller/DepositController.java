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

import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.DepositService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.DepositObject;

public class DepositController {
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private ProviderService providerService;
	
	
	@GetMapping("/deposits")
	public ResponseEntity<List<DepositObject>> retrieveAllDeposits(){
		List<Deposit> ls = depositService.findAll();
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/deposit/{id}")
	public ResponseEntity<DepositObject> retrieveDeposit(@PathVariable int id) {
		Deposit deposit = depositService.findById(id);
		if(deposit != null) {
			List<Deposit> ls = new ArrayList<>();
			ls.add(deposit);
			List<DepositObject> list = depositService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<DepositObject>> findByProviderIDInterval(@PathVariable long id, @PathVariable String dateMinStr, @PathVariable String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByProviderIDDateInterval(id,dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<DepositObject>> findByDateInterval( @PathVariable String dateMinStr, @PathVariable String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByDateInterval(dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/deposit/method/{method}/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<DepositObject>> findByfindByMethod(@PathVariable("method") String method, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByMethod(method, dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@PostMapping("/provider/{id}/deposit")
	public ResponseEntity<Deposit> createDeposit(@PathVariable("id") long id, @RequestBody Deposit deposit){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "No Provider with such ID found").build();
			
			if(depositService.findById(deposit.getDepositID()) != null)
				return ResponseEntity.badRequest().header("message", "Deposit with such ID already exists.").build();
			
			deposit.setProvider(provider);
			Deposit saveDeposit = depositService.add(deposit);
			return ResponseEntity.status(HttpStatus.CREATED).body(saveDeposit);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new Deposit").build();
		}
	}
	
	@PutMapping("/provider/{id}/deposit")
	public ResponseEntity<Deposit> updateDeposit(@PathVariable("id") long id, @RequestBody Deposit deposit){
		if(depositService.findById(deposit.getDepositID()) == null)
			return ResponseEntity.notFound().header("message", "No Deposit found for such ID").build();
		
		Provider provider = providerService.findById(id);
		if(provider == null) return ResponseEntity.notFound().header("message", "No Provider with such ID found").build();

		deposit.setProvider(provider);
		Deposit updatedDeposit = depositService.update(deposit);
		if(updatedDeposit!= null)
			return ResponseEntity.ok(updatedDeposit);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/deposit/{id}")
	public ResponseEntity<Void> deleteDeposit(@PathVariable int id){
		try{
			if(depositService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Deposit found for such ID").build();
			
			depositService.delete(id);
			return ResponseEntity.noContent().header("message", "deposit deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "deposit deletion failed").build();
		}
	}
}
