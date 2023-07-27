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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.Transction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.service.BookingFeeService;
import com.swp.VinGiG.service.DepositService;
import com.swp.VinGiG.service.SubscriptionFeeService;
import com.swp.VinGiG.service.TransactionService;
import com.swp.VinGiG.service.WalletService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.TransactionObject;

@RestController
public class TransctionController {
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private BookingFeeService bookingFeeService;
	
	@Autowired
	private SubscriptionFeeService subscriptionFeeService;
	
	@GetMapping("/transactions")
	public ResponseEntity<List<TransactionObject>> retrieveAllTransactions(){
		List<Transction> ls = transactionService.findAll();
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/transaction/{id}")
	public ResponseEntity<TransactionObject> retrieveTransaction(@PathVariable long id) {
		Transction transction = transactionService.findById(id);
		if(transction != null) {
			List<Transction> ls = new ArrayList<>();
			ls.add(transction);
			List<TransactionObject> list = transactionService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Admin
	@GetMapping("/transaction/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findByDateInterval(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByDateInterval(dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);	
	}
	
	@GetMapping("/transaction/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findTypeDepositDateInterval(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findTypeDepositDateInterval(dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/deposit/date/{dateMin}/{dateMax}/total")
	public ResponseEntity<Long> findTypeDepositDateIntervalTotal(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findTypeDepositDateInterval(dateMin, dateMax);
		long total = 0;
		for(Transction x: ls)
			total += x.getAmount();
		
		return ResponseEntity.status(HttpStatus.OK).body(total);
	}
	
	@GetMapping("/transaction/bookingFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findTypeBookingFeeDateInterval(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findTypeBookingFeeDateInterval(dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/bookingFee/date/{dateMin}/{dateMax}/total")
	public ResponseEntity<Long> findTypeBookingFeeDateIntervalTotal(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findTypeBookingFeeDateInterval(dateMin, dateMax);
		long total = 0;
		for(Transction x: ls)
			total += x.getAmount();
		
		return ResponseEntity.status(HttpStatus.OK).body(total);
	}
	
	@GetMapping("/transaction/subscriptionFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findBySubscriptionFeeDateInterval(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByTypeSubscriptionFeeDateInterval(dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/subscriptionFee/date/{dateMin}/{dateMax}/total")
	public ResponseEntity<Long> findBySubscriptionFeeDateIntervalTotal(@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByTypeSubscriptionFeeDateInterval(dateMin, dateMax);
		long total = 0;
		for(Transction x: ls)
			total += x.getAmount();
		return ResponseEntity.status(HttpStatus.OK).body(total);
	}
	
	//Provider
	@GetMapping("/transaction/provider/{id}/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findByProviderIDDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByProviderIDDateInterval(id, dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/provider/{id}/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findByProviderIDTypeDepositDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByProviderIDTypeDepositDateInterval(id, dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/provider/{id}/bookingFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findByProviderIDTypeBookingFeeDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByProviderIDTypeBookingFeeDateInterval(id, dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/transaction/provider/{id}/subscriptionFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<TransactionObject>> findByProviderIDBySubscriptionFeeDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Transction> ls = transactionService.findByProviderIDBySubscriptionFeeDateInterval(id, dateMin, dateMax);
		List<TransactionObject> list = transactionService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@PostMapping("/walletID/{walletID}/type/{type}/{id}/transaction")
	public ResponseEntity<Transction> createTransaction(@PathVariable("walletID") long walletID,@PathVariable("type") String type, @PathVariable("id") long id, @RequestBody Transction transction){
		try {
			Wallet wallet = walletService.findById(walletID);
			if(wallet == null) return ResponseEntity.notFound().header("message", "Wallet not found. Adding failed").build();
			transction.setWallet(wallet);
			if(type.equalsIgnoreCase(Constants.TRANSACTION_DEPOSIT)) {
				Deposit deposit = depositService.findById(id);
				if(deposit == null)
					return ResponseEntity.notFound().header("message", "No Deposit found with such ID").build();
				transction.setDeposit(deposit);
				transction.setAmount(deposit.getAmount());
				
			}else if(type.equalsIgnoreCase(Constants.TRANSACTION_BOOKINGFEE)) {
				BookingFee bookingFee = bookingFeeService.findById(id);
				if(bookingFee == null)
					return ResponseEntity.notFound().header("message", "No Booking Fee found with such ID").build();
				transction.setBookingFee(bookingFee);
				transction.setAmount(bookingFee.getAmount()*-1);
				
			}else if(type.equalsIgnoreCase(Constants.TRANSACTION_DEPOSIT)) {
				SubscriptionFee subscriptionFee = subscriptionFeeService.findById(id);
				if(subscriptionFee == null)
					return ResponseEntity.notFound().header("message", "No Subscription Fee found with such ID").build();
				transction.setSubscriptionFee(subscriptionFee);
				transction.setAmount(subscriptionFee.getPlan().getPrice()*-1);	
			}
			
			Transction savedTransaction = transactionService.add(transction);
			if(savedTransaction != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new transaction").build();
		}
	}
	
	
	//admin
	@DeleteMapping("/transaction/{id}")
	public ResponseEntity<Void> deleteTransaction(@PathVariable long id){
		try{
			transactionService.delete(id);
			return ResponseEntity.noContent().header("message", "Transaction deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Transaction deletion failed").build();
		}
	}
}
