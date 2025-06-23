package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.service.RoomObjects.WindowManager;
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
@RequestMapping("/api/v1/windows")
@RequiredArgsConstructor
public class WindowController {

    private final WindowManager windowManager;

    /**
     * Récupère toutes les entités Window.
     * @return Liste de toutes les Windows.
     */
    @GetMapping
    public ResponseEntity<List<Window>> getAllWindows() {
        return ResponseEntity.ok(windowManager.findAll());
    }

    /**
     * Récupère une Window par son identifiant.
     * @param id Identifiant de la Window.
     * @return Window si elle existe, sinon 404.
     */
      @GetMapping("/{id}")
    public ResponseEntity<Window> getDataTableById(@PathVariable Integer id) {
        try {
            Window window = windowManager.findById(id);
            return ResponseEntity.ok(window);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère toutes les Windows associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des Windows dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Window>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(windowManager.findByRoomId(roomId));
    }

    /**
     * Récupère toutes les Windows ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Windows correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Window>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(windowManager.findByCustomName(name));
    }

    /**
     * Crée une nouvelle Window.
     * @param Window Objet à créer.
     * @return Window créée.
     */
    @PostMapping
    public ResponseEntity<Window> createWindow(@RequestBody Window Window) {
        return ResponseEntity.ok(windowManager.save(Window));
    }

    /**
     * Met à jour une Window existante.
     * @param Window Objet à mettre à jour.
     * @return Window mise à jour.
     */
    @PutMapping
    public ResponseEntity<Window> updateWindow(@RequestBody Window Window) {
        return ResponseEntity.ok(windowManager.save(Window));
    }

    /**
     * Supprime une Window par son identifiant.
     * @param id ID de la Window à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWindow(@PathVariable Integer id) {
        if (!windowManager.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        windowManager.deleteById(id);
        return ResponseEntity.ok("Window supprimée avec succès");
    }

    /**
     * Supprime toutes les Windows avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        windowManager.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les Windows avec ce nom ont été supprimées");
    }
}
