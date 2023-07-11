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
public class ProviderServiceObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long proServiceID;
	private double rating;
	private int bookingNo;
	private long unitPrice;
	private String description;
	private boolean availability;
	private boolean visible;
	private boolean active;
	
	//Provider
	private long providerID;
	private String fullName;
	private boolean gender;
	
	//Service
	private int serviceID;
	private String serviceName;
	
	//Badge
	private int badgeID;
	private String badgeName;
	
	//Image
	private long imageID;
	private String link;
	
}
