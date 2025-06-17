package com.SAE.sae.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SAE.sae.entity.RoomType;
import com.SAE.sae.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour la gestion des entités RoomType.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/room_types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeRepository roomTypeRepository;

    /**
     * Récupère toutes les entités RoomType.
     * @return Liste de toutes les RoomTypes.
     */
    @GetMapping
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeRepository.findAll());
    }

    /**
     * Récupère une RoomType par son identifiant.
     * @param id Identifiant de la RoomType.
     * @return RoomType si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Integer id) {
        return roomTypeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle RoomType.
     * @param roomType Objet à créer.
     * @return RoomType créée.
     */
    @PostMapping
    public ResponseEntity<RoomType> createRoomType(@RequestBody RoomType roomType) {
        return ResponseEntity.ok(roomTypeRepository.save(roomType));
    }

    /**
     * Met à jour une RoomType existante.
     * @param roomType Objet à mettre à jour.
     * @return RoomType mise à jour.
     */
    @PutMapping
    public ResponseEntity<RoomType> updateRoomType(@RequestBody RoomType roomType) {
        return ResponseEntity.ok(roomTypeRepository.save(roomType));
    }

    /**
     * Supprime une RoomType par son identifiant.
     * @param id ID de la RoomType à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoomType(@PathVariable Integer id) {
        if (!roomTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomTypeRepository.deleteById(id);
        return ResponseEntity.ok("RoomType supprimée avec succès");
    }

}

