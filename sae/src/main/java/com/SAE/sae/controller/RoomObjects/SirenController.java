package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.service.RoomObjects.SirenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Siren.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/sirens")
@RequiredArgsConstructor
public class SirenController {

    private final SirenManager sirenManager;

    /**
     * Récupère toutes les entités Siren.
     * @return Liste de toutes les Sirens.
     */
    @GetMapping
    public ResponseEntity<List<Siren>> getAllSirens() {
        return ResponseEntity.ok(sirenManager.findAll());
    }

    /**
     * Récupère une Siren par son identifiant.
     * @param id Identifiant de la Siren.
     * @return Siren si elle existe, sinon 404.
     */
   @GetMapping("/{id}")
    public ResponseEntity<Siren> getDataTableById(@PathVariable Integer id) {
        try {
            Siren siren = sirenManager.findById(id);
            return ResponseEntity.ok(siren);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère toutes les Sirens associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Sirens dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Siren>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(sirenManager.findByRoomId(roomId));
    }

    /**
     * Récupère toutes les Sirens ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Sirens correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Siren>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(sirenManager.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Siren.
     * @param Siren Objet à créer.
     * @return Siren créée.
     */
    @PostMapping
    public ResponseEntity<Siren> createSiren(@RequestBody Siren Siren) {
        return ResponseEntity.ok(sirenManager.save(Siren));
    }

    /**
     * Met à jour une Siren existante.
     * @param Siren Objet à mettre à jour.
     * @return Siren mise à jour.
     */
    @PutMapping
    public ResponseEntity<Siren> updateSiren(@RequestBody Siren Siren) {
        return ResponseEntity.ok(sirenManager.save(Siren));
    }

    /**
     * Supprime une Siren par son identifiant.
     * @param id ID de la Siren à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSiren(@PathVariable Integer id) {
        if (!sirenManager.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sirenManager.deleteById(id);
        return ResponseEntity.ok("Siren supprimée avec succès");
    }

    /**
     * Supprime toutes les Sirens avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        sirenManager.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Sirens avec ce nom ont été supprimées");
    }
}
