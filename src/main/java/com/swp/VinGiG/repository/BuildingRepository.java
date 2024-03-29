package com.swp.VinGiG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swp.VinGiG.entity.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer>, JpaSpecificationExecutor<Building>{

	public Building findByBuildingIDAndActiveIsTrue(int buildingID);

	public List<Building> findByActiveIsFalse();
	
	public List<Building> findByActiveIsTrue();
	
	public List<Building> findByNoteContainingIgnoreCaseAndActiveIsTrue(String note);
	
}
