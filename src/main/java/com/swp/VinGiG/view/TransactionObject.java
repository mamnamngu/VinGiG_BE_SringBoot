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

public class TransactionObject {

	//Transaction
	private long transactionID;
	private long amount;
	private Date date;
	
	//Provider
	private long providerID;
	private String providerFullName;
	
	//Deposit
	private long depositID;
	
	//SubscriptionFee
	private long subID;
	
	//BookingFee
	private long bookingFeeID;
	
}
