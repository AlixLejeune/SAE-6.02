package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.RoomObjects.DoorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Door.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/data-tables")
@RequiredArgsConstructor
public class DoorController {

    private final DoorRepository DoorRepository;

    /**
     * Récupère toutes les entités Door.
     * @return Liste de toutes les Doors.
     */
    @GetMapping
    public ResponseEntity<List<Door>> getAllDoors() {
        return ResponseEntity.ok(DoorRepository.findAll());
    }

    /**
     * Récupère une Door par son identifiant.
     * @param id Identifiant de la Door.
     * @return Door si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Door> getDoorById(@PathVariable Integer id) {
        return DoorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les Doors associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Doors dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Door>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(DoorRepository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les Doors ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Doors correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Door>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(DoorRepository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Door.
     * @param Door Objet à créer.
     * @return Door créée.
     */
    @PostMapping
    public ResponseEntity<Door> createDoor(@RequestBody Door Door) {
        return ResponseEntity.ok(DoorRepository.save(Door));
    }

    /**
     * Met à jour une Door existante.
     * @param Door Objet à mettre à jour.
     * @return Door mise à jour.
     */
    @PutMapping
    public ResponseEntity<Door> updateDoor(@RequestBody Door Door) {
        return ResponseEntity.ok(DoorRepository.save(Door));
    }

    /**
     * Supprime une Door par son identifiant.
     * @param id ID de la Door à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoor(@PathVariable Integer id) {
        if (!DoorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        DoorRepository.deleteById(id);
        return ResponseEntity.ok("Door supprimée avec succès");
    }

    /**
     * Supprime toutes les Doors d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Number roomId) {
        DoorRepository.deleteByIdRoom(roomId);
        return ResponseEntity.ok("Toutes les Doors de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les Doors avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        DoorRepository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Doors avec ce nom ont été supprimées");
    }
}
