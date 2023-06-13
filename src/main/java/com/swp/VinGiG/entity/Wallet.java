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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Table(name = "Wallet")
public class Wallet implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "walletID")
	private long walletID;
	
//	@Column(name = "providerID", nullable = false, insertable = false, updatable = false)
//	private long providerID;
	
	@Column(name = "balance", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
	private long balance;
	
	@Column(name = "createDate", nullable = false)
	private Date createDate;
	
	@Column(name = "active", columnDefinition = "BIT DEFAULT 1", nullable = false)
	private boolean active;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "providerID", referencedColumnName = "providerID", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private Provider provider;
	
	@OneToMany(targetEntity = Transaction.class, mappedBy = "wallet")
	@JsonIgnore
	@ToString.Exclude
	private Collection<Transaction> transactionList;
}
