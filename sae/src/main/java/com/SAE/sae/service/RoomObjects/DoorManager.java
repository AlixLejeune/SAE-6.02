package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.RoomObjects.DoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoorManager {

    private final DoorRepository doorRepository;

    @Autowired
    public DoorManager(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Door
     */
    public Door save(Door door) {
        return doorRepository.save(door);
    }

    /**
     * Créer ou mettre à jour une liste de Doors
     */
    public List<Door> saveAll(List<Door> doors) {
        return doorRepository.saveAll(doors);
    }

    // ========= READ =========

    /**
     * Récupérer une Door par ID
     */
    public Door findById(Integer id) {
        return doorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune porte trouvée avec l'ID : " + id));
    }

    /**
     * Récupérer toutes les Doors
     */
    public List<Door> findAll() {
        return doorRepository.findAll();
    }

    /**
     * Récupérer les Doors par ID de salle
     */
    public List<Door> findByRoomId(Integer roomId) {
        return doorRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Doors via l'objet Room (ID de Room)
     */
    public List<Door> findByRoomId(Long roomId) {
        return doorRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Doors par nom personnalisé
     */
    public List<Door> findByCustomName(String customName) {
        return doorRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Door par ID
     */
    public void deleteById(Integer id) {
        doorRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Door
     */
    public void delete(Door door) {
        doorRepository.delete(door);
    }

    /**
     * Supprimer toutes les Doors
     */
    public void deleteAll() {
        doorRepository.deleteAll();
    }

    /**
     * Supprimer les Doors par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        doorRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Door par ID
     */
    public boolean existsById(Integer id) {
        return doorRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Doors
     */
    public long count() {
        return doorRepository.count();
    }
}
