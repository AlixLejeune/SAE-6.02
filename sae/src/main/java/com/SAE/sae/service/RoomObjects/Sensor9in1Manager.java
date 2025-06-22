package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.repository.RoomObjects.Sensor9in1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Sensor9in1Manager {

    private final Sensor9in1Repository sensor9in1Repository;

    @Autowired
    public Sensor9in1Manager(Sensor9in1Repository sensor9in1Repository) {
        this.sensor9in1Repository = sensor9in1Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour un Sensor9in1
     */
    public Sensor9in1 save(Sensor9in1 sensor9in1) {
        return sensor9in1Repository.save(sensor9in1);
    }

    /**
     * Créer ou mettre à jour une liste de Sensor9in1s
     */
    public List<Sensor9in1> saveAll(List<Sensor9in1> sensor9in1s) {
        return sensor9in1Repository.saveAll(sensor9in1s);
    }

    // ========= READ =========

    /**
     * Récupérer un Sensor9in1 par ID
     */
    public Sensor9in1 findById(Integer id) {
        return sensor9in1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun capteur 9-en-1 trouvé avec l'ID : " + id));
    }

    /**
     * Récupérer tous les Sensor9in1s
     */
    public List<Sensor9in1> findAll() {
        return sensor9in1Repository.findAll();
    }

    /**
     * Récupérer les Sensor9in1s par ID de salle
     */
    public List<Sensor9in1> findByRoomId(Integer roomId) {
        return sensor9in1Repository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Sensor9in1s via l'objet Room
     */
    public List<Sensor9in1> findByRoomId(Long roomId) {
        return sensor9in1Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sensor9in1s par nom personnalisé
     */
    public List<Sensor9in1> findByCustomName(String customName) {
        return sensor9in1Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer un Sensor9in1 par ID
     */
    public void deleteById(Integer id) {
        sensor9in1Repository.deleteById(id);
    }

    /**
     * Supprimer un objet Sensor9in1
     */
    public void delete(Sensor9in1 sensor9in1) {
        sensor9in1Repository.delete(sensor9in1);
    }

    /**
     * Supprimer tous les Sensor9in1s
     */
    public void deleteAll() {
        sensor9in1Repository.deleteAll();
    }

    /**
     * Supprimer tous les Sensor9in1s d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        sensor9in1Repository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Sensor9in1s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        sensor9in1Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'un Sensor9in1 par ID
     */
    public boolean existsById(Integer id) {
        return sensor9in1Repository.existsById(id);
    }

    /**
     * Compter le nombre total de Sensor9in1s
     */
    public long count() {
        return sensor9in1Repository.count();
    }
}
