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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

@Table(name = "SubscriptionFee")
public class SubscriptionFee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subID")
	private long subID;
	
//	@Column(name = "providerID", nullable = false, insertable = false, updatable = false)
//	private long providerID;
//	
//	@Column(name = "planID", nullable = false, insertable = false, updatable = false)
//	private int planID;
	
	@Column(name = "date", nullable = false, updatable = false)
	private Date date;

	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "providerID", referencedColumnName = "providerID", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
	
	@ManyToOne(targetEntity = SubscriptionPlan.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "planID", referencedColumnName = "planID", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private SubscriptionPlan plan;
	
	@OneToMany(targetEntity = Transaction.class, mappedBy = "subscriptionFee")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Transaction> transactionList;
}
