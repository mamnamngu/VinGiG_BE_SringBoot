package com.swp.VinGiG.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

@Table(name = "Transction")
public class Transction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactionID")
	private long transactionID;
	
//	@Column(name = "walletID", nullable = false, insertable = false, updatable = false)
//	private long walletID;
//	
//	@Column(name = "depositID", nullable = true, insertable = false, updatable = false)
//	private long depositID;
//	
//	@Column(name = "bookingFeeID", nullable = true, insertable = false, updatable = false)
//	private long bookingFeeID;
//	
//	@Column(name = "subID", nullable = true, insertable = false, updatable = false)
//	private long subID;
	
	@Column(name = "amount", nullable = false, insertable = true, updatable = false)
	private long amount;
	
	@Column(name = "date", nullable = false, insertable = true, updatable = false)
	private Date date;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Wallet.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "walletID", referencedColumnName = "walletID", nullable = false, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Wallet wallet;
	
	@ManyToOne(targetEntity = BookingFee.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingFeeID", referencedColumnName = "bookingFeeID", nullable = true, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private BookingFee bookingFee;
	
	@ManyToOne(targetEntity = Deposit.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "depositID", referencedColumnName = "depositID", nullable = true, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Deposit deposit;
	
	@ManyToOne(targetEntity = SubscriptionFee.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "subID", referencedColumnName = "subID", nullable = true, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private SubscriptionFee subscriptionFee;
}
