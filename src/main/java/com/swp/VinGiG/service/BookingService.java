package com.swp.VinGiG.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.repository.BookingRepository;
import com.swp.VinGiG.utilities.Constants;

@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ProviderServiceService providerServiceService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private CustomerService customerService;

	//FIND
	public List<Booking> findAll(){
		return bookingRepo.findAll();
	}
	
	public Booking findById(long id) {
		Optional<Booking> booking = bookingRepo.findById(id);
		if(booking.isPresent()) return booking.get();
		else return null;
	}
	
	public List<Booking> findByDate(Date date){
		return bookingRepo.findByDate(date);
	}
	
	//FINDING CURRENT ACTIVITY FOR CUSTOMER AND PROVIDER
	public List<Booking> findByProServiceIDGoingOn(long proServiceID){
		return bookingRepo.findByProviderServiceProServiceID(proServiceID);
	}
	
	public List<Booking> findByCustomerIDGoingOn(long customerID){
		return bookingRepo.findByCustomerCustomerID(customerID);
	}
	
	//rating for customer
	public double ratingAverageForCustomerByCustomerID(long customerID) {
		List<Booking> providersRatingBooking = bookingRepo.findByCustomerCustomerIDAndProvidersRatingIsNotNull(customerID);
		double total = 0;
		for(Booking booking: providersRatingBooking)
			total += booking.getProvidersRating();
		return DoubleRounder.round(total/providersRatingBooking.size(), 1);
	}
	
	//rating for provider
	public double ratingAverageForProviderByProviderID(long providerID) {
		ProviderServiceService pss = new ProviderServiceService();
		List<com.swp.VinGiG.entity.ProviderService> proService = pss.findByProviderID(providerID);
		double total = 0;
		int bookingNo = 0;
		for(com.swp.VinGiG.entity.ProviderService ps: proService) {
			total += ps.getRating()*ps.getBookingNo();
			bookingNo += ps.getBookingNo();
		}
		return DoubleRounder.round(total/bookingNo, 1);
	}
	
	//rating for providerService
	public double ratingAverageForProviderServiceByProServiceID(long proServiceID) {
		List<Booking> customersRatingBooking = bookingRepo.findByProviderServiceProServiceIDAndCustomersRatingIsNotNull(proServiceID);
		double total = 0;
		for(Booking booking: customersRatingBooking)
			total += booking.getCustomersRating();
		return DoubleRounder.round(total/customersRatingBooking.size(), 1);
	}
	
	//bookingNo for providerService
	public int bookingNoByProServiceID(long proServiceID) {
		return bookingRepo.countByProviderServiceProServiceID(proServiceID);
	}
	
	//display reviews for a providerService
	public List<Booking> reviewsByProServiceID(long proServiceID){
		return bookingRepo.findByProviderServiceProServiceIDAndCustomersReviewIsNotNullOrderByDateDesc(proServiceID);
	}
	
	//display booking by customerID over a time interval
	public List<Booking> findByCustomerIDByDateInterval(long customerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingRepo.findByCustomerIDByDateInterval(customerID, dateMin, dateMax);
	}
	
	//display booking by proServiceID over a time interval
	public List<Booking> findByProServiceIDByDateInterval(long proServiceID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingRepo.findByProServiceIDByDateInterval(proServiceID, dateMin, dateMax);
	}
	
	//ADD
	public Booking add(Booking booking) {
		return bookingRepo.save(booking);
	}
	
	
	//UPDATE for admin
	public Booking update(Booking newBooking) {
		return add(newBooking);
	}
	
	//DELETE
	public boolean delete(long id) {
		bookingRepo.deleteById(id);
		return bookingRepo.findById(id).isEmpty();
	}
	
	//BUSINESS CORE
	//Customer place a Booking
	public Booking placeBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
//		booking.setStatus(false);
		return add(booking);
	}
	
	//Provider accept a Booking
	public Booking acceptBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
//		booking.setStatus(true);
		update(booking);
		
		//Set the availability of all other Provider Service of such Provider to FALSE
		List<com.swp.VinGiG.entity.ProviderService> psList = providerServiceService.findByProviderID(booking.getProviderService().getProvider().getProviderID());
		for(com.swp.VinGiG.entity.ProviderService x: psList) {
			x.setAvailability(false);
			providerServiceService.update(x);
		}
		return booking;
	}
	
	//Provider decline a Booking
	public Booking declineBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
//		booking.setStatus(false);
		update(booking);
		return booking;
	}
	
	//Provider confirm the completion of a Booking
	public Booking completeBooking(Booking booking, Long total) {
		if(findById(booking.getBookingID()) == null) return null;
//		booking.setStatus(true);
		if(total != null) booking.setTotal(total);
		update(booking);
		
		//Set the availability of all other Provider Service of such Provider to TRUE
		List<com.swp.VinGiG.entity.ProviderService> psList = providerServiceService.findByProviderID(booking.getProviderService().getProvider().getProviderID());
		for(com.swp.VinGiG.entity.ProviderService x: psList) {
			x.setAvailability(true);
			providerServiceService.update(x);
		}
		return booking;
	}
	
	//Review
	public Booking reviewBookingByCustomerID(long customerID, Booking booking, String content, Integer rating) {
		if(customerID != booking.getCustomer().getCustomerID()) return null;
		if(!content.isEmpty()) booking.setCustomersReview(content);
		if(rating != null) booking.setCustomersRating(rating);
		
		//UPDATE Provider Service Rating
		com.swp.VinGiG.entity.ProviderService ps = booking.getProviderService();
		ps.setRating(ratingAverageForProviderServiceByProServiceID(ps.getProServiceID()));
		providerServiceService.update(ps);
		update(booking);
		
		//UPDATE Provider Rating
		Provider p = booking.getProviderService().getProvider();
		p.setRating(ratingAverageForProviderByProviderID(p.getProviderID()));
		providerService.update(p);
		return booking;
	}
	
	public Booking reviewBookingByProviderID(long providerID, Booking booking, String content, Integer rating) {
		if(providerID != booking.getProviderService().getProvider().getProviderID()) return null;
		
		if(!content.isEmpty()) booking.setProvidersReview(content);
		if(rating != null) booking.setProvidersRating(rating);
		update(booking);
		
		//UPDATE Customer Rating
		Customer c = booking.getCustomer();
		c.setRating(ratingAverageForCustomerByCustomerID(c.getCustomerID()));
		customerService.update(c);

		return booking;
	}
}
