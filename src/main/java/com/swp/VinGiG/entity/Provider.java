package com.swp.VinGiG.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "Provider")

public class Provider implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "providerID")
	private long providerID;
	
	@Column(name = "username", unique = true, nullable = false)
	@Size(min = Constants.USERNAME_MIN, message = "username must have at least 6 characters")
	private String username;
	
	@Column(name = "password", nullable = false)
	@Size(min = Constants.PASSWORD_MIN, message = "password must have at least 6 characters")
	private String password;
	
	@Column(name = "gender", nullable = false)
	private boolean gender;
	
//	@Column(name = "buildingID",nullable = false)
//	private int buildingID;
//	
//	@Column(name = "badgeID", nullable = false, columnDefinition = "INT DEFAULT " + Constants.DEFAULT_BADGE)
//	private int badgeID;
	
	@Column(name = "avatar", columnDefinition = "NVARCHAR(150) DEFAULT '" + Constants.DEFAULT_AVATAR + "'")
	private String avatar;
	
	@Column(name = "rating", precision = 2, scale = 1)
	private double rating;
	
	@Column(name = "fullName", nullable = false)
	private String fullName;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "createDate", nullable = false)
	private Date createDate;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	@Column(name = "role", columnDefinition = "NVARCHAR(10) DEFAULT 'provider'", nullable = false)
	private boolean role;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Badge.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "badgeID", referencedColumnName = "badgeID", nullable = false)
	@JsonIgnore
	@ToString.Exclude
	private Badge badge;
	
	@ManyToOne(targetEntity = Building.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingID", referencedColumnName = "buildingID", nullable = false)
	@JsonIgnore
	@ToString.Exclude
	private Building building;
	
	@OneToMany(targetEntity = Wallet.class, mappedBy = "provider", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private Collection<Wallet> walletList;
	
	@OneToMany(targetEntity = ProviderService.class, mappedBy = "provider", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private Collection<ProviderService> providerServiceList;
	
	@OneToMany(targetEntity = Count.class, mappedBy = "provider", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private Collection<Count> countList;
	
	@OneToMany(targetEntity = Deposit.class, mappedBy = "provider")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Deposit> depositList;
	
	@OneToMany(targetEntity = SubscriptionFee.class, mappedBy = "provider")
	@JsonIgnore
	@ToString.Exclude
	private Collection<SubscriptionFee> subscriptionFeeList;
}
