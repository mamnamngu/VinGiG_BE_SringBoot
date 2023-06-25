package com.swp.VinGiG.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class GiGServiceObject {
	//GiGService
	private int serviceID;
	private String serviceName;
	private String description;
	private String unit;
	private long priceMin;
	private long priceMax;
	private long fee;
	private boolean active;
	
	//Service Category
	private int categoryID;
	private String categoryName;
}
