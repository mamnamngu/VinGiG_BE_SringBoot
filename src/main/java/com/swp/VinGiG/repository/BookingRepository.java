package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking>{

	//Current activity. CORRECT THE STATUS TO 1 (PENDING)
	public List<Booking> findByCustomerCustomerIDAndStatus(long customerID, int status);
	
	public List<Booking> findByProviderServiceProviderProviderIDAndStatus(long providerID, int status);
	
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
	@Query("SELECT a FROM Booking a WHERE a.customer.customerID = :customerID AND a.date BETWEEN :dateMin AND :dateMax AND a.status = :status")
	public List<Booking> findByCustomerIDByDateIntervalAndStatus(@Param("customerID") long customerID, @Param("dateMin") Date dateMin,@Param("dateMax") Date dateMax, @Param("status") int status);

	@Query("SELECT a FROM Booking a WHERE a.providerService.proServiceID = :proServiceID AND a.date BETWEEN :dateMin AND :dateMax AND a.status = :status")
	public List<Booking> findByProServiceIDByDateIntervalAndStatus(@Param("proServiceID") long proServiceID, @Param("dateMin") Date dateMin,@Param("dateMax") Date dateMax, @Param("status") int status);

}
