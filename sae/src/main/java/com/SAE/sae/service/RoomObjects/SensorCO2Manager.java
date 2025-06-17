package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.repository.RoomObjects.SensorCO2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SensorCO2Manager {

    private final SensorCO2Repository SensorCO2Repository;

    @Autowired
    public SensorCO2Manager(SensorCO2Repository SensorCO2Repository) {
        this.SensorCO2Repository = SensorCO2Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une SensorCO2
     */
    public SensorCO2 save(SensorCO2 SensorCO2) {
        return SensorCO2Repository.save(SensorCO2);
    }

    /**
     * Créer ou mettre à jour une liste de SensorCO2s
     */
    public List<SensorCO2> saveAll(List<SensorCO2> SensorCO2s) {
        return SensorCO2Repository.saveAll(SensorCO2s);
    }

    // ========= READ =========

    /**
     * Récupérer une SensorCO2 par ID
     */
    public Optional<SensorCO2> findById(Integer id) {
        return SensorCO2Repository.findById(id);
    }

    /**
     * Récupérer toutes les SensorCO2s
     */
    public List<SensorCO2> findAll() {
        return SensorCO2Repository.findAll();
    }

    /**
     * Récupérer les SensorCO2s par ID de salle
     */
    public List<SensorCO2> findByIdRoom(Integer  roomId) {
        return SensorCO2Repository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les SensorCO2s via l'objet Room
     */
    public List<SensorCO2> findByRoomId(Long roomId) {
        return SensorCO2Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les SensorCO2s par nom personnalisé
     */
    public List<SensorCO2> findByCustomName(String customName) {
        return SensorCO2Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une SensorCO2 par ID
     */
    public void deleteById(Integer id) {
        SensorCO2Repository.deleteById(id);
    }

    /**
     * Supprimer un objet SensorCO2
     */
    public void delete(SensorCO2 SensorCO2) {
        SensorCO2Repository.delete(SensorCO2);
    }

    /**
     * Supprimer toutes les SensorCO2s
     */
    public void deleteAll() {
        SensorCO2Repository.deleteAll();
    }

    /**
     * Supprimer toutes les SensorCO2s d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        SensorCO2Repository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les SensorCO2s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        SensorCO2Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une SensorCO2 par ID
     */
    public boolean existsById(Integer id) {
        return SensorCO2Repository.existsById(id);
    }

    /**
     * Compter le nombre total de SensorCO2s
     */
    public long count() {
        return SensorCO2Repository.count();
    }
}
