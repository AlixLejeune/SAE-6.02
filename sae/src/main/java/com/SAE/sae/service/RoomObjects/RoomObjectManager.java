package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.RoomObject;
import com.SAE.sae.repository.RoomObjects.RoomObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomObjectManager {

    private final RoomObjectRepository RoomObjectRepository;

    @Autowired
    public RoomObjectManager(RoomObjectRepository RoomObjectRepository) {
        this.RoomObjectRepository = RoomObjectRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une RoomObject
     */
    public RoomObject save(RoomObject RoomObject) {
        return RoomObjectRepository.save(RoomObject);
    }

    /**
     * Créer ou mettre à jour une liste de RoomObjects
     */
    public List<RoomObject> saveAll(List<RoomObject> RoomObjects) {
        return RoomObjectRepository.saveAll(RoomObjects);
    }

    // ========= READ =========

    /**
     * Récupérer une RoomObject par ID
     */
    public Optional<RoomObject> findById(Integer id) {
        return RoomObjectRepository.findById(id);
    }

    /**
     * Récupérer toutes les RoomObjects
     */
    public List<RoomObject> findAll() {
        return RoomObjectRepository.findAll();
    }

    /**
     * Récupérer les RoomObjects par ID de salle
     */
    public List<RoomObject> findByIdRoom(Number roomId) {
        return RoomObjectRepository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les RoomObjects via l'objet Room
     */
    public List<RoomObject> findByRoomId(Long roomId) {
        return RoomObjectRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les RoomObjects par nom personnalisé
     */
    public List<RoomObject> findByCustomName(String customName) {
        return RoomObjectRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une RoomObject par ID
     */
    public void deleteById(Integer id) {
        RoomObjectRepository.deleteById(id);
    }

    /**
     * Supprimer un objet RoomObject
     */
    public void delete(RoomObject RoomObject) {
        RoomObjectRepository.delete(RoomObject);
    }

    /**
     * Supprimer toutes les RoomObjects
     */
    public void deleteAll() {
        RoomObjectRepository.deleteAll();
    }

    /**
     * Supprimer toutes les RoomObjects d'une salle
     */
    @Transactional
    public void deleteByIdRoom(Number roomId) {
        RoomObjectRepository.deleteByIdRoom(roomId);
    }

    /**
     * Supprimer les RoomObjects par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        RoomObjectRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une RoomObject par ID
     */
    public boolean existsById(Integer id) {
        return RoomObjectRepository.existsById(id);
    }

    /**
     * Compter le nombre total de RoomObjects
     */
    public long count() {
        return RoomObjectRepository.count();
    }
}
