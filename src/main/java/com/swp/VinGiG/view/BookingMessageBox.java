package com.swp.VinGiG.view;

import java.io.Serializable;

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

public class BookingMessageBox implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Booking
	private long bookingID;
	
	//Provider
	private long providerID;
	private String providerFullName;
	private String providerAvatar;
	
	//Customer
	private long customerID;
	private String customerFullName;
	private String customerAvatar;
	
	//GiGService
	private long serviceID;
	private String serviceName;
	
}
