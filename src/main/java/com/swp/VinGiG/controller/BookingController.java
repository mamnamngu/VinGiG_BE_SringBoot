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
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.BookingService;
import com.swp.VinGiG.service.BuildingService;
import com.swp.VinGiG.service.CustomerService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.service.ProviderServiceService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.BookingObject;

@RestController
public class BookingController {
	@Autowired
	private BookingService bookingService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private ProviderServiceService providerServiceService;

	@GetMapping("/bookings")
	public ResponseEntity<List<BookingObject>> retreiveAllBookings() {
		List<Booking> ls = bookingService.findAll();
		List<BookingObject> list = bookingService.display(ls);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/booking/{id}")
	public ResponseEntity<BookingObject> retrieveBooking(@PathVariable int id) {
		Booking booking = bookingService.findById(id);
		if (booking != null) {
			List<Booking> ls = new ArrayList<>();
			ls.add(booking);
			List<BookingObject> list = bookingService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/booking/date/{date}/status/{status}")
	public ResponseEntity<List<BookingObject>> retrieveBooking(@PathVariable("date") String dateStr,
			@PathVariable("status") Integer status) {
		Date date = Constants.strToDate(dateStr);	
		try {
			List<Booking> ls = bookingService.findByDate(date, status);
			List<BookingObject> list = bookingService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// FIND CURRENT ACTIVITY
	// Customer
	@GetMapping("/customer/{id}/bookings/currentActivity")
	public ResponseEntity<List<BookingObject>> findByCustomerIDGoingOn(@PathVariable("id") long id) {
		Customer customer = customerService.findById(id);
		if (customer == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		
		List<Booking> ls = bookingService.findByCustomerIDGoingOn(id);
		List<BookingObject> list = bookingService.display(ls);	
		return ResponseEntity.ok(list);
	}

	// Provider
	@GetMapping("/provider/{id}/bookings/currentActivity")
	public ResponseEntity<List<BookingObject>> findByProviderIDGoingOn(@PathVariable("id") long id) {
		Provider provider = providerService.findById(id);
		if (provider == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		List<Booking> ls = bookingService.findByProviderIDGoingOn(id);
		List<BookingObject> list = bookingService.display(ls);
		return ResponseEntity.ok(list);
	}

	// BOOKING HISTORY
	// Customer
	@GetMapping("/customer/{id}/bookings/history/{dateMin}/{dateMax}")
	public ResponseEntity<List<BookingObject>> findByCustomerIDByDateInterval(@PathVariable("id") long id,
			@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);

		Customer customer = customerService.findById(id);
		if (customer == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such ID").build();
		
		List<Booking> ls = bookingService.findByCustomerIDByDateInterval(id, dateMin, dateMax);
		List<BookingObject> list = bookingService.display(ls);
		return ResponseEntity.ok(list);
	}

	// Provider
	@GetMapping("/provider/{id}/bookings/history/{dateMin}/{dateMax}")
	public ResponseEntity<List<BookingObject>> findByProServiceIDByDateInterval(@PathVariable("id") long id,
			@PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr) {
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);

		Provider provider = providerService.findById(id);
		if (provider == null)
			return ResponseEntity.notFound().header("message", "No Provider found for such ID").build();
		
		List<Booking> ls = bookingService.findByProServiceIDByDateInterval(id, dateMin, dateMax);
		List<BookingObject> list = bookingService.display(ls);
		return ResponseEntity.ok(list);
	}

	// DISPLAY REVIEW
	@GetMapping("/providerService/{id}/bookings/reviews")
	public ResponseEntity<List<BookingObject>> displayReviewByProServiceID(@PathVariable("id") long id) {
		com.swp.VinGiG.entity.ProviderService providerService = providerServiceService.findById(id);
		if (providerService == null)
			return ResponseEntity.notFound().header("message", "No Provider Service found for such ID").build();
		
		List<Booking> ls = bookingService.reviewsByProServiceID(id);
		List<BookingObject> list = bookingService.display(ls);
		return ResponseEntity.ok(list);
	}

	// WEEKLY UPDATE RATINGS AND BOOKING NO
	// Provider Service
	@GetMapping("/providerServices/bookings/updateReview")
	public ResponseEntity<Void> weeklyProviderServiceRatingUpdate() {
		try {
			bookingService.weeklyProviderServiceRatingUpdate();
			return ResponseEntity.noContent().header("message", "Update review successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "Failed to update review").build();
		}
	}

	// Provider Service
	@GetMapping("/customers/bookings/updateReview")
	public ResponseEntity<Void> weeklyCustomerRatingUpdate() {
		try {
			bookingService.weeklyCustomerRatingUpdate();
			return ResponseEntity.noContent().header("message", "Update review successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "Failed to update review").build();
		}
	}

	// Provider Service
	@GetMapping("/providers/bookings/updateReview")
	public ResponseEntity<Void> weeklyProviderRatingUpdate() {
		try {
			bookingService.weeklyProviderRatingUpdate();
			return ResponseEntity.noContent().header("message", "Update review successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "Failed to update review").build();
		}
	}

	// CREATE BOOKING
	@PostMapping("building/{buildingID}/customer/{customerID}/providerService/{proServiceID}/booking")
	public ResponseEntity<Booking> placeBooking(@PathVariable("buildingID") int buildingID,
			@PathVariable("customerID") long customerID, @PathVariable("proServiceID") long proServiceID,
			@RequestBody Booking booking) {
		try {
			if (bookingService.findById(booking.getBookingID()) != null)
				return ResponseEntity.badRequest().header("message", "Booking with such ID already exists").build();

			Building building = buildingService.findById(buildingID);
			if (building == null)
				return ResponseEntity.notFound().header("message", "No Building found for such buildingID").build();

			Customer customer = customerService.findById(customerID);
			if (customer == null)
				return ResponseEntity.notFound().header("message", "No Customer found for such customerID").build();

			com.swp.VinGiG.entity.ProviderService providerService = providerServiceService.findById(proServiceID);
			if (providerService == null)
				return ResponseEntity.notFound().header("message", "No Provider Service found for such proServiceID")
						.build();

			booking.setBuilding(building);
			booking.setCustomer(customer);
			booking.setProviderService(providerService);
			booking.setDate(Constants.currentDate());

			Booking savedBooking = bookingService.placeBooking(booking);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("message", "Failed to add new booking").build();
		}
	}

	@PutMapping("building/{buildingID}/customer/{customerID}/providerService/{proServiceID}/booking")
	public ResponseEntity<Booking> updateBooking(@PathVariable("buildingID") int buildingID,
			@PathVariable("customerID") long customerID, @PathVariable("proServiceID") long proServiceID,
			@RequestBody Booking booking) {
		if (bookingService.findById(booking.getBookingID()) == null)
			return ResponseEntity.badRequest().header("message", "No Booking with such ID found").build();

		Building building = buildingService.findById(buildingID);
		if (building == null)
			return ResponseEntity.notFound().header("message", "No Building found for such buildingID").build();

		Customer customer = customerService.findById(customerID);
		if (customer == null)
			return ResponseEntity.notFound().header("message", "No Customer found for such customerID").build();

		com.swp.VinGiG.entity.ProviderService providerService = providerServiceService.findById(proServiceID);
		if (providerService == null)
			return ResponseEntity.notFound().header("message", "No Provider Service found for such proServiceID")
					.build();

		booking.setBuilding(building);
		booking.setCustomer(customer);
		booking.setProviderService(providerService);

		Booking updatedBooking = bookingService.update(booking);
		if (updatedBooking != null)
			return ResponseEntity.ok(updatedBooking);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PutMapping("/providerService/{proServiceID}/booking/{bookingID}/action/{action}/total/{total}")
	public ResponseEntity<Booking> acceptBooking(@PathVariable("proServiceID") long proServiceID,
			@PathVariable("bookingID") long bookingID, @PathVariable("action") String action,
			@PathVariable(name = "total", required = false) Long total) {
		try {
			com.swp.VinGiG.entity.ProviderService providerService = providerServiceService.findById(proServiceID);
			if (providerService == null)
				return ResponseEntity.notFound().header("message", "No Provider Service found for such proServiceID")
						.build();

			Booking booking = bookingService.findById(bookingID);
			if (booking == null)
				return ResponseEntity.notFound().header("message", "No Booking found for such proServiceID").build();

			if (booking.getProviderService().getProServiceID() != providerService.getProServiceID())
				return ResponseEntity.badRequest()
						.header("message", "This Provider Service does not have the authority to accept this Booking")
						.build();

			// ACTIONS
			Booking temp = null;
			switch (action) {
			case Constants.BOOKING_ACCEPT:
				temp = bookingService.acceptBooking(booking);
				if(temp == null) return ResponseEntity.badRequest().header("message", "Failed to make a transaction to thr provider's account").build();
				break;

			case Constants.BOOKING_DECLINE:
				temp = bookingService.declineBooking(booking);
				break;

			case Constants.BOOKING_COMPLETE:
				temp = bookingService.completeBooking(booking, total);
				break;
				
			case Constants.BOOKING_CANCEL:
				temp = bookingService.cancelBooking(booking);
				break;
			}
			if (temp != null)
				return ResponseEntity.ok(temp);
			else
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// CUSTOMER REVIEW
	@PutMapping("/customer/{customerID}/booking/{bookingID}/content/{content}/rating/{rating}")
	public ResponseEntity<Booking> reviewBookingByCustomerID(@PathVariable("customerID") long customerID,
			@PathVariable("bookingID") long bookingID, @PathVariable("content") String content,
			@PathVariable("rating") Integer rating) {
		try {
			Customer customer = customerService.findById(customerID);
			if (customer == null)
				return ResponseEntity.notFound().header("message", "No Customer found for such customerID").build();

			Booking booking = bookingService.findById(bookingID);
			if (booking == null)
				return ResponseEntity.notFound().header("message", "No Booking found for such proServiceID").build();

			if (customer.getCustomerID() != booking.getCustomer().getCustomerID()
					|| booking.getStatus() != Constants.BOOKING_STATUS_COMPLETED)
				return ResponseEntity.badRequest()
						.header("message", "Customer with such ID has no permission to leave a review on this booking!")
						.build();

			Booking rvBooking = bookingService.reviewBookingByCustomerID(customerID, booking, content, rating);
			if (rvBooking != null)
				return ResponseEntity.ok(rvBooking);
			else
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// PROVIDER REVIEW
	@PutMapping("/provider/{providerID}/booking/{bookingID}/content/{content}/rating/{rating}")
	public ResponseEntity<Booking> reviewBookingByProviderID(@PathVariable("providerID") long providerID,
			@PathVariable("bookingID") long bookingID, @PathVariable("content") String content,
			@PathVariable("rating") Integer rating) {
		try {
			Provider provider = providerService.findById(providerID);
			if (provider == null)
				return ResponseEntity.notFound().header("message", "No Provider found for such providerID").build();

			Booking booking = bookingService.findById(bookingID);
			if (booking == null)
				return ResponseEntity.notFound().header("message", "No Booking found for such proServiceID").build();

			if (provider.getProviderID() != booking.getProviderService().getProvider().getProviderID()
					|| booking.getStatus() != Constants.BOOKING_STATUS_COMPLETED)
				return ResponseEntity.badRequest()
						.header("message", "Provider with such ID has no permission to leave a review on this booking!")
						.build();

			Booking rvBooking = bookingService.reviewBookingByCustomerID(providerID, booking, content, rating);
			if (rvBooking != null)
				return ResponseEntity.ok(rvBooking);
			else
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/booking/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
		try {
			if (bookingService.findById(id) == null)
				return ResponseEntity.badRequest().header("message", "No Booking with such ID found").build();

			bookingService.delete(id);
			return ResponseEntity.noContent().header("message", "booking deleted successfully").build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "booking deletion failed")
					.build();
		}
	}
}
