package com.swp.VinGiG.view;

import com.swp.VinGiG.entity.Image;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.entity.ProviderService;

import lombok.AllArgsConstructor;
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
public class ProviderServiceObject {
	private ProviderService providerService;
	private Provider provider;
	private Image image;
}
