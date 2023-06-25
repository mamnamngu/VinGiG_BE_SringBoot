package com.swp.VinGiG.view;

import java.util.Date;

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
public class ProviderObject {
	//Provider
	private long providerID;
	private boolean gender;
	private String avatar;
	private double rating;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private Date createDate;
	private boolean active;
	private String role;
	
	//Badge
	private int badgeID;
	private String badgeName;
	
	//Building
	private int buildingID;
	private String buildingName;
}
