package com.swp.VinGiG.Entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Customer")

public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerID")
	private long customerID;
	
	@Column(name = "buidingID", insertable = false, updatable = false)
	private int buidingID;
	
	@Column(name = "username", unique = true, nullable = false)
	@Size(min = Constants.USERNAME_MIN, message = "username must have at least 6 characters")
	private String username;
	
	@Column(name = "password", nullable = false)
	@Size(min = Constants.PASSWORD_MIN, message = "username must have at least 6 characters")
	private String password;
	
	@Column(name = "fullName", nullable = false)
	private String fullName;
	
	@Column(name = "dob")
	private Date dob;
	
	@Column(name = "avatar", columnDefinition = "STRING DEFAULT " + Constants.DEFAULT_AVATAR)
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
	
	//RELATIONSHIP SETUP
	@OneToMany(targetEntity = Booking.class, mappedBy = "customer")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Booking> bookingList;
}
