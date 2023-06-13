package com.swp.VinGiG.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.swp.VinGiG.entity.Image;
import com.swp.VinGiG.entity.ProviderService;
import com.swp.VinGiG.service.ImageService;
import com.swp.VinGiG.service.ProviderServiceService;

@Controller
public class ImageController {

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ProviderServiceService providerServiceService;
	
	@GetMapping("/images")
	public ResponseEntity<List<Image>> retrieveAllImages(){
		return ResponseEntity.ok(imageService.findAll());
    }
	
	@GetMapping("/image/{id}")
	public ResponseEntity<Image> retrieveImage(@PathVariable long id) {
		Image image = imageService.findById(id);
		if(image != null) {
			return ResponseEntity.status(HttpStatus.OK).body(image);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/providerService/{id}/images")
	public ResponseEntity<List<Image>> retrieveAllImagesOfProviderService(@PathVariable long id){
		ProviderService providerService = providerServiceService.findById(id);
		if(providerService != null)
			return ResponseEntity.ok(imageService.findByProviderServiceID(id));
		else return ResponseEntity.notFound().build();
    }
	
	@PostMapping("/providerService/{id}/image")
	public ResponseEntity<Image> createImage(@PathVariable long id, @RequestBody Image image){
		try {
			ProviderService providerService = providerServiceService.findById(id);
			if(providerService == null) return ResponseEntity.notFound().header("message", "ProviderService not found. Adding failed").build();
			
			image.setProviderService(providerService);
			Image savedImage = imageService.add(image);
			if(savedImage != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new image").build();
		}
	}
	
	@PutMapping("/providerService/{id}/image")
	public ResponseEntity<Image> updateImage(@PathVariable long id, @RequestBody Image image){
		try {
			ProviderService providerService = providerServiceService.findById(id);
			if(providerService == null) return ResponseEntity.notFound().header("message", "ProviderService not found. Update failed").build();
			
			if(imageService.findById(image.getImageID()) == null) return ResponseEntity.notFound().header("message", "Image with such ID not found. Update failed").build();
			
			image.setProviderService(providerService);
			Image savedImage = imageService.update(image);
			if(savedImage != null)
				return ResponseEntity.status(HttpStatus.OK).body(savedImage);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to update image").build();
		}
	}
	
	@DeleteMapping("/image/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable long id){
		try{
			imageService.delete(id);
			return ResponseEntity.noContent().header("message", "Image deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Image deletion failed").build();
		}
	}
}
