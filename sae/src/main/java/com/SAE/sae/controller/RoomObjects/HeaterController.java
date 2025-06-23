package com.SAE.sae.controller.RoomObjects;

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

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.service.RoomObjects.HeaterManager;

import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour la gestion des entités Heater.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/heaters")
@RequiredArgsConstructor
public class HeaterController {

    private final HeaterManager heaterManager;

    /**
     * Récupère toutes les entités Heater.
     * @return Liste de toutes les Heaters.
     */
    @GetMapping
    public ResponseEntity<List<Heater>> getAllHeaters() {
        return ResponseEntity.ok(heaterManager.findAll());
    }

    /**
     * Récupère une Heater par son identifiant.
     * @param id Identifiant de la Heater.
     * @return Heater si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Heater> getDataTableById(@PathVariable Integer id) {
        try {
            Heater heater = heaterManager.findById(id);
            return ResponseEntity.ok(heater);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère toutes les Heaters associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Heaters dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Heater>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(heaterManager.findByRoomId(roomId));
    }

    /**
     * Récupère toutes les Heaters ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Heaters correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Heater>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(heaterManager.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Heater.
     * @param Heater Objet à créer.
     * @return Heater créée.
     */
    @PostMapping
    public ResponseEntity<Heater> createHeater(@RequestBody Heater Heater) {
        return ResponseEntity.ok(heaterManager.save(Heater));
    }

    /**
     * Met à jour une Heater existante.
     * @param Heater Objet à mettre à jour.
     * @return Heater mise à jour.
     */
    @PutMapping
    public ResponseEntity<Heater> updateHeater(@RequestBody Heater Heater) {
        return ResponseEntity.ok(heaterManager.save(Heater));
    }

    /**
     * Supprime une Heater par son identifiant.
     * @param id ID de la Heater à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHeater(@PathVariable Integer id) {
        if (!heaterManager.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        heaterManager.deleteById(id);
        return ResponseEntity.ok("Heater supprimée avec succès");
    }

    /**
     * Supprime toutes les Heaters avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        heaterManager.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Heaters avec ce nom ont été supprimées");
    }
}
