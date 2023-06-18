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
	//admin
	public List<Booking> findAll(){
		return bookingRepo.findAll();
	}
	
	public Booking findById(long id) {
		Optional<Booking> booking = bookingRepo.findById(id);
		if(booking.isPresent()) return booking.get();
		else return null;
	}
	
	public List<Booking> findByDate(Date date, Integer status){
		if(date == null) date = Constants.currentDate();
		if(status == null) status = Constants.BOOKING_STATUS_COMPLETED;
		
		return bookingRepo.findByDateAndStatus(date, status);
	}
	
	//FINDING CURRENT ACTIVITY FOR CUSTOMER AND PROVIDER
	public List<Booking> findByProviderIDGoingOn(long providerID){
		return bookingRepo.findByProviderServiceProviderProviderIDAndStatus(providerID, Constants.BOOKING_STATUS_PENDING);
	}
	
	public List<Booking> findByCustomerIDGoingOn(long customerID){
		return bookingRepo.findByCustomerCustomerIDAndStatus(customerID, Constants.BOOKING_STATUS_PENDING);
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
		List<com.swp.VinGiG.entity.ProviderService> proService = providerServiceService.findByProviderID(providerID);
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
		return bookingRepo.countByProviderServiceProServiceIDAndStatus(proServiceID, Constants.BOOKING_STATUS_COMPLETED);
	}
	
	//display reviews for a providerService
	public List<Booking> reviewsByProServiceID(long proServiceID){
		return bookingRepo.findByProviderServiceProServiceIDAndCustomersReviewIsNotNullOrderByDateDesc(proServiceID);
	}
	
	//display booking history by customerID over a time interval
	public List<Booking> findByCustomerIDByDateInterval(long customerID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingRepo.findByCustomerIDByDateIntervalAndStatus(customerID, dateMin, dateMax, Constants.BOOKING_STATUS_COMPLETED);
	}
	
	//display booking by proServiceID over a time interval
	public List<Booking> findByProServiceIDByDateInterval(long proServiceID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingRepo.findByProServiceIDByDateIntervalAndStatus(proServiceID, dateMin, dateMax, Constants.BOOKING_STATUS_COMPLETED);
	}
	
	//ADD
	public Booking add(Booking booking) {
		return bookingRepo.save(booking);
	}
	
	
	//UPDATE for admin
	public Booking update(Booking newBooking) {
		return bookingRepo.save(newBooking);
	}
	
	//DELETE
	public boolean delete(long id) {
		bookingRepo.deleteById(id);
		return bookingRepo.findById(id).isEmpty();
	}
	
	//BUSINESS CORE
	//Customer place a Booking
	public Booking placeBooking(Booking booking) {
		if(findById(booking.getBookingID()) != null) return null;
		//Open Web Socket
		
		booking.setStatus(Constants.BOOKING_STATUS_PENDING);
		return add(booking);
	}
	
	//Provider accept a Booking
	public Booking acceptBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_ACCEPTED);
		update(booking);
		
		//Set the availability of all other Provider Service of such Provider to FALSE
		providerServiceService.setAvailability(booking.getProviderService().getProvider().getProviderID(), false);
		return booking;
	}
	
	//Provider decline a Booking
	public Booking declineBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_DECLINED);
		update(booking);
		return booking;
	}
	
	//Provider confirm the completion of a Booking
	public Booking completeBooking(Booking booking, Long total) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_COMPLETED);
		if(total != null) booking.setTotal(total);
		update(booking);
		
		//Set the availability of all other Provider Service of such Provider to TRUE
		providerServiceService.setAvailability(booking.getProviderService().getProvider().getProviderID(), true);
		
		
		//Update BookingNo
		com.swp.VinGiG.entity.ProviderService proService = booking.getProviderService();
		proService.setBookingNo(bookingNoByProServiceID(proService.getProServiceID()));
		providerServiceService.update(proService);
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
