package com.swp.VinGiG.view;

import java.io.Serializable;
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

public class BookingMessageObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//BookingMessage
	private long messageID;
	private boolean sendBy;
	private String content;
	private Date time;
	
	//Booking
	private long bookingID;
}
