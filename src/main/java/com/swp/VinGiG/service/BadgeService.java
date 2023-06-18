package com.swp.VinGiG.service;

import java.util.List;

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
		return badgeRepo.findByActiveIsTrue();
	}
	
	public Badge findById(int id) {
		Badge badge = badgeRepo.findByBadgeIDAndActiveIsTrue(id);
		return badge;
	}
	
	public List<Badge> findByNameOrDescription(String keyword){
		return badgeRepo.findByBadgeNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveIsTrue(keyword.trim(), keyword.trim());
	}
	
	public List<Badge> findDeletedBadge(){
		return badgeRepo.findByActiveIsFalse();
	}
	
	//ADD
	public Badge add(Badge badge) {
		return badgeRepo.save(badge);
	}
	
	//UPDATE
	public Badge update(Badge newBadge) {
		return add(newBadge);
	}
	
	//DELETE - DEACTIVATE
	public boolean delete(int id) {
		Badge badge = findById(id);
		if(badge == null) return false;
		badge.setActive(false);
		update(badge);
		return !badge.isActive();
	}
	
}
