package com.swp.VinGiG.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class Constants {
	public static final int DEFAULT_BADGEID = 1;
	public static final int NEW_PROVIDER_BADGEID = 6;
	public static final int MONTH_PERIOD = 30;
	public static final String DEFAULT_AVATAR = "https://media.istockphoto.com/id/1311497219/vector/stylish-avatar-in-black-in-a-yellow-circle-profile-template-for-websites-applications-social.jpg?s=612x612&w=0&k=20&c=SzDRaxQ_ckdqqPbjFqw23uTgj6bbu3x4G66O7j08o6U=";
	
	//Review cutoffs
	public static final int REVIEW_MIN = 1;
	public static final int REVIEW_MAX = 5;
	
	//VALIDATION
	public static final int USERNAME_MIN = 6;
	public static final int PASSWORD_MIN = 6;
	
	//Count
	public static final int COUNT_DAY_LEFT_NOTI = 5;
	public static final int COUNT_DAY_OVERDUE = -3;
	
	//Wallet
	public static final long WALLET_BALANCE_THRESHOLD = 5000;
	
	//Date
	public static final Date START_DATE = new Date(05/05/2023);
	public static final Date currentDate() {
		return new Date();
	}
	
	public static final Date subtractDaysFromDate(Date date, int days) {
		LocalDate currenDate = LocalDate.now();
		currenDate.minusDays(days);
		return Date.from(currenDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static final long DEFAULT_LOWER = -10000;
	public static final long DEFAULT_UPPER = 1000000000;
	public static final long NEW_WALLET_BALANCE_PROMO = 10000;
	
	//Transaction type constants
	public static final String TRANSACTION_DEPOSIT = "deposit";
	public static final String TRANSACTION_BOOKINGFEE = "bookingFee";
	public static final String TRANSACTION_SUBSCRIPTIONFEE = "subscriptionFee";
	public static final int TRANSACTION_SUBTRACT_FACTOR = -1;
	
	//Booking Status
	public static final int BOOKING_STATUS_PENDING = 0;
	public static final int BOOKING_STATUS_ACCEPTED = 1;
	public static final int BOOKING_STATUS_COMPLETED = 2;
	public static final int BOOKING_STATUS_DECLINED = 3;
	public static final int BOOKING_STATUS_CANCELLED_PROVIDER = 4;
	public static final int BOOKING_STATUS_CANCELLED_CUSTOMER = 5;
	public static final int BOOKING_STATUS_TIMEOUT = 6;
	
	//Count
	public static final int DEFAULT_COUNT = 3;
	
	//Booking Message
	public static final boolean SENDBY_CUSTOMER = true;
	public static final boolean SENDBY_PROVIDER = false;
	
	//Booking Actions
	public static final String BOOKING_ACCEPT = "accept";
	public static final String BOOKING_COMPLETE = "complete";
	public static final String BOOKING_DECLINE = "decline";
	public static final String BOOKING_CANCEL_PROVIDER = "cancel_provider";
	public static final String BOOKING_CANCEL_CUSTOMER = "cancel_customer";
	public static final String BOOKING_TIMEOUT = "timeout";
	
	//Date format
	public static final Date strToDate(String dateStr) {
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
	     try {
	         Date date = dateFormat.parse(dateStr);
	         return date;
	     } catch (ParseException e) {
	         System.out.println("Error parsing date: " + e.getMessage());
	     }
	     return null;
     }
	
	public static final Date subtractDay(Date date, int offset) {
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		ldt.minusDays(offset);
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	//REGULAR BACHGROUND WORKER
	//DELETION
	public static final int BOOKINGFEEE_DELETION = 90;
	public static final int BOOKINGMESSAGE_DELETION = 90;
	public static final int DEPOSIT_DELETION = 180;
	public static final int SUBSCRIPTIONFEE_DELETION = 180;
}
