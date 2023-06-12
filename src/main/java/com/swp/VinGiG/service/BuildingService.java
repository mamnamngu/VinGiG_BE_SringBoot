package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.repository.BuildingRepository;

@Service
public class BuildingService {

	@Autowired
	private BuildingRepository buildingRepo;
	
	//FIND
	public List<Building> findAll(){
		return buildingRepo.findAll();
	}
	
	public Building findById(int id) {
		Optional<Building> building = buildingRepo.findById(id);
		if(building.isPresent()) return building.get();
		else return null;
	}
	
	public List<Building> findByNote(String note){
		return buildingRepo.findByNoteContainingIgnoreCase(note);
	}
	
	//ADD
	public Building add(Building building) {
		return buildingRepo.save(building);
	}
	
	//UPDATE
	public Building update(Building newBuilding) {
		return add(newBuilding);
	}
	
	//DELETE
	public boolean delete(int id) {
		buildingRepo.deleteById(id);
		return buildingRepo.findById(id).isEmpty();
	}
}
