package com.swp.VinGiG.controller;

import java.util.ArrayList;
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

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.BookingMessage;
import com.swp.VinGiG.service.BookingMessageService;
import com.swp.VinGiG.service.BookingService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.BookingMessageBox;

@RestController
public class BookingMessageController {
	@Autowired
	private BookingMessageService bookingMessageService;
	
	@Autowired
	private BookingService bookingService;
	
	//SEARCH
	//admin
	@GetMapping("/bookingMessages")
	public ResponseEntity<List<BookingMessage>> retrieveAllBookingMessages(){
		return ResponseEntity.ok(bookingMessageService.findAll());
    }
	
	//admin
	@GetMapping("/bookingMessage/{id}")
	public ResponseEntity<BookingMessage> retrieveBookingMessageByMessageID(@PathVariable long id) {
		BookingMessage bookingMessage = bookingMessageService.findById(id);
		if(bookingMessage != null) {
			return ResponseEntity.status(HttpStatus.OK).body(bookingMessage);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("booking/{id}/bookingMessage")
	public ResponseEntity<List<BookingMessage>> retrieveBookingMessageByBookingID(@PathVariable long id){
		Booking booking = bookingService.findById(id);
		if(booking != null)
			return ResponseEntity.ok(bookingMessageService.findByBookingID(id));
		else return ResponseEntity.notFound().build();
	}
	
	//Getting all Booking that have Message for Provider
	@GetMapping("provider/{id}/bookingMessage")
	public ResponseEntity<List<BookingMessageBox>> retrieveBookingMessageByproviderID(@PathVariable long id){
		List<Booking> ls = bookingMessageService.findBookingHasBookingMessageForProvider(id);
		List<BookingMessageBox> list = new ArrayList<BookingMessageBox>();
		for(Booking x: ls) {
			BookingMessageBox y = new BookingMessageBox();
			y.setBookingID(x.getBookingID());
			y.setProviderID(x.getProviderService().getProvider().getProviderID());
			y.setFullName(x.getProviderService().getProvider().getFullName());
			y.setServiceID(x.getProviderService().getService().getServiceID());
			y.setServiceName(x.getProviderService().getService().getServiceName());
			y.setAvatar(x.getProviderService().getProvider().getAvatar());
			list.add(y);
		}
		return ResponseEntity.ok(list);
	}
	
	//Getting all Booking that have Message for Customer
	@GetMapping("customer/{id}/bookingMessage")
	public ResponseEntity<List<BookingMessageBox>> retrieveBookingMessageBycustomerID(@PathVariable long id){
		List<Booking> ls = bookingMessageService.findBookingHasBookingMessageForCustomer(id);
		List<BookingMessageBox> list = new ArrayList<BookingMessageBox>();
		for(Booking x: ls) {
			BookingMessageBox y = new BookingMessageBox();
			y.setBookingID(x.getBookingID());
			y.setProviderID(x.getProviderService().getProvider().getProviderID());
			y.setFullName(x.getProviderService().getProvider().getFullName());
			y.setServiceID(x.getProviderService().getService().getServiceID());
			y.setServiceName(x.getProviderService().getService().getServiceName());
			y.setAvatar(x.getProviderService().getProvider().getAvatar());
			list.add(y);
		}
		return ResponseEntity.ok(list);
	}
	
	//Creating a new Message by Provider and Customer
	@PostMapping("booking/{id}/bookingMessage")
	public ResponseEntity<BookingMessage> createBookingMessage(@PathVariable long id, @RequestBody BookingMessage bookingMessage){
		try {
			Booking booking = bookingService.findById(id);
			if(booking == null) return ResponseEntity.notFound().header("message", "Booking not found. Adding message failed").build();
			
			bookingMessage.setBooking(booking);
			
			if(bookingMessage.getTime() == null) bookingMessage.setTime(Constants.currentDate());
			BookingMessage savedMessage = bookingMessageService.add(bookingMessage);
			if(savedMessage != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new message to corresponding booking").build();
		}
	}
	
	@DeleteMapping("/bookingMessage/{id}")
	public ResponseEntity<Void> deleteBookingMessage(@PathVariable int id){
		try{
			bookingMessageService.delete(id);
			return ResponseEntity.noContent().header("message", "bookingMessage deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "BookingMessage deletion failed").build();
		}
	}
	
	@DeleteMapping("booking/{id}/bookingMessage/")
	public ResponseEntity<Void> deleteBookingMessageByBookingID(@PathVariable int id){
		try{
			Booking booking = bookingService.findById(id);
			if(booking == null) return ResponseEntity.notFound().header("message", "Booking not found. Adding message failed").build();
			
			List<BookingMessage> messageList = bookingMessageService.findByBookingID(id);
			for(BookingMessage x: messageList)
				bookingMessageService.delete(x.getMessageID());
			return ResponseEntity.noContent().header("message", "bookingMessage deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "BookingMessage deletion failed").build();
		}
	}
}
