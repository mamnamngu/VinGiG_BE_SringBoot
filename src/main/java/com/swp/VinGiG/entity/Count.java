package com.swp.VinGiG.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

@Table(name = "Count")
public class Count implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "countID")
	private long countID;
	
//	@Column(name = "providerID", insertable = false, nullable = false, updatable = false)
//	private long providerID;
	
	@Column(name = "count", nullable = false)
	private int count;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "providerID", referencedColumnName = "providerID", nullable = false)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
}
