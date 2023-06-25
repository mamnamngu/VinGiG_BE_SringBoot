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
public class BookingObject {
	//Booking
	private long bookingID;
	private String apartment;
	private long unitPrice;
	private long total;
	private int status;
	private Date date;
	private int providersRating;
	private String providersReview;
	private int customersRating;
	private String customersReview;
	
	//Customer
	private long customerID;
	private String customerFullName;
	
	//Provider Service
	private long proServiceID;
	
	//GiGService
	private int serviceID;
	private String serviceName;
	
	//Provider
	private long providerID;
	private String providerFullName;
	
	//Building
	private int buildingID;
	private String buildingName;
}
