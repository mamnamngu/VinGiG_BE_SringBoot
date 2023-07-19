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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp.VinGiG.utilities.Constants;

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
public class Booking implements Serializable, Comparable<Booking>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookingID")
	private long bookingID;
	
//	@Column(name = "customerID", insertable = false, updatable = false, nullable = false)
//	private long customerID;
//	
//	@Column(name = "proServiceID", insertable = false, updatable = false, nullable = false)
//	private long proServiceID;
//	
//	@Column(name = "buildingID", updatable = false, nullable = false)
//	private int buildingID;
	
	@Column(name = "apartment", nullable = false)
	private String apartment;
	
	@Column(name = "unitPrice", nullable = false)
	private long unitPrice;
	
	@Column(name = "total", nullable = true)
	private Long total;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "providersRating", nullable = true)
	@Size(min = Constants.REVIEW_MIN, max = Constants.REVIEW_MAX)
	private Integer providersRating;
	
	@Column(name = "providersReview", columnDefinition = "NVARCHAR(300) NULL")
	private String providersReview;
	
	@Column(name = "customersRating", nullable = true)
	@Size(min = Constants.REVIEW_MIN, max = Constants.REVIEW_MAX)
	private Integer customersRating;
	
	@Column(name = "customersReview", columnDefinition = "NVARCHAR(300) NULL")
	private String customersReview;
	
	//SORT
	@Override
    public int compareTo(Booking other) {
        return this.date.compareTo(other.getDate());
    }
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "customerID", referencedColumnName = "customerID", nullable = false, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Customer customer;
	
	@ManyToOne(targetEntity = Building.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingID", referencedColumnName = "buildingID", nullable = false, insertable = true, updatable = true)
	@JsonIgnore
	@ToString.Exclude
	private Building building;
	
	@ManyToOne(targetEntity = ProviderService.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "proServiceID", referencedColumnName = "proServiceID", nullable = false, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private ProviderService providerService;
	
	@OneToMany(targetEntity = BookingMessage.class, mappedBy = "booking")
	@JsonIgnore
	@ToString.Exclude
	private Collection<BookingMessage> bookingMessageList;
	
	@OneToMany(targetEntity = BookingFee.class, mappedBy = "booking")
	@JsonIgnore
	@ToString.Exclude
	private Collection<BookingFee> bookingFeeList;
}
