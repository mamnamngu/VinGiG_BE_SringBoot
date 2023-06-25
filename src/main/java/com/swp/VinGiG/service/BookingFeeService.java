package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.repository.BookingFeeRepository;
import com.swp.VinGiG.view.BookingFeeObject;

@Service
public class BookingFeeService {
	@Autowired
	private BookingFeeRepository bookingFeeRepo;
	
	//FIND
	public List<BookingFee> findAll(){
		return bookingFeeRepo.findAll();
	}
	
	public BookingFee findById(long id) {
		Optional<BookingFee> BookingFee = bookingFeeRepo.findById(id);
		if(BookingFee.isPresent()) return BookingFee.get();
		else return null;
	}

	public List<BookingFee> findByBookingBookingID(long id){
		return bookingFeeRepo.findByBookingBookingID(id);
	}
	
	public List<BookingFee> findByDate(Date date){
		return bookingFeeRepo.findByDate(date);
	}
	
	public List<BookingFee> findByDateInterval(Date dateMin, Date dateMax){
		return bookingFeeRepo.findByDateBetween(dateMin, dateMax);
	}
	
	public List<BookingFee> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		
		return null;
	}
	
	//ADD
	public BookingFee add(BookingFee BookingFee) {
		return bookingFeeRepo.save(BookingFee);
	}
	
	//UPDATE
	public BookingFee update(BookingFee newBookingFee) {
		return add(newBookingFee);
	}
	
	//DELETE
	public boolean delete(long id) {
		bookingFeeRepo.deleteById(id);
		return bookingFeeRepo.findById(id).isEmpty();
	}
	
	//DISPLAY
	public List<BookingFeeObject> display(List<BookingFee> ls){
		List<BookingFeeObject> list = new ArrayList<>();
		for(BookingFee x: ls) {
			BookingFeeObject y = new BookingFeeObject();
			y.setBookingFeeID(x.getBookingFeeID());
			y.setAmount(x.getAmount());
			y.setDate(x.getDate());
			y.setBookingID(x.getBooking().getBookingID());
			
			GiGService service = x.getBooking().getProviderService().getService();				
			y.setServiceID(service.getServiceID());
			y.setServiceName(service.getServiceName());
			
			Provider provider = x.getBooking().getProviderService().getProvider();
			y.setProviderID(provider.getProviderID());
			y.setFullName(provider.getFullName());
			
			list.add(y);
		}
		return list;
	}
}
