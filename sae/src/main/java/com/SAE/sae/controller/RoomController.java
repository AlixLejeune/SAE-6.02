package com.SAE.sae.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomManager;

import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour la gestion des entités Room.
 * Fournit les opérations CRUD et des recherches spécifiques.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomManager roomManager;

    /**
     * Récupère toutes les entités Room.
     * @return Liste de toutes les Rooms.
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomManager.getAllRooms());
    }

    /**
     * Récupère une Room par son identifiant.
     * @param id Identifiant de la Room.
     * @return Room si elle existe, sinon 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomManager.getRoomById(id));
    }

    /**
     * Récupère toutes les Rooms ayant un nom personnalisé spécifique.
     * @param name Nom personnalisé.
     * @return Liste des Rooms correspondantes.
     */
    @GetMapping("/by-custom-name")
    public ResponseEntity<List<Room>> getByName(@RequestParam String name) {
        return  ResponseEntity.ok(roomManager.getRoomsByName(name));
    }

    /**
     * Crée une nouvelle Room.
     * @param room Objet à créer.
     * @return Room créée.
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomManager.saveRoom(room));
    }

    /**
     * Met à jour une Room existante.
     * @param room Objet à mettre à jour.
     * @return Room mise à jour.
     */
    @PutMapping
    public ResponseEntity<Room> updateRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomManager.updateRoom(room));
    }

    /**
     * Supprime une Room par son identifiant.
     * @param id ID de la Room à supprimer.
     * @return Message de confirmation ou erreur 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer id) {
        roomManager.deleteRoomById(id);
        return ResponseEntity.ok("Room supprimée avec succès");
    }

}

