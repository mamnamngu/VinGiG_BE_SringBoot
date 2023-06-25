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

public class SubscriptionFeeObject {

	//SubscriptionFee
	private long subID;
	private long amount;
	private Date date;
	
	//Plan
	private int planID;
	private String description;
	
	
	// Provider
	private long providerID;
	private String fullName;
}
