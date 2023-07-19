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

@Table(name = "Deposit")
public class Deposit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "depositID")
	private long depositID;
	
//	@Column(name = "providerID", insertable = false, nullable = false, updatable = false)
//	private long providerID;
	
	@Column(name = "amount", nullable = false, updatable = false)
	private long amount;
	
	@Column(name = "date", nullable = false, updatable = false)
	private Date date;
	
	@Column(name = "method", nullable = false, columnDefinition = "NVARCHAR(2000)")
	private String method;
	
	@Column(name = "success", nullable = false)
	private boolean success;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "providerID", referencedColumnName = "providerID", nullable = false, insertable = true, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
	
	@OneToMany(targetEntity = Transction.class, mappedBy = "deposit")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Transction> transactionList;
	
}
