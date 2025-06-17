package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.repository.RoomObjects.WindowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités Window.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/data-tables")
@RequiredArgsConstructor
public class WindowController {

    private final WindowRepository WindowRepository;

    /**
     * Récupère toutes les entités Window.
     * @return Liste de toutes les Windows.
     */
    @GetMapping
    public ResponseEntity<List<Window>> getAllWindows() {
        return ResponseEntity.ok(WindowRepository.findAll());
    }

    /**
     * Récupère une Window par son identifiant.
     * @param id Identifiant de la Window.
     * @return Window si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Window> getWindowById(@PathVariable Integer id) {
        return WindowRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les Windows associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Windows dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Window>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(WindowRepository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les Windows ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Windows correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Window>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(WindowRepository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Window.
     * @param Window Objet à créer.
     * @return Window créée.
     */
    @PostMapping
    public ResponseEntity<Window> createWindow(@RequestBody Window Window) {
        return ResponseEntity.ok(WindowRepository.save(Window));
    }

    /**
     * Met à jour une Window existante.
     * @param Window Objet à mettre à jour.
     * @return Window mise à jour.
     */
    @PutMapping
    public ResponseEntity<Window> updateWindow(@RequestBody Window Window) {
        return ResponseEntity.ok(WindowRepository.save(Window));
    }

    /**
     * Supprime une Window par son identifiant.
     * @param id ID de la Window à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWindow(@PathVariable Integer id) {
        if (!WindowRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        WindowRepository.deleteById(id);
        return ResponseEntity.ok("Window supprimée avec succès");
    }

    /**
     * Supprime toutes les Windows d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Number roomId) {
        WindowRepository.deleteByIdRoom(roomId);
        return ResponseEntity.ok("Toutes les Windows de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les Windows avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        WindowRepository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Windows avec ce nom ont été supprimées");
    }
}
