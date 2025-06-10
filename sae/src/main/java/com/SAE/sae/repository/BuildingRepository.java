package com.SAE.sae.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SAE.sae.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Integer>{
    
}
