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
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BookingFeeService;
import com.swp.VinGiG.service.BookingService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.BookingFeeObject;

@RestController
public class BookingFeeController {

	@Autowired
	private BookingFeeService bookingFeeService;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/bookingFees")
	public ResponseEntity<List<BookingFeeObject>> retrieveAllBookingFees() {
		List<BookingFee> ls = bookingFeeService.findAll();
		List<BookingFeeObject> list = bookingFeeService.display(ls);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/bookingFee/{id}")
	public ResponseEntity<BookingFeeObject> retrieveBookingFee(@PathVariable int id) {
		BookingFee bookingFee = bookingFeeService.findById(id);
		if (bookingFee != null) {
			List<BookingFee> ls = new ArrayList<>();
			ls.add(bookingFee);
			List<BookingFeeObject> list = bookingFeeService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// admin
	@GetMapping("/bookingFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<BookingFeeObject>> findByDateInterval(@PathVariable("dateMin") String dateMinStr,
			@PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<BookingFee> ls = bookingFeeService.findByDateInterval(dateMin, dateMax);
		List<BookingFeeObject> list = bookingFeeService.display(ls);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/bookingFees/date/{date}")
	public ResponseEntity<List<BookingFeeObject>> findByDate(@PathVariable("date") String dateStr) {
		Date date = Constants.strToDate(dateStr);

		List<BookingFee> ls = bookingFeeService.findByDate(date);
		List<BookingFeeObject> list = bookingFeeService.display(ls);
		return ResponseEntity.ok(list);
	}

	// admin
	@GetMapping("/booking/{id}/bookingFees")
	public ResponseEntity<List<BookingFeeObject>> findByBookingID(@PathVariable("id") long id) {
		Booking booking = bookingService.findById(id);
		if (booking != null) {
			List<BookingFee> ls = bookingFeeService.findByBookingBookingID(id);
			List<BookingFeeObject> list = bookingFeeService.display(ls);
			return ResponseEntity.ok(list);
		} else
			return ResponseEntity.notFound().header("message", "No Booking found for such ID").build();
	}

	// provider
	@GetMapping("/provider/{id}/bookingFees/date/{dateMin}/{dateMax}")
	public ResponseEntity<List<BookingFeeObject>> findByProviderIDDateInterval(@PathVariable("id") long id,
			@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		Provider provider = providerService.findById(id);
		if (provider != null) {
			List<BookingFee> ls = bookingFeeService.findByProviderIDDateInterval(id, dateMin, dateMax);
			List<BookingFeeObject> list = bookingFeeService.display(ls);
			return ResponseEntity.ok(list);
		} else
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
	}

	@PostMapping("/booking/{bookingID}/bookingFee")
	public ResponseEntity<BookingFee> createBookingFee(@PathVariable("bookingID") long bookingID, @RequestBody BookingFee bookingFee) {
		try {
			Booking booking = bookingService.findById(bookingID);
			if (booking == null)
				return ResponseEntity.notFound().header("message", "No Booking found for such bookingID").build();

			if (bookingFeeService.findById(bookingFee.getBookingFeeID()) != null)
				return ResponseEntity.notFound().header("message", "BookingFee with such ID already exists").build();

			bookingFee.setBooking(booking);
			BookingFee savedBookingFee = bookingFeeService.add(bookingFee);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBookingFee);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new bookingFee").build();
		}
	}

	@PutMapping("/booking/{bookingID}/bookingFee")
	public ResponseEntity<BookingFee> updateBookingFee(@PathVariable("bookingID") long bookingID, @RequestBody BookingFee bookingFee) {
		try {
			if (bookingFeeService.findById(bookingFee.getBookingFeeID()) == null)
				return ResponseEntity.notFound().header("message", "No BookingFee found for such ID").build();

			Booking booking = bookingService.findById(bookingID);
			if (booking == null)
				return ResponseEntity.notFound().header("message", "No Booking found for such bookingID").build();
			
			bookingFee.setBooking(booking);
			BookingFee updatedBookingFee = bookingFeeService.update(bookingFee);
			
			if (updatedBookingFee != null) 
				return ResponseEntity.ok(updatedBookingFee);
			else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();		
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update bookingFee").build();
		}
	}

	@DeleteMapping("/bookingFee/{id}")
	public ResponseEntity<Void> deleteBookingFee(@PathVariable long id) {
		try {
			if (bookingFeeService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No BookingFee found for such ID").build();
			
			bookingFeeService.delete(id);
			return ResponseEntity.noContent().header("message", "bookingFee deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "bookingFee deletion failed").build();
		}
	}
}
