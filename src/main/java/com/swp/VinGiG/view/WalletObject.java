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

public class WalletObject {
	//Wallet
	private long walletID;
	private long balance;
	private Date createDate;
	private boolean active;
	
	//Provider
	private long providerID;
	private String fullName;
	
}
