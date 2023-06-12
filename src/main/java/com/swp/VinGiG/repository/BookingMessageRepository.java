package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.BookingMessage;

@Repository
public interface BookingMessageRepository  extends JpaRepository<BookingMessage, Long>, JpaSpecificationExecutor<BookingMessage>{

	public List<BookingMessage> findByBookingBookingIDOrderByTimeAsc(long bookingID);
}
