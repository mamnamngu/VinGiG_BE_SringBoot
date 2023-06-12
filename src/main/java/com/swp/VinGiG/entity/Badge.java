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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Table(name = "Badge")
public class Badge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "badgeID")
	private int badgeID;
	
	@Column(name = "badgeName", nullable = false)
	private String badgeName;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "benefit", precision = 2, scale = 1)
	private double benefit;
	
	//RELATIONSHIP SETUP
	
	@OneToMany(targetEntity = Provider.class, mappedBy = "badge")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Provider> providerList;
	
}
