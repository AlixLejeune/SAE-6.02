package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.RoomObject;
import com.SAE.sae.repository.RoomObjects.RoomObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomObjectManager {

    private final RoomObjectRepository roomObjectRepository;

    @Autowired
    public RoomObjectManager(RoomObjectRepository roomObjectRepository) {
        this.roomObjectRepository = roomObjectRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour un RoomObject
     */
    public RoomObject save(RoomObject roomObject) {
        return roomObjectRepository.save(roomObject);
    }

    /**
     * Créer ou mettre à jour une liste de RoomObjects
     */
    public List<RoomObject> saveAll(List<RoomObject> roomObjects) {
        return roomObjectRepository.saveAll(roomObjects);
    }

    // ========= READ =========

    /**
     * Récupérer un RoomObject par ID
     */
    public RoomObject findById(Integer id) {
        return roomObjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun objet de salle trouvé avec l'ID : " + id));
    }

    /**
     * Récupérer tous les RoomObjects
     */
    public List<RoomObject> findAll() {
        return roomObjectRepository.findAll();
    }

    /**
     * Récupérer les RoomObjects par ID de salle
     */
    public List<RoomObject> findByRoomId(Integer roomId) {
        return roomObjectRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les RoomObjects via l'objet Room
     */
    public List<RoomObject> findByRoomId(Long roomId) {
        return roomObjectRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les RoomObjects par nom personnalisé
     */
    public List<RoomObject> findByCustomName(String customName) {
        return roomObjectRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer un RoomObject par ID
     */
    public void deleteById(Integer id) {
        roomObjectRepository.deleteById(id);
    }

    /**
     * Supprimer un objet RoomObject
     */
    public void delete(RoomObject roomObject) {
        roomObjectRepository.delete(roomObject);
    }

    /**
     * Supprimer tous les RoomObjects
     */
    public void deleteAll() {
        roomObjectRepository.deleteAll();
    }

    /**
     * Supprimer les RoomObjects par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        roomObjectRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'un RoomObject par ID
     */
    public boolean existsById(Integer id) {
        return roomObjectRepository.existsById(id);
    }

    /**
     * Compter le nombre total de RoomObjects
     */
    public long count() {
        return roomObjectRepository.count();
    }
}
