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
@Table(name = "ServiceCategory")

public class ServiceCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryID")
	private int categoryID;
	
	@Column(name = "categoryName", nullable = false)
	private String categoryName;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	//RELATIONSHIP SETUP
	
	@OneToMany(targetEntity = GiGService.class, mappedBy = "serviceCategory")
	@JsonIgnore
	@ToString.Exclude
	private Collection<GiGService> serviceList;
}
