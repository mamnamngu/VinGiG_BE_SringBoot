package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Image;
import com.swp.VinGiG.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepo;
	
	//FIND
	public List<Image> findAll(){
		return imageRepo.findAll();
	}
	
	public Image findById(long id) {
		Optional<Image> image = imageRepo.findById(id);
		if(image.isPresent()) return image.get();
		else return null;
	}
	
	public List<Image> findByProviderServiceID(long proServiceID) {
		return imageRepo.findByProviderServiceProServiceID(proServiceID);
	}
	
	public Image findProviderServiceAvatarByProviderServiceID(long proServiceID) {
		return findByProviderServiceID(proServiceID).get(0);
	}
	
	//ADD
	public Image add(Image image) {
		return imageRepo.save(image);
	}
	
	//UPDATE
	public Image update(Image newImage) {
		return add(newImage);
	}
	
	//DELETE
	public boolean delete(long id) {
		imageRepo.deleteById(id);
		return imageRepo.findById(id).isEmpty();
	}
	
}
