package com.swp.VinGiG.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.service.WalletService;

@Controller
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/wallets")
	public ResponseEntity<List<Wallet>> retrieveAllWallets(){
		return ResponseEntity.ok(walletService.findAll());
    }
	
	@GetMapping("/wallet/{id}")
	public ResponseEntity<Wallet> retrieveWallet(@PathVariable long id) {
		Wallet wallet = walletService.findById(id);
		if(wallet != null) {
			return ResponseEntity.status(HttpStatus.OK).body(wallet);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/wallets")
	public ResponseEntity<List<Wallet>> retrieveAllWalletsOfCategory(@PathVariable long id){
		Provider provider = providerService.findById(id);
		if(provider != null)
			return ResponseEntity.ok(walletService.findByProviderId(id));
		else return ResponseEntity.notFound().build();
    }
	
	@GetMapping("/provider/balanceBelowThreshold")
	public ResponseEntity<List<Wallet>> findByBalanceBelowThreshold(){
		return ResponseEntity.ok(walletService.findByBalanceBelowThreshold());
	}
	
	@GetMapping("/wallets/createDate/{dateMin}/{dateMax}")
	public ResponseEntity<List<Wallet>> findByCreateDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax){
		return ResponseEntity.ok(walletService.findByCreateDateInterval(dateMin,dateMax));
	}
	
	@GetMapping("/provider/balance/{lower}/{upper}")
	public ResponseEntity<List<Wallet>> findByBalanceInterval(@PathVariable("lower") long lower, @PathVariable("upper") long upper){
		return ResponseEntity.ok(walletService.findByBalanceInterval(lower,upper));
	}
	
	@PostMapping("/provider/{id}/wallet")
	public ResponseEntity<Wallet> createWallet(@PathVariable long id, @RequestBody Wallet wallet){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "Provider not found. Adding failed").build();
			
			wallet.setProvider(provider);
			Wallet savedWallet = walletService.add(wallet);
			if(savedWallet != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedWallet);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new wallet").build();
		}
	}
	
	@PutMapping("/provider/{id}/wallet")
	public ResponseEntity<Wallet> updateWallet(@PathVariable long id, @RequestBody Wallet wallet){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "Provider not found. Update failed").build();
			
			if(walletService.findById(wallet.getWalletID()) == null) return ResponseEntity.notFound().header("message", "Wallet with such ID not found. Update failed").build();
			
			wallet.setProvider(provider);
			Wallet savedWallet = walletService.update(wallet);
			if(savedWallet != null)
				return ResponseEntity.status(HttpStatus.OK).body(savedWallet);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update wallet").build();
		}
	}
	
	@DeleteMapping("/wallet/{id}")
	public ResponseEntity<Void> deleteWallet(@PathVariable long id){
		try{
			walletService.delete(id);
			return ResponseEntity.noContent().header("message", "Wallet deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Wallet deletion failed").build();
		}
	}
}
