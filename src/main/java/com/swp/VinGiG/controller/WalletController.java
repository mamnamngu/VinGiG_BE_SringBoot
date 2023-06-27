package com.swp.VinGiG.controller;

import java.util.ArrayList;
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
import com.swp.VinGiG.view.WalletObject;

@Controller
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	@Autowired
	private ProviderService providerService;
	
	//Admin
	@GetMapping("/wallets")
	public ResponseEntity<List<WalletObject>> retrieveAllWallets(){
		List<Wallet> ls = walletService.findAll();
		List<WalletObject> list = walletService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/wallets/deleted")
	public ResponseEntity<List<WalletObject>> retrieveAllDeletedWallets(){
		List<Wallet> ls = walletService.findDeletedWallet();
		List<WalletObject> list = walletService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/wallet/{id}")
	public ResponseEntity<WalletObject> retrieveWallet(@PathVariable long id) {
		Wallet wallet = walletService.findById(id);
		if(wallet != null) {
			List<Wallet> ls = new ArrayList<>();
			ls.add(wallet);
			List<WalletObject> list = walletService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/wallets")
	public ResponseEntity<List<WalletObject>> findByProviderID(@PathVariable long id){
		Provider provider = providerService.findById(id);
		if(provider != null) {
			List<Wallet> ls = walletService.findByProviderId(id);
			List<WalletObject> list = walletService.display(ls);
			return ResponseEntity.ok(list);
		}else return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
    }
	
	@GetMapping("/provider/balanceBelowThreshold")
	public ResponseEntity<List<WalletObject>> findByBalanceBelowThreshold(){
		List<Wallet> ls = walletService.findByBalanceBelowThreshold();
		List<WalletObject> list = walletService.display(ls);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/wallets/createDate/{dateMin}/{dateMax}")
	public ResponseEntity<List<WalletObject>> findByCreateDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax){
		List<Wallet> ls = walletService.findByCreateDateInterval(dateMin,dateMax);
		List<WalletObject> list = walletService.display(ls);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/wallets/balance/{lower}/{upper}")
	public ResponseEntity<List<WalletObject>> findByBalanceInterval(@PathVariable("lower") long lower, @PathVariable("upper") long upper){
		List<Wallet> ls = walletService.findByBalanceInterval(lower,upper);
		List<WalletObject> list = walletService.display(ls);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/provider/{id}/wallet")
	public ResponseEntity<Wallet> createWallet(@PathVariable long id, @RequestBody Wallet wallet){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "Provider not found. Adding failed").build();
			
			if(walletService.findById(wallet.getWalletID()) != null)
				return ResponseEntity.badRequest().header("message", "Wallet with such ID already exists").build();
			
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
			if(walletService.findById(id) == null) return ResponseEntity.notFound().header("message", "Wallet with such ID not found. Update failed").build();
			
			walletService.delete(id);
			return ResponseEntity.noContent().header("message", "Wallet deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Wallet deletion failed").build();
		}
	}
}
