package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.repository.RoomObjects.Sensor9in1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Sensor9in1.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/data-tables")
@RequiredArgsConstructor
public class Sensor9in1Controller {

    private final Sensor9in1Repository Sensor9in1Repository;

    /**
     * Récupère toutes les entités Sensor9in1.
     * @return Liste de toutes les Sensor9in1s.
     */
    @GetMapping
    public ResponseEntity<List<Sensor9in1>> getAllSensor9in1s() {
        return ResponseEntity.ok(Sensor9in1Repository.findAll());
    }

    /**
     * Récupère une Sensor9in1 par son identifiant.
     * @param id Identifiant de la Sensor9in1.
     * @return Sensor9in1 si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sensor9in1> getSensor9in1ById(@PathVariable Integer id) {
        return Sensor9in1Repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les Sensor9in1s associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Sensor9in1s dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Sensor9in1>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(Sensor9in1Repository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les Sensor9in1s ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Sensor9in1s correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Sensor9in1>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(Sensor9in1Repository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Sensor9in1.
     * @param Sensor9in1 Objet à créer.
     * @return Sensor9in1 créée.
     */
    @PostMapping
    public ResponseEntity<Sensor9in1> createSensor9in1(@RequestBody Sensor9in1 Sensor9in1) {
        return ResponseEntity.ok(Sensor9in1Repository.save(Sensor9in1));
    }

    /**
     * Met à jour une Sensor9in1 existante.
     * @param Sensor9in1 Objet à mettre à jour.
     * @return Sensor9in1 mise à jour.
     */
    @PutMapping
    public ResponseEntity<Sensor9in1> updateSensor9in1(@RequestBody Sensor9in1 Sensor9in1) {
        return ResponseEntity.ok(Sensor9in1Repository.save(Sensor9in1));
    }

    /**
     * Supprime une Sensor9in1 par son identifiant.
     * @param id ID de la Sensor9in1 à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSensor9in1(@PathVariable Integer id) {
        if (!Sensor9in1Repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Sensor9in1Repository.deleteById(id);
        return ResponseEntity.ok("Sensor9in1 supprimée avec succès");
    }

    /**
     * Supprime toutes les Sensor9in1s d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Number roomId) {
        Sensor9in1Repository.deleteByIdRoom(roomId);
        return ResponseEntity.ok("Toutes les Sensor9in1s de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les Sensor9in1s avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        Sensor9in1Repository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Sensor9in1s avec ce nom ont été supprimées");
    }
}
