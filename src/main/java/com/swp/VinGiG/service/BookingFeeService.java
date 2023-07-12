package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.Transction;
import com.swp.VinGiG.entity.Wallet;
import com.swp.VinGiG.repository.BookingFeeRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.BookingFeeObject;

@Service
public class BookingFeeService {
	@Autowired
	private BookingFeeRepository bookingFeeRepo;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
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
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingFeeRepo.findByDateBetween(dateMin, dateMax);
	}
	
	public List<BookingFee> findByProviderIDDateInterval(long providerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		Provider provider = providerService.findById(providerID);
		if(provider == null) return null;
		List<Booking> ls = bookingService.findByProServiceIDByDateInterval(providerID, null, null);
		List<BookingFee> list = new ArrayList<>();
		for(Booking x: ls) {
			List<BookingFee> fee = bookingFeeRepo.findByBookingBookingID(x.getBookingID());
			if(fee.size() > 0)
				if(fee.get(0).getDate().before(dateMax) && fee.get(0).getDate().after(dateMin))
					list.addAll(fee);
		}
		return list;
	}
	
	//ADD
	public BookingFee add(BookingFee bookingFee) {
		bookingFee.setDate(Constants.currentDate());
		bookingFee.setAmount(bookingFee.getBooking().getProviderService().getService().getFee());
		
		//Create a transaction
		Transction transction = new Transction();
		transction.setBookingFee(bookingFee);
		transction.setDate(Constants.currentDate());
		transction.setAmount(bookingFee.getAmount()*Constants.TRANSACTION_SUBTRACT_FACTOR);
		
		Provider provider = bookingFee.getBooking().getProviderService().getProvider();
		List<Wallet> wallet = walletService.findByProviderId(provider.getProviderID());
		if(wallet == null || wallet.size() == 0) return null;
		transction.setWallet(wallet.get(0));
		
		//save father object in db
		BookingFee tmp = bookingFeeRepo.save(bookingFee);
		
		//save Transaction
		Transction output = transactionService.add(transction);
		
		if(output == null) {
			delete(tmp.getBookingFeeID());
			return null;
		}
		
		return tmp;
	}
	
	//UPDATE
	public BookingFee update(BookingFee newBookingFee) {
		return bookingFeeRepo.save(newBookingFee);
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
	
	//BACKGROUND WORKER
	public List<BookingFee> regularDelete(){
		Date currentDate = Constants.currentDate();
		List<BookingFee> ls = findByDateInterval(Constants.subtractDay(currentDate, Constants.BOOKINGFEEE_DELETION),currentDate);
		for(BookingFee x: ls) {
			delete(x.getBookingFeeID());
		}
		return ls;
	}
}
