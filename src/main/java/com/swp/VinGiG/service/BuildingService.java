package com.swp.VinGiG.service;

import java.util.List;

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
		return buildingRepo.findByActiveIsTrue();
	}
	
	public Building findById(int id) {
		Building building = buildingRepo.findByBuildingIDAndActiveIsTrue(id);
		return building;
	}
	
	public List<Building> findByNote(String note){
		return buildingRepo.findByNoteContainingIgnoreCaseAndActiveIsTrue(note);
	}
	
	public List<Building> findDeletedBuilding(){
		return buildingRepo.findByActiveIsFalse();
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
		Building building = findById(id);
		if(building == null) return false;
		building.setActive(false);
		update(building);
		return !building.isActive();
	}
}
