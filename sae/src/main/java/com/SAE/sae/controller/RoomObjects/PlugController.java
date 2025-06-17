package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.repository.RoomObjects.PlugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Plug.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/plugs")
@RequiredArgsConstructor
public class PlugController {

    private final PlugRepository PlugRepository;

    /**
     * Récupère toutes les entités Plug.
     * @return Liste de toutes les Plugs.
     */
    @GetMapping
    public ResponseEntity<List<Plug>> getAllPlugs() {
        return ResponseEntity.ok(PlugRepository.findAll());
    }

    /**
     * Récupère une Plug par son identifiant.
     * @param id Identifiant de la Plug.
     * @return Plug si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plug> getPlugById(@PathVariable Integer id) {
        return PlugRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les Plugs associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Plugs dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Plug>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(PlugRepository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les Plugs ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Plugs correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Plug>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(PlugRepository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Plug.
     * @param Plug Objet à créer.
     * @return Plug créée.
     */
    @PostMapping
    public ResponseEntity<Plug> createPlug(@RequestBody Plug Plug) {
        return ResponseEntity.ok(PlugRepository.save(Plug));
    }

    /**
     * Met à jour une Plug existante.
     * @param Plug Objet à mettre à jour.
     * @return Plug mise à jour.
     */
    @PutMapping
    public ResponseEntity<Plug> updatePlug(@RequestBody Plug Plug) {
        return ResponseEntity.ok(PlugRepository.save(Plug));
    }

    /**
     * Supprime une Plug par son identifiant.
     * @param id ID de la Plug à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlug(@PathVariable Integer id) {
        if (!PlugRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        PlugRepository.deleteById(id);
        return ResponseEntity.ok("Plug supprimée avec succès");
    }

    /**
     * Supprime toutes les Plugs d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
     @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Integer roomId) {
        PlugRepository.deleteByRoomId(roomId);
        return ResponseEntity.ok("Toutes les DataTables de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les Plugs avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        PlugRepository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Plugs avec ce nom ont été supprimées");
    }
}
