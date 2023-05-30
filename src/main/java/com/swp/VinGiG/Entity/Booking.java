package com.swp.VinGiG.Entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.VinGiG.Utilities.Constants;

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

@Table(name = "Booking")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookingID")
	private long bookingID;
	
	@Column(name = "customerID", insertable = false, updatable = false)
	private int customerID;
	
	@Column(name = "proServiceID", insertable = false, updatable = false)
	private int proServiceID;
	
	@Column(name = "buildingID", insertable = false, updatable = false)
	private int buildingID;
	
	@Column(name = "apartment", nullable = false)
	private String apartment;
	
	@Column(name = "unitPrice", nullable = false)
	private long unitPrice;
	
	@Column(name = "total")
	private long total;
	
	@Column(name = "status", nullable = false)
	private boolean status;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "providersRating")
	@Size(min = Constants.REVIEW_MIN, max = Constants.REVIEW_MAX)
	private int providersRating;
	
	@Column(name = "providersReview", columnDefinition = "NVARCHAR(300) NULL")
	private String providersReview;
	
	@Column(name = "customersRating")
	@Size(min = Constants.REVIEW_MIN, max = Constants.REVIEW_MAX)
	private int customersRating;
	
	@Column(name = "customerReview", columnDefinition = "NVARCHAR(300) NULL")
	private String customerReview;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	private Customer customer;
	
	@ManyToOne(targetEntity = Building.class, fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	private Building building;
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
	
	@OneToMany(targetEntity = BookingMessage.class, mappedBy = "booking")
	@JsonIgnore
	@ToString.Exclude
	private Collection<BookingMessage> bookingMessageList;
	
}
