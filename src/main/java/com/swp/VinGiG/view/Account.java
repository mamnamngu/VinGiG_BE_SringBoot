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
public class Account {
	private String username;
	private String password;
	private String email;
	private String phone;
	private String fullName;
	private boolean gender;
	private int buildingID;
	private String apartment;
	private int badgeID;
}
