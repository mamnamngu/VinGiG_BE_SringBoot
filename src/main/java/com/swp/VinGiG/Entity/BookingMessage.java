package com.swp.VinGiG.Entity;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Table(name = "BookingMessage")
public class BookingMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "messageID")
	private long messageID;
	
	@Column(name = "bookingID", insertable = false, updatable = false)
	private long bookingID;
	
	@Column(name = "sendBy", nullable = false)
	private boolean sendBy;
	
	@Column(name = "content", nullable = false, columnDefinition = "NVARCHAR(700)", updatable = false)
	private String content;
	
	@Column(name = "time")
	@Convert(converter = com.swp.VinGiG.Utilities.ZonedDateTimeConverter.class)
	private ZonedDateTime time;
	
	//RELATIONSHIP SETUP
	@ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	private Booking booking;
}
