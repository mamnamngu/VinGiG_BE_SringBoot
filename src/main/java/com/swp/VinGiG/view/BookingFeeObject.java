package com.swp.VinGiG.view;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data

public class BookingFeeObject {
	//Booking Fee
	private long bookingFeeID;
	private long amount;
	private Date date;
	
	//Booking
	private long bookingID;
	
	//GiGService
	private int serviceID;
	private String serviceName;
	
	//Provider
	private long providerID;
	private String fullName;
}
