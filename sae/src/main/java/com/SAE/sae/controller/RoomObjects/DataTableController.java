package com.SAE.sae.controller.RoomObjects;

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.repository.RoomObjects.DataTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entités DataTable.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/data-tables")
@RequiredArgsConstructor
public class DataTableController {

    private final DataTableRepository dataTableRepository;

    /**
     * Récupère toutes les entités DataTable.
     * @return Liste de toutes les DataTables.
     */
    @GetMapping
    public ResponseEntity<List<DataTable>> getAllDataTables() {
        return ResponseEntity.ok(dataTableRepository.findAll());
    }

    /**
     * Récupère une DataTable par son identifiant.
     * @param id Identifiant de la DataTable.
     * @return DataTable si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataTable> getDataTableById(@PathVariable Integer id) {
        return dataTableRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les DataTables associées à une salle (via l'ID de la salle).
     * @param roomId ID de la salle.
     * @return Liste des DataTables dans cette salle.
     */
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<DataTable>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(dataTableRepository.findByRoom_Id(roomId));
    }

    /**
     * Récupère toutes les DataTables ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des DataTables correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<DataTable>> getByCustomName(@RequestParam String name) {
        return ResponseEntity.ok(dataTableRepository.findByCustomName(name));
    }

    /**
     * Crée une nouvelle DataTable.
     * @param dataTable Objet à créer.
     * @return DataTable créée.
     */
    @PostMapping
    public ResponseEntity<DataTable> createDataTable(@RequestBody DataTable dataTable) {
        return ResponseEntity.ok(dataTableRepository.save(dataTable));
    }

    /**
     * Met à jour une DataTable existante.
     * @param dataTable Objet à mettre à jour.
     * @return DataTable mise à jour.
     */
    @PutMapping
    public ResponseEntity<DataTable> updateDataTable(@RequestBody DataTable dataTable) {
        return ResponseEntity.ok(dataTableRepository.save(dataTable));
    }

    /**
     * Supprime une DataTable par son identifiant.
     * @param id ID de la DataTable à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDataTable(@PathVariable Integer id) {
        if (!dataTableRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dataTableRepository.deleteById(id);
        return ResponseEntity.ok("DataTable supprimée avec succès");
    }

    /**
     * Supprime toutes les DataTables d'une salle spécifique.
     * @param roomId ID de la salle.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-room/{roomId}")
    @Transactional
    public ResponseEntity<String> deleteByRoomId(@PathVariable Number roomId) {
        dataTableRepository.deleteByIdRoom(roomId);
        return ResponseEntity.ok("Toutes les DataTables de la salle ont été supprimées");
    }

    /**
     * Supprime toutes les DataTables avec un nom personnalisé spécifique.
     * @param customName Nom personnalisé.
     * @return Message de confirmation.
     */
    @DeleteMapping("/by-custom-name")
    @Transactional
    public ResponseEntity<String> deleteByCustomName(@RequestParam String customName) {
        dataTableRepository.deleteByCustomName(customName);
        return ResponseEntity.ok("Toutes les DataTables avec ce nom ont été supprimées");
    }
}
