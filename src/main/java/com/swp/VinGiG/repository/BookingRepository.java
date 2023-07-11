package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Booking;
import com.swp.VinGiG.utilities.Constants;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking>{

	//Current activity
	public List<Booking> findByCustomerCustomerIDAndStatusBetween(long customerID, int statusLower, int statusUpper);
	
	public List<Booking> findByProviderServiceProviderProviderIDAndStatusBetween(long providerID, int statusLower, int statusUpper);
	
	public List<Booking> findByDateAndStatus(Date date, int status);
	
	//Count bookingNo of providerService
	public int countByProviderServiceProServiceIDAndStatus(long proServiceID, int status);
	
	//Calculate rating for customer
	public List<Booking> findByCustomerCustomerIDAndProvidersRatingIsNotNull(long customerID);
	
	//Calculate rating for providerService
	public List<Booking> findByProviderServiceProServiceIDAndCustomersRatingIsNotNull(long proServiceID);
	
	//Display all reviews for providerService
	public List<Booking> findByProviderServiceProServiceIDAndCustomersReviewIsNotNullOrderByDateDesc(long proServiceID);
	
	//History
	@Query("SELECT a FROM Booking a WHERE a.customer.customerID = :customerID AND a.date BETWEEN :dateMin AND :dateMax AND a.status != 4 AND a.status BETWEEN " + Constants.BOOKING_STATUS_COMPLETED + " AND " + Constants.BOOKING_STATUS_CANCELLED_CUSTOMER)
	public List<Booking> findByCustomerIDByDateIntervalHistory(@Param("customerID") long customerID, @Param("dateMin") Date dateMin,@Param("dateMax") Date dateMax);

	@Query("SELECT a FROM Booking a WHERE a.providerService.proServiceID = :proServiceID AND a.date BETWEEN :dateMin AND :dateMax AND a.status BETWEEN " + Constants.BOOKING_STATUS_COMPLETED + " AND " + Constants.BOOKING_STATUS_CANCELLED_PROVIDER)
	public List<Booking> findByProServiceIDByDateIntervalHistory(@Param("proServiceID") long proServiceID, @Param("dateMin") Date dateMin,@Param("dateMax") Date dateMax);

}
