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
public class CountObject {

	//Count
	private long countID;
	private int count;
	private boolean active;
	
	//Provider
	private long providerID;
	private String fullName;
}
