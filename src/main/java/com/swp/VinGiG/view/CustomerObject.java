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
public class CustomerObject {

	//Customer
	private long customerID;
	private String fullName;
	private Date dob;
	private String avatar;
	private boolean gender;
	private String email;
	private String phone;
	private String address;
	private Date createDate;
	private double rating;
	private boolean active;
	private String role;
	
	//Building
	private int buildingID;
	private String buildingName;
}
