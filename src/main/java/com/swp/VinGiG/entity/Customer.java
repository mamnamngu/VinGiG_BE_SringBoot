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
@Table(name = "Customer")

public class Customer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerID")
	private long customerID;
	
//	@Column(name = "buildingID", insertable = false, updatable = true, nullable = false)
//	private int buildingID;
	
	@Column(name = "username", unique = true, nullable = false)
	@Size(min = Constants.USERNAME_MIN, message = "username must have at least 6 characters")
	private String username;
	
	@Column(name = "password", nullable = false)
	@Size(min = Constants.PASSWORD_MIN, message = "password must have at least 6 characters")
	private String password;
	
	@Column(name = "fullName", nullable = false)
	private String fullName;
	
	@Column(name = "dob")
	private Date dob;
	
	@Column(name = "avatar", columnDefinition = "NVARCHAR(150) DEFAULT '" + Constants.DEFAULT_AVATAR + "'")
	private String avatar;
	
	@Column(name = "gender", nullable = false)
	private boolean gender;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "createDate", nullable = false)
	private Date createDate;
	
	@Column(name = "rating", precision = 2, scale = 1)
	private double rating;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	@Column(name = "role", columnDefinition = "NVARCHAR(10) DEFAULT 'customer'", nullable = false)
	private boolean role;
	
	//RELATIONSHIP SETUP
	
	@OneToMany(targetEntity = Booking.class, mappedBy = "customer")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Booking> bookingList;
	
	@ManyToOne(targetEntity = Building.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingID", referencedColumnName = "buildingID", nullable = false)
	@JsonIgnore
	@ToString.Exclude
	private Building building;
}
