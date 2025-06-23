package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.service.RoomObjects.LampManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Lamp.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/lamps")
@RequiredArgsConstructor
public class LampController {

    private final LampManager lampManager;

    /**
     * Récupère toutes les entités Lamp.
     * 
     * @return Liste de toutes les Lamps.
     */
    @GetMapping
    public ResponseEntity<List<Lamp>> getAllLamps() {
        return ResponseEntity.ok(lampManager.findAll());
    }

    /**
     * Récupère une Lamp par son identifiant.
     * 
     * @param id Identifiant de la Lamp.
     * @return Lamp si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lamp> getDataTableById(@PathVariable Integer id) {
        try {
            Lamp lamp = lampManager.findById(id);
            return ResponseEntity.ok(lamp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère toutes les Lamps associées à une salle (via l'ID de la salle).
     * 
     * @param roomId ID de la salle.
     * @return Liste des Lamps dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Lamp>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(lampManager.findByRoomId(roomId));
    }

    /**
     * Récupère toutes les Lamps ayant un nom personnalisé spécifique.
     * 
     * @param name Nom personnalisé.
     * @return Liste des Lamps correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Lamp>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(lampManager.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Lamp.
     * 
     * @param Lamp Objet à créer.
     * @return Lamp créée.
     */
    @PostMapping
    public ResponseEntity<Lamp> createLamp(@RequestBody Lamp Lamp) {
        return ResponseEntity.ok(lampManager.save(Lamp));
    }

    /**
     * Met à jour une Lamp existante.
     * 
     * @param Lamp Objet à mettre à jour.
     * @return Lamp mise à jour.
     */
    @PutMapping
    public ResponseEntity<Lamp> updateLamp(@RequestBody Lamp Lamp) {
        return ResponseEntity.ok(lampManager.save(Lamp));
    }

    /**
     * Supprime une Lamp par son identifiant.
     * 
     * @param id ID de la Lamp à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLamp(@PathVariable Integer id) {
        if (!lampManager.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        lampManager.deleteById(id);
        return ResponseEntity.ok("Lamp supprimée avec succès");
    }

    /**
     * Supprime toutes les Lamps avec un nom personnalisé spécifique.
     * 
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        lampManager.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Lamps avec ce nom ont été supprimées");
    }
}
