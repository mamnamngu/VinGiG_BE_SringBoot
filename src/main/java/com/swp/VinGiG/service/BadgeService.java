package com.swp.VinGiG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.VinGiG.entity.Badge;
import com.swp.VinGiG.repository.BadgeRepository;

@Service
public class BadgeService {

	@Autowired
	private BadgeRepository badgeRepo;
	
	//FIND
	public List<Badge> findAll(){
		return badgeRepo.findAll();
	}
	
	public Badge findById(int id) {
		Optional<Badge> badge = badgeRepo.findById(id);
		if(badge.isPresent()) return badge.get();
		else return null;
	}
	
	//ADD
	public Badge add(Badge badge) {
		return badgeRepo.save(badge);
	}
	
	//UPDATE
	public Badge update(Badge newBadge) {
		return add(newBadge);
	}
	
	//DELETE
	public boolean delete(int id) {
		badgeRepo.deleteById(id);
		return badgeRepo.findById(id).isEmpty();
	}
	
}
