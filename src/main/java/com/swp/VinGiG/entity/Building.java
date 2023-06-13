package com.swp.VinGiG.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Building")

public class Building implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "buildingID")
	private int buildingID;
	
	@Column(name = "buildingName", nullable = false)
	private String buildingName;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	//RELATIONSHIP SETUP
	
	@OneToMany(targetEntity = Customer.class, mappedBy = "building")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Customer> customerList;
	
	@OneToMany(targetEntity = Provider.class, mappedBy = "building")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Provider> providerList;
	
	@OneToMany(targetEntity = Booking.class, mappedBy = "building")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Booking> bookingList;
}
