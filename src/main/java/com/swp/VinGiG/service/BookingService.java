package com.swp.VinGiG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.entity.BookingFee;
import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.entity.Customer;
import com.swp.VinGiG.entity.GiGService;
import com.swp.VinGiG.entity.Image;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.repository.BookingRepository;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.BookingObject;

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
	
	@Autowired
	private BookingFeeService bookingFeeService;
	
	@Autowired
	private ImageService imageService;
	

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
		List<Booking> ls = new ArrayList<Booking>();
		ls.addAll(bookingRepo.findByProviderServiceProviderProviderIDAndStatusBetween(providerID, Constants.BOOKING_STATUS_PENDING, Constants.BOOKING_STATUS_ACCEPTED));
		return ls;
	}
	
	public List<Booking> findByCustomerIDGoingOn(long customerID){
		List<Booking> ls = new ArrayList<>();
		ls.addAll(bookingRepo.findByCustomerCustomerIDAndStatusBetween(customerID, Constants.BOOKING_STATUS_PENDING, Constants.BOOKING_STATUS_ACCEPTED));
		return ls;
	}
	
	//rating for customer
	private double ratingAverageForCustomerByCustomerID(long customerID) {
		List<Booking> providersRatingBooking = bookingRepo.findByCustomerCustomerIDAndProvidersRatingIsNotNull(customerID);
		double total = 0;
		for(Booking booking: providersRatingBooking)
			total += booking.getProvidersRating();
		return DoubleRounder.round(total/providersRatingBooking.size(), 1);
	}
	
	//rating for provider
	private double ratingAverageForProviderByProviderID(long providerID) {
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
	private double ratingAverageForProviderServiceByProServiceID(long proServiceID) {
		List<Booking> customersRatingBooking = bookingRepo.findByProviderServiceProServiceIDAndCustomersRatingIsNotNull(proServiceID);
		double total = 0;
		for(Booking booking: customersRatingBooking)
			total += booking.getCustomersRating();
		return DoubleRounder.round(total/customersRatingBooking.size(), 1);
	}
	
	//bookingNo for providerService
	private int bookingNoByProServiceID(long proServiceID) {
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
		return bookingRepo.findByCustomerIDByDateIntervalHistory(customerID, dateMin, dateMax);
	}
	
	//display booking history by proServiceID over a time interval
	public List<Booking> findByProServiceIDByDateInterval(long proServiceID, Date dateMin, Date dateMax){
		if(dateMin == null) dateMin = Constants.START_DATE;
		if(dateMax == null) dateMax = Constants.currentDate();
		return bookingRepo.findByProServiceIDByDateIntervalHistory(proServiceID, dateMin, dateMax);
	}
	
	//display booking history by proServiceID over a time interval
		public List<Booking> findByProviderIDByDateInterval(long providerID, Date dateMin, Date dateMax){
			if(dateMin == null) dateMin = Constants.START_DATE;
			if(dateMax == null) dateMax = Constants.currentDate();
			Provider provider = providerService.findById(providerID);
			if(provider == null) return null;
			
			List<com.swp.VinGiG.entity.ProviderService> ls = providerServiceService.findByProviderID(providerID);
			List<Booking> list = new ArrayList<>();
			for(com.swp.VinGiG.entity.ProviderService x:ls)
				list.addAll(bookingRepo.findByProServiceIDByDateIntervalHistory(x.getProServiceID(), dateMin, dateMax));
			return list;
		}
	
	//ADD
	private Booking add(Booking booking) {
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
		
		booking.setStatus(Constants.BOOKING_STATUS_PENDING);
		return add(booking);
	}
	
	//Provider accept a Booking
	public Booking acceptBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		
		//Create a new Booking Fee
		BookingFee bookingFee = new BookingFee();
		bookingFee.setAmount(booking.getProviderService().getService().getFee());
		bookingFee.setBooking(booking);
		bookingFee.setDate(Constants.currentDate());
		BookingFee output = bookingFeeService.add(bookingFee);
		
		if(output == null) return null;
		
		//Update booking status
		booking.setStatus(Constants.BOOKING_STATUS_ACCEPTED);
		
		//Set the availability of all other Provider Service of such Provider to FALSE
		providerServiceService.setAvailability(booking.getProviderService().getProvider().getProviderID(), false);
		
		return update(booking);
	}
	
	//Provider decline a Booking
	public Booking declineBooking(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_DECLINED);
		
		return update(booking);
	}
	
	//Provider cancel a booking
	public Booking cancelBookingProvider(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_CANCELLED_PROVIDER);
		
		//SUSPEND THE PROVIDER'S ACCOUNT FOR 1 HOUR
		
		//Set the availability of all other Provider Service of such Provider to TRUE
		providerServiceService.setAvailability(booking.getProviderService().getProvider().getProviderID(), true);
				
		return update(booking);
	}
	
	//Customer cancel a booking
	public Booking cancelBookingCustomer(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_CANCELLED_CUSTOMER);	
		return update(booking);
	}
	
	//Timeout
	public Booking timeout(Booking booking) {
		if(findById(booking.getBookingID()) == null) return null;
		booking.setStatus(Constants.BOOKING_STATUS_TIMEOUT);	
		return update(booking);
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
	
	//UPDATE ratings and bookingNo
	public void updateCustomerRating(Customer customer) {
		if(customer == null) return;
		
		double newRating = ratingAverageForCustomerByCustomerID(customer.getCustomerID());
		customer.setRating(newRating);
		customerService.update(customer);
	}
	
	public void updateProviderRating(Provider provider) {
		if(provider == null) return;
		
		double newRating = ratingAverageForProviderByProviderID(provider.getProviderID());
		provider.setRating(newRating);
		providerService.update(provider);
	}
	
	public void updateProviderServiceRating(com.swp.VinGiG.entity.ProviderService providerService) {
		if(providerService == null) return;
		
		double newRating = ratingAverageForProviderServiceByProServiceID(providerService.getProServiceID());
		providerService.setRating(newRating);
		providerServiceService.update(providerService);
	}
	
	public void updateProviderServiceBookingNo(long proServiceID) {
		com.swp.VinGiG.entity.ProviderService providerService = providerServiceService.findById(proServiceID);
		if(providerService == null) return;
		
		int newBookingNo = bookingNoByProServiceID(proServiceID);
		providerService.setBookingNo(newBookingNo);
		providerServiceService.update(providerService);
	}
	
	//Weekly update
	public void weeklyCustomerRatingUpdate() {
		List<Customer> ls = customerService.findAll();
		for(Customer x: ls)
			updateCustomerRating(x);
	}
	
	public void weeklyProviderRatingUpdate() {
		List<Provider> ls = providerService.findAll();
		for(Provider x: ls)
			updateProviderRating(x);
	}
	
	public void weeklyProviderServiceRatingUpdate() {
		List<com.swp.VinGiG.entity.ProviderService> ls = providerServiceService.findAll();
		for(com.swp.VinGiG.entity.ProviderService x: ls)
			updateProviderServiceRating(x);
	}
	
	//Display
	
	public List<BookingObject> display(List<Booking> ls){
		List<BookingObject> list = new ArrayList<BookingObject>();
		for(Booking x: ls) {
			BookingObject y = new BookingObject();
			y.setBookingID(x.getBookingID());
			y.setApartment(x.getApartment());
			y.setUnitPrice(x.getUnitPrice());
			if(x.getTotal() != null) y.setTotal(x.getTotal());
			y.setStatus(x.getStatus());
			y.setDate(x.getDate());
			if(x.getProvidersRating() != null) y.setProvidersRating(x.getProvidersRating());
			y.setProvidersReview(x.getProvidersReview());
			if(x.getCustomersRating() != null) y.setCustomersRating(x.getCustomersRating());
			y.setCustomersReview(x.getCustomersReview());
			
			Customer customer = x.getCustomer();
			y.setCustomerID(customer.getCustomerID());
			y.setCustomerFullName(customer.getFullName());
			
			com.swp.VinGiG.entity.ProviderService proService = x.getProviderService();
			y.setProServiceID(proService.getProServiceID());
			
			Provider provider = proService.getProvider();
			y.setProviderID(provider.getProviderID());
			y.setProviderFullName(provider.getFullName());
			
			GiGService service = proService.getService();
			y.setServiceID(service.getServiceID());
			y.setServiceName(service.getServiceName());
		
			Building building = x.getBuilding();
			y.setBuildingID(building.getBuildingID());
			y.setBuildingName(building.getBuildingName());
			
			List<Image> lis = imageService.findByProviderServiceID(x.getProviderService().getProServiceID());	
			y.setImage(lis.get(0).getLink());
			list.add(y);
		}
		return list;
	}
}
