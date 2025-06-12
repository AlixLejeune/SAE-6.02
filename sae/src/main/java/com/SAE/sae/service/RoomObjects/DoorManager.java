package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.RoomObjects.DoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoorManager {

    private final DoorRepository DoorRepository;

    @Autowired
    public DoorManager(DoorRepository DoorRepository) {
        this.DoorRepository = DoorRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Door
     */
    public Door save(Door Door) {
        return DoorRepository.save(Door);
    }

    /**
     * Créer ou mettre à jour une liste de Doors
     */
    public List<Door> saveAll(List<Door> Doors) {
        return DoorRepository.saveAll(Doors);
    }

    // ========= READ =========

    /**
     * Récupérer une Door par ID
     */
    public Optional<Door> findById(Integer id) {
        return DoorRepository.findById(id);
    }

    /**
     * Récupérer toutes les Doors
     */
    public List<Door> findAll() {
        return DoorRepository.findAll();
    }

    /**
     * Récupérer les Doors par ID de salle
     */
    public List<Door> findByIdRoom(Number roomId) {
        return DoorRepository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les Doors via l'objet Room
     */
    public List<Door> findByRoomId(Long roomId) {
        return DoorRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Doors par nom personnalisé
     */
    public List<Door> findByCustomName(String customName) {
        return DoorRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Door par ID
     */
    public void deleteById(Integer id) {
        DoorRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Door
     */
    public void delete(Door Door) {
        DoorRepository.delete(Door);
    }

    /**
     * Supprimer toutes les Doors
     */
    public void deleteAll() {
        DoorRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Doors d'une salle
     */
    @Transactional
    public void deleteByIdRoom(Number roomId) {
        DoorRepository.deleteByIdRoom(roomId);
    }

    /**
     * Supprimer les Doors par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        DoorRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Door par ID
     */
    public boolean existsById(Integer id) {
        return DoorRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Doors
     */
    public long count() {
        return DoorRepository.count();
    }
}
