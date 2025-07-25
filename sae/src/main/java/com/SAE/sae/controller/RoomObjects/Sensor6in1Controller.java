package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.service.RoomObjects.Sensor6in1Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Sensor6in1.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/sensor6in1s")
@RequiredArgsConstructor
public class Sensor6in1Controller {

    private final Sensor6in1Manager sensor6in1Manager;

    /**
     * Récupère toutes les entités Sensor6in1.
     * @return Liste de toutes les Sensor6in1s.
     */
    @GetMapping
    public ResponseEntity<List<Sensor6in1>> getAllSensor6in1s() {
        return ResponseEntity.ok(sensor6in1Manager.findAll());
    }

    /**
     * Récupère une Sensor6in1 par son identifiant.
     * @param id Identifiant de la Sensor6in1.
     * @return Sensor6in1 si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sensor6in1> getDataTableById(@PathVariable Integer id) {
        try {
            Sensor6in1 sensor6in1 = sensor6in1Manager.findById(id);
            return ResponseEntity.ok(sensor6in1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère toutes les Sensor6in1s associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Sensor6in1s dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Sensor6in1>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(sensor6in1Manager.findByRoomId(roomId));
    }

    /**
     * Récupère toutes les Sensor6in1s ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Sensor6in1s correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Sensor6in1>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(sensor6in1Manager.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Sensor6in1.
     * @param Sensor6in1 Objet à créer.
     * @return Sensor6in1 créée.
     */
    @PostMapping
    public ResponseEntity<Sensor6in1> createSensor6in1(@RequestBody Sensor6in1 Sensor6in1) {
        return ResponseEntity.ok(sensor6in1Manager.save(Sensor6in1));
    }

    /**
     * Met à jour une Sensor6in1 existante.
     * @param Sensor6in1 Objet à mettre à jour.
     * @return Sensor6in1 mise à jour.
     */
    @PutMapping
    public ResponseEntity<Sensor6in1> updateSensor6in1(@RequestBody Sensor6in1 Sensor6in1) {
        return ResponseEntity.ok(sensor6in1Manager.save(Sensor6in1));
    }

    /**
     * Supprime une Sensor6in1 par son identifiant.
     * @param id ID de la Sensor6in1 à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSensor6in1(@PathVariable Integer id) {
        if (!sensor6in1Manager.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sensor6in1Manager.deleteById(id);
        return ResponseEntity.ok("Sensor6in1 supprimée avec succès");
    }

    /**
     * Supprime toutes les Sensor6in1s avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        sensor6in1Manager.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Sensor6in1s avec ce nom ont été supprimées");
    }
}
