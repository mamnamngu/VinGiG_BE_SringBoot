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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.SubscriptionFee;
import com.swp.VinGiG.entity.Transaction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.service.BookingFeeService;
import com.swp.VinGiG.service.DepositService;
import com.swp.VinGiG.service.SubscriptionFeeService;
import com.swp.VinGiG.service.TransactionService;
import com.swp.VinGiG.service.WalletService;
import com.swp.VinGiG.utilities.Constants;

@RestController
public class TransactionController {
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
	public ResponseEntity<List<Transaction>> retrieveAllTransactions(){
		return ResponseEntity.ok(transactionService.findAll());
    }
	
	@GetMapping("/transaction/{id}")
	public ResponseEntity<Transaction> retrieveTransaction(@PathVariable long id) {
		Transaction transaction = transactionService.findById(id);
		if(transaction != null) {
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Admin
	@GetMapping("/transaction/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findByDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findByDateInterval(dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction found within such Date interval").build();
	}
	
	@GetMapping("/transaction/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findTypeDepositDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findTypeDepositDateInterval(dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction found for Deposit within such Date interval").build();
	}
	
	@GetMapping("/transaction/bookingFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findTypeBookingFeeDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findTypeBookingFeeDateInterval(dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction found for Booking Fee within such Date interval").build();
	}
	
	@GetMapping("/transaction/subscriptionFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findBySubscriptionFeeDateInterval(@PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findBySubscriptionFeeDateInterval(dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction found for Subscription Fee within such Date interval").build();
	}
	
	//Provider
	@GetMapping("/transaction/provider/{id}/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findByProviderIDDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findByProviderIDDateInterval(id, dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction for such Provider found within such Date interval").build();
	}
	
	@GetMapping("/transaction/provider/{id}/deposit/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findByProviderIDTypeDepositDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findByProviderIDTypeDepositDateInterval(id, dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction for Deposit for such Provider found within such Date interval").build();
	}
	
	@GetMapping("/transaction/provider/{id}/bookingFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findByProviderIDTypeBookingFeeDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findByProviderIDTypeBookingFeeDateInterval(id, dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction for Booking Fee for such Provider found within such Date interval").build();
	}
	
	@GetMapping("/transaction/provider/{id}/subscriptionFee/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<Transaction>> findByProviderIDBySubscriptionFeeDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") Date dateMin, @PathVariable("dateMax") Date dateMax) {
		List<Transaction> transaction = transactionService.findByProviderIDBySubscriptionFeeDateInterval(id, dateMin, dateMax);
		if(transaction.size() > 0)
			return ResponseEntity.status(HttpStatus.OK).body(transaction);
		return ResponseEntity.notFound().header("message", "No Transaction for Subscription Fee for such Provider found within such Date interval").build();
	}
	
	@PostMapping("/walletID/{walletID}/type/{type}/{id}/transaction")
	public ResponseEntity<Transaction> createTransaction(@PathVariable("walletID") long walletID,@PathVariable("type") String type, @PathVariable("id") long id, @RequestBody Transaction transaction){
		try {
			Wallet wallet = walletService.findById(walletID);
			if(wallet == null) return ResponseEntity.notFound().header("message", "Wallet not found. Adding failed").build();
			transaction.setWallet(wallet);
			if(type.equalsIgnoreCase(Constants.TRANSACTION_DEPOSIT)) {
				Deposit deposit = depositService.findById(id);
				if(deposit == null)
					return ResponseEntity.notFound().header("message", "No Deposit found with such ID").build();
				transaction.setDeposit(deposit);
				transaction.setAmount(deposit.getAmount());
				
			}else if(type.equalsIgnoreCase(Constants.TRANSACTION_BOOKINGFEE)) {
				BookingFee bookingFee = bookingFeeService.findById(id);
				if(bookingFee == null)
					return ResponseEntity.notFound().header("message", "No Booking Fee found with such ID").build();
				transaction.setBookingFee(bookingFee);
				transaction.setAmount(bookingFee.getAmount()*-1);
				
			}else if(type.equalsIgnoreCase(Constants.TRANSACTION_DEPOSIT)) {
				SubscriptionFee subscriptionFee = subscriptionFeeService.findById(id);
				if(subscriptionFee == null)
					return ResponseEntity.notFound().header("message", "No Subscription Fee found with such ID").build();
				transaction.setSubscriptionFee(subscriptionFee);
				transaction.setAmount(subscriptionFee.getPlan().getPrice()*-1);	
			}
			
			Transaction savedTransaction = transactionService.add(transaction);
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
