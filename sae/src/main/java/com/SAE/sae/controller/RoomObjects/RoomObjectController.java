package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.RoomObject;
import com.SAE.sae.repository.RoomObjects.RoomObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les objets de type RoomObject.
 * Il permet les opérations CRUD de base et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/room-objects")
@RequiredArgsConstructor
public class RoomObjectController {

    private final RoomObjectRepository roomObjectRepository;

    /**
     * Récupère tous les objets de type RoomObject.
     * @return Liste de tous les objets.
     */
    @GetMapping
    public ResponseEntity<List<RoomObject>> getAllRoomObjects() {
        return ResponseEntity.ok(roomObjectRepository.findAll());
    }

    /**
     * Récupère un objet RoomObject par son identifiant.
     * @param id L'identifiant de l'objet.
     * @return L'objet correspondant ou une réponse 404 s'il n'existe pas.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomObject> getRoomObjectById(@PathVariable Integer id) {
        return roomObjectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les objets associés à une salle à partir de son ID.
     * @param roomId ID de la salle.
     * @return Liste d'objets appartenant à la salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<RoomObject>> getObjectsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomObjectRepository.findByRoom_Id(roomId));
    }

    /**
     * Récupère tous les objets portant un nom personnalisé spécifique.
     * @param name Le nom personnalisé à rechercher.
     * @return Liste d'objets ayant ce nom personnalisé.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<RoomObject>> getObjectsByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(roomObjectRepository.findByCustomName(name));
    }

    /**
     * Crée un nouvel objet RoomObject.
     * @param roomObject L'objet à créer.
     * @return L'objet nouvellement créé.
     */
    @PostMapping
    public ResponseEntity<RoomObject> createRoomObject(@RequestBody RoomObject roomObject) {
        return ResponseEntity.ok(roomObjectRepository.save(roomObject));
    }

    /**
     * Met à jour un objet existant ou le crée s'il n'existe pas.
     * @param roomObject L'objet à mettre à jour.
     * @return L'objet mis à jour.
     */
    @PutMapping
    public ResponseEntity<RoomObject> updateRoomObject(@RequestBody RoomObject roomObject) {
        return ResponseEntity.ok(roomObjectRepository.save(roomObject));
    }

    /**
     * Supprime un objet à partir de son ID.
     * @param id L'identifiant de l'objet à supprimer.
     * @return Message de confirmation ou 404 si non trouvé.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoomObject(@PathVariable Integer id) {
        if (!roomObjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomObjectRepository.deleteById(id);
        return ResponseEntity.ok("Objet supprimé avec succès");
    }

    /**
     * Supprime tous les objets associés à une salle spécifique.
     * @param roomId L'ID de la salle.
     * @return Message de confirmation.
     */
      @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Integer roomId) {
        roomObjectRepository.deleteByRoomId(roomId);
        return ResponseEntity.ok("Toutes les DataTables de la salle ont été supprimées");
    }

    /**
     * Supprime tous les objets ayant un nom personnalisé spécifique.
     * @param customName Le nom personnalisé à supprimer.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        roomObjectRepository.deleteByCustomName(customName);
        return ResponseEntity.ok("Objets avec ce nom supprimés");
    }
}
