package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.BookingMessage;
import com.swp.VinGiG.repository.BookingMessageRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class BookingMessageService {

	@Autowired
	private BookingMessageRepository bookingMessageRepo;
	
	@Autowired
	ProviderServiceService pss;
	
	@Autowired
	BookingService bs;
	
	//FIND
	public List<BookingMessage> findAll(){
		return bookingMessageRepo.findAll();
	}
	
	public BookingMessage findById(long id) {
		Optional<BookingMessage> bookingMessage = bookingMessageRepo.findById(id);
		if(bookingMessage.isPresent()) return bookingMessage.get();
		else return null;
	}
	
	public List<BookingMessage> findByBookingID(long bookingID){
		return bookingMessageRepo.findByBookingBookingIDOrderByTimeAsc(bookingID);
	}
	
	public List<Booking> findBookingHasBookingMessageForProvider(long providerID){
		List<Booking> list = new ArrayList<Booking>();
		List<com.swp.VinGiG.entity.ProviderService> psList = pss.findByProviderID(providerID);
		List<Booking> bookingList = new ArrayList<Booking>();
		for(com.swp.VinGiG.entity.ProviderService x: psList)
			bookingList.addAll(bs.findByProServiceIDByDateInterval(x.getProServiceID(), null, null));
		
		for(Booking y: bookingList)
			if(findByBookingID(y.getBookingID()).size() > 0)
				list.add(y);
		return list;
	}
	
	public List<Booking> findBookingHasBookingMessageForCustomer(long customerID){
		List<Booking> list = new ArrayList<Booking>();
		List<Booking> bookingList = bs.findByCustomerIDByDateInterval(customerID, null, null);
		for(Booking y: bookingList)
			if(findByBookingID(y.getBookingID()).size() > 0)
				list.add(y);
		return list;
	}
	
	public List<BookingMessage> findByTimeInterval(Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingMessageRepo.findByTimeBetween(dateMin, dateMax);
	}
	
	//ADD
	public BookingMessage add(BookingMessage bookingMessage) {
		return bookingMessageRepo.save(bookingMessage);
	}
	
	//DELETE
	public boolean delete(long id) {
		bookingMessageRepo.deleteById(id);
		return bookingMessageRepo.findById(id).isEmpty();
	}
	
	//BACKGROUND WORKER
	public List<BookingMessage> regularDelete(){
		Date currentDate = Constants.currentDate();
		List<BookingMessage> ls = findByTimeInterval(Constants.subtractDay(currentDate, Constants.BOOKINGMESSAGE_DELETION),currentDate);
		for(BookingMessage x: ls) {
			delete(x.getMessageID());
		}
		return ls;
	}
}
