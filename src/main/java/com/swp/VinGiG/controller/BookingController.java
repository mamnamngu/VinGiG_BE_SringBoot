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

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BookingService;
import com.swp.VinGiG.service.CustomerService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;

@RestController
public class BookingController {
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/bookings")
	public List<Booking> retreiveAllBookings(){
		return bookingService.findAll();
	}
	
	@GetMapping("/booking/{id}")
	public ResponseEntity<Booking> retrieveBooking(@PathVariable int id) {
		Booking booking = bookingService.findById(id);
		if(booking != null) {
			return ResponseEntity.status(HttpStatus.OK).body(booking);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/booking/date/{date}/status/{status}")
	public ResponseEntity<List<Booking>> retrieveBooking(@PathVariable("date") Date date, @PathVariable("status") Integer status) {
		List<Booking> booking = bookingService.findByDate(date, status);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(booking);
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	//FIND CURRENT ACTIVITY
	//Customer
	@GetMapping("/customer/{id}/providerServices/currentActivity")
	public ResponseEntity<List<Booking>> findByCustomerIDGoingOn(@PathVariable("id") long id){
		Customer customer = customerService.findById(id);
		if(customer == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		return ResponseEntity.ok(bookingService.findByCustomerIDGoingOn(id));
	}
	
	//Provider
		@GetMapping("/provider/{id}/providerServices/currentActivity")
		public ResponseEntity<List<Booking>> findByProviderIDGoingOn(@PathVariable("id") long id){
			Provider provider = providerService.findById(id);
			if(provider == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
			return ResponseEntity.ok(bookingService.findByProviderIDGoingOn(id));
		}
	
	//BOOKING HISTORY
	//Customer
	@GetMapping("/customer/{id}/providerServices/history/{dateMin}/{dateMax}")
	public ResponseEntity<List<Booking>> findByCustomerIDByDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		Customer customer = customerService.findById(id);
		if(customer == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		return ResponseEntity.ok(bookingService.findByCustomerIDByDateInterval(id, dateMin, dateMax));
	}
	
	//Provider
	@GetMapping("/provider/{id}/providerServices/history/{dateMin}/{dateMax}")
	public ResponseEntity<List<Booking>> findByProServiceIDByDateInterval(@PathVariable("id") long id, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		Provider provider = providerService.findById(id);
		if(provider == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		return ResponseEntity.ok(bookingService.findByProServiceIDByDateInterval(id, dateMin, dateMax));
	}
	
	//CREATE BOOKING	
	@PostMapping("building/{buildingID}/customer/{customerID}/providerService/{proServiceID}/booking")
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking){
		try {
			Booking savedBooking = bookingService.add(booking);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new booking").build();
		}
	}
	
	@PutMapping("/booking")
	public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking){
		Booking updatedBooking = bookingService.update(booking);
		if(updatedBooking != null)
			return ResponseEntity.ok(updatedBooking);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/booking/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable int id){
		try{
			bookingService.delete(id);
			return ResponseEntity.noContent().header("message", "booking deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "booking deletion failed").build();
		}
	}
}
