package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.repository.RoomObjects.Sensor6in1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Sensor6in1Manager {

    private final Sensor6in1Repository sensor6in1Repository;

    @Autowired
    public Sensor6in1Manager(Sensor6in1Repository sensor6in1Repository) {
        this.sensor6in1Repository = sensor6in1Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour un Sensor6in1
     */
    public Sensor6in1 save(Sensor6in1 sensor6in1) {
        return sensor6in1Repository.save(sensor6in1);
    }

    /**
     * Créer ou mettre à jour une liste de Sensor6in1s
     */
    public List<Sensor6in1> saveAll(List<Sensor6in1> sensor6in1s) {
        return sensor6in1Repository.saveAll(sensor6in1s);
    }

    // ========= READ =========

    /**
     * Récupérer un Sensor6in1 par ID
     */
    public Sensor6in1 findById(Integer id) {
        return sensor6in1Repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun capteur 6-en-1 trouvé avec l'ID : " + id));
    }

    /**
     * Récupérer tous les Sensor6in1s
     */
    public List<Sensor6in1> findAll() {
        return sensor6in1Repository.findAll();
    }

    /**
     * Récupérer les Sensor6in1s par ID de salle
     */
    public List<Sensor6in1> findByRoomId(Integer roomId) {
        return sensor6in1Repository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Sensor6in1s via l'objet Room
     */
    public List<Sensor6in1> findByRoomId(Long roomId) {
        return sensor6in1Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sensor6in1s par nom personnalisé
     */
    public List<Sensor6in1> findByCustomName(String customName) {
        return sensor6in1Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer un Sensor6in1 par ID
     */
    public void deleteById(Integer id) {
        sensor6in1Repository.deleteById(id);
    }

    /**
     * Supprimer un objet Sensor6in1
     */
    public void delete(Sensor6in1 sensor6in1) {
        sensor6in1Repository.delete(sensor6in1);
    }

    /**
     * Supprimer tous les Sensor6in1s
     */
    public void deleteAll() {
        sensor6in1Repository.deleteAll();
    }

    /**
     * Supprimer les Sensor6in1s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        sensor6in1Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'un Sensor6in1 par ID
     */
    public boolean existsById(Integer id) {
        return sensor6in1Repository.existsById(id);
    }

    /**
     * Compter le nombre total de Sensor6in1s
     */
    public long count() {
        return sensor6in1Repository.count();
    }
}
