package com.swp.VinGiG.Entity;

import java.util.Collection;

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

@Table(name = "Service")

public class Service {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serviceID")
	private int serviceID;
	
	@Column(name = "categoryID", insertable = false, updatable  = false)
	private int categoryID;
	
	@Column(name = "serviceName", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String serviceName;
	
	@Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(300)")
	private String description;
	
	@Column(name = "unit", nullable = false, columnDefinition = "NVARCHAR(20)")
	private String unit;
	
	@Column(name = "priceMin", nullable = false)
	private long priceMin;
	
	@Column(name = "priceMax", nullable = false)
	private long priceMax;
	
	@Column(name = "fee", nullable = false)
	private long fee;
	
	//RELATIONSHIP SETUP
	@ManyToOne(targetEntity = ServiceCategory.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
	@JsonIgnore
	@ToString.Exclude
	private ServiceCategory serviceCategory;
	
	@OneToMany(targetEntity = ProviderService.class, mappedBy = "service", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private Collection<ProviderService> providerServiceList;
}
