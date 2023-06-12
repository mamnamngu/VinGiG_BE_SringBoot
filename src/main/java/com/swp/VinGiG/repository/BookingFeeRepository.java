package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.BookingFee;

@Repository
public interface BookingFeeRepository  extends JpaRepository<BookingFee, Long>, JpaSpecificationExecutor<BookingFee>{

	public List<BookingFee> findByBookingBookingID(long bookingID);
	
	public List<BookingFee> findByDate(Date date);
	
	@Query("SELECT b FROM BookingFee b WHERE b.date BETWEEN :dateMin AND :dateMax")
	public List<BookingFee> findByDateInterval(@Param("dateMin") java.util.Date dateMin, @Param("dateMax") java.util.Date dateMax);
	
//	@Query("SELECT b FROM BookingFee b WHERE b.date BETWEEN :dateMin AND :dateMax")
//	public List<BookingFee> findByBookingIDDateInterval(@Param("bookingID") long bookingID, @Param("dateMin") java.util.Date dateMin, @Param("dateMax") java.util.Date dateMax);
}
