package com.swp.VinGiG.Entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "Provider")

public class Provider {

	@Id
	@GeneratedValue
	@Column(name = "providerID")
	private long providerID;
	
	@Column(name = "username", unique = true, nullable = false)
	@Size(min = 6, message = "username must have at least 6 characters")
	private String username;
	
	@Column(name = "password", nullable = false)
	@Size(min = 6, message = "password must have at least 6 characters")
	private String password;
	
	@Column(name = "buildingID", insertable = false, updatable = false, nullable = false)
	private int buildingID;
	
	@Column(name = "badgeID", insertable = false, updatable = false, nullable = false, columnDefinition = "INT DEFAULT " + Constants.DEFAULT_BADGE)
	private int badgeID;
	
	@Column(name = "avatar", columnDefinition = "STRING DEFAULT " + Constants.DEFAULT_AVATAR)
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
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Badge.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JsonIgnore
	@ToString.Exclude
	private Badge badge;
	
	@OneToMany(targetEntity = Wallet.class, mappedBy = "provider")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Wallet> walletList;
}
