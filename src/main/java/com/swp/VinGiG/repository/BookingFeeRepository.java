package com.swp.VinGiG.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.BookingFee;


@Repository
public interface BookingFeeRepository  extends JpaRepository<BookingFee, Long>, JpaSpecificationExecutor<BookingFee>{

	public List<BookingFee> findByBookingBookingID(long bookingID);
	
	public List<BookingFee> findByDate(Date date);
	
	public List<BookingFee> findByDateBetween(Date dateMin, Date dateMax);

	public List<BookingFee> findByBookingBookingIDAndDateBetween(long bookingID, Date dateMin, Date dateMax);
}
