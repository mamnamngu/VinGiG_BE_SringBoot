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

import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.DepositService;

public class DepositController {
	@Autowired
	private DepositService depositService;
	
	
	@GetMapping("/deposits")
	public ResponseEntity<List<Deposit>> retrieveAllDeposits(){
		return ResponseEntity.ok(depositService.findAll());
    }
	
	@GetMapping("/deposit/{id}")
	public ResponseEntity<Deposit> retrieveDeposit(@PathVariable int id) {
		Deposit deposit = depositService.findById(id);
		if(deposit != null) {
			return ResponseEntity.status(HttpStatus.OK).body(deposit);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/deposit/{dateMin}/{dateMax}")
	public ResponseEntity<List<Deposit>> findByProviderIDInterval(@PathVariable long id, @PathVariable Date dateMin, @PathVariable Date dateMax ){

		List<Deposit> ls = depositService.findByProviderIDInterval(id,dateMin, dateMax);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	
	}
	@GetMapping("/deposit/{dateMin}/{dateMax}")
	public ResponseEntity<List<Deposit>> findByDateInterval( @PathVariable Date dateMin, @PathVariable Date dateMax ){

		List<Deposit> ls = depositService.findByDateInterval(dateMin, dateMax);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	
	}
	@GetMapping("/deposit/{method}")
	public ResponseEntity<List<Deposit>> findByfindByMethod(@PathVariable String method){
		List<Deposit> ls = depositService.findByMethod(method);
		if(ls.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(ls);
		else 
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<Deposit> createBuilding(@RequestBody Deposit deposit){
		try {
			Deposit saveDeposit = depositService.add(deposit);
			return ResponseEntity.status(HttpStatus.CREATED).body(saveDeposit);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new Deposit").build();
		}
	}
	
	@PutMapping("/deposit")
	public ResponseEntity<Deposit> updateBuilding(@RequestBody Deposit deposit){
		if(depositService.findById(deposit.getDepositID()) == null)
			return ResponseEntity.notFound().header("message", "No Deposit found for such ID").build();
		
		Deposit updatedDeposit = depositService.update(deposit);
		if(updatedDeposit!= null)
			return ResponseEntity.ok(updatedDeposit);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/deposit/{id}")
	public ResponseEntity<Void> deleteDeposit(@PathVariable int id){
		try{
			depositService.delete(id);
			return ResponseEntity.noContent().header("message", "deposit deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "deposit deletion failed").build();
		}
	}
}
