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
import com.swp.VinGiG.service.BookingService;

@RestController
public class BookingController {
	@Autowired
	private BookingService bookingService;
	
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
	
	@GetMapping("/booking/{date}")
	public ResponseEntity<List<Booking>> retrieveBooking(@PathVariable Date date) {
		List<Booking> booking = bookingService.findByDate(date);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(booking);
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
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
