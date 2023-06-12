package com.swp.VinGiG.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.entity.Building;
import com.swp.VinGiG.service.BuildingService;

@RestController
public class BuildingController {
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping("/buildings")
	public ResponseEntity<List<Building>> retrieveAllBuildings(){
		return ResponseEntity.ok(buildingService.findAll());
    }
	
	@GetMapping("/building/{id}")
	public ResponseEntity<Building> retrieveBuilding(@PathVariable int id) {
		Building building = buildingService.findById(id);
		if(building != null) {
			return ResponseEntity.status(HttpStatus.OK).body(building);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/buildings/{keyword}")
	public ResponseEntity<List<Building>> retrieveBuildingsByKeyword(@PathVariable String keyword){
		return ResponseEntity.ok(buildingService.findByNote(keyword));
    }
	
	@PostMapping("/building")
	public ResponseEntity<Building> createBuilding(@RequestBody Building building){
		try {
			Building savedBuilding = buildingService.add(building);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBuilding);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new building").build();
		}
	}
	
	@PutMapping("/building")
	public ResponseEntity<Building> updateBuilding(@RequestBody Building building){
		if(buildingService.findById(building.getBuildingID()) == null)
			return ResponseEntity.notFound().header("message", "No Building found for such ID").build();
		
		Building updatedBuilding = buildingService.update(building);
		if(updatedBuilding != null)
			return ResponseEntity.ok(updatedBuilding);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/building/{id}")
	public ResponseEntity<Void> deleteBuilding(@PathVariable int id){
		try{
			buildingService.delete(id);
			return ResponseEntity.noContent().header("message", "building deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "building deletion failed").build();
		}
	}
}
