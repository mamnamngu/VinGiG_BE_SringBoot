package com.swp.VinGiG.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
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

@Table(name = "ProviderService")
public class ProviderService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proServiceID")
	private long proServiceID;
	
//	@Column(name = "providerID", insertable = false, updatable = false, nullable = false)
//	private long providerID;
//	
//	@Column(name = "serviceID", insertable = false, updatable = false, nullable = false)
//	private int serviceID;
	
	@Column(name = "rating", precision = 2, scale = 1)
	private double rating;
	
	@Column(name = "bookingNo", columnDefinition = "INT DEFAULT 0")
	private int bookingNo;
	
	@Column(name = "unitPrice", nullable = false)
	private long unitPrice;
	
	@Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(2500)")
	private String description;
	
	@Column(name = "availability", nullable = false)
	private boolean availability;
	
	@Column(name = "visible", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean visible;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "providerID", referencedColumnName = "providerID", nullable = false, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
	
	@ManyToOne(targetEntity = GiGService.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "serviceID", referencedColumnName = "serviceID")
	@JsonIgnore
	@ToString.Exclude
	private GiGService service;
	
	@OneToMany(targetEntity = Booking.class, mappedBy = "providerService")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Booking> bookingList;
	
	@OneToMany(targetEntity = Image.class, mappedBy = "providerService")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Image> imageList;
}
