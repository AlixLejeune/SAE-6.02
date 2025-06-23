package com.SAE.sae.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SAE.sae.entity.Building;
import com.SAE.sae.repository.BuildingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildingManager {
    private final BuildingRepository buildingRepo;

    public List<Building> getAllBuildings(){
        return buildingRepo.findAll();
    }
    public Building getBuildingById(int id){
        Optional<Building> optionalbuilding = buildingRepo.findById(id);
        if(optionalbuilding.isPresent()){
            log.debug("building {} has been returned", id);
            return optionalbuilding.get();
        }
        log.error("building with id {} doesn't exist.", id);
        return null;
    }

    public Building saveBuilding (Building building){
        Building savedbuilding = buildingRepo.save(building);
        log.info("building with id {} saved wucessfully.", building.getId());
        return savedbuilding;
    }

    public Building updateBuilding(Building building){
        Optional<Building> existingBuilding = buildingRepo.findById(building.getId());

        Building updatedbuilding = buildingRepo.save(building);
        log.info("building with id {} has been updated sucessfully.", building.getId());

        return updatedbuilding;
    }

    public void deleteBuildingById(int id){
        buildingRepo.deleteById(id);
        log.info("building with id {} has been deleted sucessfully.", id);
    }

    public List<Building> getBuildingByName(String name){
        log.info("Return all buildings named " + name);
        return buildingRepo.findByName(name);
    }

    public void deleteBuildingsByName(String name){
        log.info("Deleted all buildings named " + name);
        buildingRepo.deleteByName(name);
    }
}
