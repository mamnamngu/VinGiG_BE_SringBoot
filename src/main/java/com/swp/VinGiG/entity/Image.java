package com.swp.VinGiG.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Table(name = "Image")
public class Image implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "imageID")
	private long imageID;
	
	@Column(name = "link", nullable = false, columnDefinition = "NVARCHAR(150)")
	private String link;
	
//	@Column(name = "proServiceID", insertable = false, updatable = false, nullable = false)
//	private long proServiceID;
	
	//RELATIONSHIP SETUP
	
	@ManyToOne(targetEntity = ProviderService.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "proServiceID", referencedColumnName = "proServiceID", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@ToString.Exclude
	private ProviderService providerService;
}
