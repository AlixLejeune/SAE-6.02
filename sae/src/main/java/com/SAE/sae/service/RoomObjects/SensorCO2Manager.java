package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.repository.RoomObjects.SensorCO2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorCO2Manager {

    private final SensorCO2Repository sensorCO2Repository;

    @Autowired
    public SensorCO2Manager(SensorCO2Repository sensorCO2Repository) {
        this.sensorCO2Repository = sensorCO2Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour un SensorCO2
     */
    public SensorCO2 save(SensorCO2 sensorCO2) {
        return sensorCO2Repository.save(sensorCO2);
    }

    /**
     * Créer ou mettre à jour une liste de SensorCO2s
     */
    public List<SensorCO2> saveAll(List<SensorCO2> sensorCO2s) {
        return sensorCO2Repository.saveAll(sensorCO2s);
    }

    // ========= READ =========

    /**
     * Récupérer un SensorCO2 par ID
     */
    public SensorCO2 findById(Integer id) {
        return sensorCO2Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun capteur CO2 trouvé avec l'ID : " + id));
    }

    /**
     * Récupérer tous les SensorCO2s
     */
    public List<SensorCO2> findAll() {
        return sensorCO2Repository.findAll();
    }

    /**
     * Récupérer les SensorCO2s par ID de salle
     */
    public List<SensorCO2> findByRoomId(Integer roomId) {
        return sensorCO2Repository.findByRoomId(roomId);
    }

    /**
     * Récupérer les SensorCO2s via l'objet Room
     */
    public List<SensorCO2> findByRoomId(Long roomId) {
        return sensorCO2Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les SensorCO2s par nom personnalisé
     */
    public List<SensorCO2> findByCustomName(String customName) {
        return sensorCO2Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer un SensorCO2 par ID
     */
    public void deleteById(Integer id) {
        sensorCO2Repository.deleteById(id);
    }

    /**
     * Supprimer un objet SensorCO2
     */
    public void delete(SensorCO2 sensorCO2) {
        sensorCO2Repository.delete(sensorCO2);
    }

    /**
     * Supprimer tous les SensorCO2s
     */
    public void deleteAll() {
        sensorCO2Repository.deleteAll();
    }

    /**
     * Supprimer tous les SensorCO2s d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        sensorCO2Repository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les SensorCO2s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        sensorCO2Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'un SensorCO2 par ID
     */
    public boolean existsById(Integer id) {
        return sensorCO2Repository.existsById(id);
    }

    /**
     * Compter le nombre total de SensorCO2s
     */
    public long count() {
        return sensorCO2Repository.count();
    }
}
