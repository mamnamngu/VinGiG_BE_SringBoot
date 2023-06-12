package com.swp.VinGiG.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.repository.BookingFeeRepository;

@RestController
public class BookingFeeController {

	@Autowired
	private BookingFeeRepository bookingFeeRepo;
	
	@GetMapping("/bookingFees")
	public List<BookingFee> retrieveAllBookingFee(){
		return bookingFeeRepo.findAll();
	}
}
