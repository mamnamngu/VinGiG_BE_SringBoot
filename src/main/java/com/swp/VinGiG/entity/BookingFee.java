package com.swp.VinGiG.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

@Table(name = "BookingFee")
public class BookingFee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookingFeeID")
	private long bookingFeeID;
	
//	@Column(name = "bookingID", insertable = false, nullable = false, updatable = false)
//	private long bookingID;
	
	@Column(name = "amount", nullable = false)
	private long amount;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingID", referencedColumnName = "bookingID", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Booking booking;
	
	@OneToMany(targetEntity = Transaction.class, mappedBy = "bookingFee")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Transaction> transactionList;
	
	
}
