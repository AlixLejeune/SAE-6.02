package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.repository.RoomObjects.SensorCO2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités SensorCO2.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/sensorco2s")
@RequiredArgsConstructor
public class SensorCO2Controller {

    private final SensorCO2Repository SensorCO2Repository;

    /**
     * Récupère toutes les entités SensorCO2.
     * @return Liste de toutes les SensorCO2s.
     */
    @GetMapping
    public ResponseEntity<List<SensorCO2>> getAllSensorCO2s() {
        return ResponseEntity.ok(SensorCO2Repository.findAll());
    }

    /**
     * Récupère une SensorCO2 par son identifiant.
     * @param id Identifiant de la SensorCO2.
     * @return SensorCO2 si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SensorCO2> getSensorCO2ById(@PathVariable Integer id) {
        return SensorCO2Repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les SensorCO2s associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des SensorCO2s dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<SensorCO2>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(SensorCO2Repository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les SensorCO2s ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des SensorCO2s correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<SensorCO2>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(SensorCO2Repository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle SensorCO2.
     * @param SensorCO2 Objet à créer.
     * @return SensorCO2 créée.
     */
    @PostMapping
    public ResponseEntity<SensorCO2> createSensorCO2(@RequestBody SensorCO2 SensorCO2) {
        return ResponseEntity.ok(SensorCO2Repository.save(SensorCO2));
    }

    /**
     * Met à jour une SensorCO2 existante.
     * @param SensorCO2 Objet à mettre à jour.
     * @return SensorCO2 mise à jour.
     */
    @PutMapping
    public ResponseEntity<SensorCO2> updateSensorCO2(@RequestBody SensorCO2 SensorCO2) {
        return ResponseEntity.ok(SensorCO2Repository.save(SensorCO2));
    }

    /**
     * Supprime une SensorCO2 par son identifiant.
     * @param id ID de la SensorCO2 à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSensorCO2(@PathVariable Integer id) {
        if (!SensorCO2Repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        SensorCO2Repository.deleteById(id);
        return ResponseEntity.ok("SensorCO2 supprimée avec succès");
    }

    /**
     * Supprime toutes les SensorCO2s d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
      @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Integer roomId) {
        SensorCO2Repository.deleteByRoomId(roomId);
        return ResponseEntity.ok("Toutes les SensorCO2s de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les SensorCO2s avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        SensorCO2Repository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les SensorCO2s avec ce nom ont été supprimées");
    }
}
