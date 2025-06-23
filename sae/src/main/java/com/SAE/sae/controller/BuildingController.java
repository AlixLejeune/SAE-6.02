package com.SAE.sae.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SAE.sae.entity.Building;
import com.SAE.sae.repository.BuildingRepository;
import com.SAE.sae.service.BuildingManager;


import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour la gestion des building.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class BuildingController {
    
    private final BuildingManager buildingManager;

    /**
     * Récupère toutes les entités Building.
     * @return Liste de toutes les Buildings.
     */
    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingManager.getAllBuildings());
    }

    /**
     * Récupère une Building par son identifiant.
     * @param id Identifiant de la Building.
     * @return Building si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Integer id) {
        return ResponseEntity.ok(buildingManager.getBuildingById(id));
    }

    /**
     * Récupère toutes les Buildings ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Buildings correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Building>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(buildingManager.getBuildingByName(name));
    }

    /**
     * Crée une nouvelle Building.
     * @param Building Objet à créer.
     * @return Building créée.
     */
    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestBody Building Building) {
        return ResponseEntity.ok(buildingManager.saveBuilding(Building));
    }

    /**
     * Met à jour une Building existante.
     * @param Building Objet à mettre à jour.
     * @return Building mise à jour.
     */
    @PutMapping
    public ResponseEntity<Building> updateBuilding(@RequestBody Building Building) {
        return ResponseEntity.ok(buildingManager.updateBuilding(Building));
    }

    /**
     * Supprime un Building par son identifiant.
     * @param id ID de la Building à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Integer id) {
        buildingManager.deleteBuildingById(id);
        return ResponseEntity.ok("Building supprimé avec succès");
    }


    /**
     * Supprime toutes les Buildings avec un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByName(@RequestParam String name) {
        buildingManager.deleteBuildingsByName(name);
        return ResponseEntity.ok("Tous les Buildings avec ce nom ont été supprimées");
    }
}
