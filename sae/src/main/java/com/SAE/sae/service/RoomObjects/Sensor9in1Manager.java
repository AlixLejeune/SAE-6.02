package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.repository.RoomObjects.Sensor9in1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class Sensor9in1Manager {

    private final Sensor9in1Repository Sensor9in1Repository;

    @Autowired
    public Sensor9in1Manager(Sensor9in1Repository Sensor9in1Repository) {
        this.Sensor9in1Repository = Sensor9in1Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Sensor9in1
     */
    public Sensor9in1 save(Sensor9in1 Sensor9in1) {
        return Sensor9in1Repository.save(Sensor9in1);
    }

    /**
     * Créer ou mettre à jour une liste de Sensor9in1s
     */
    public List<Sensor9in1> saveAll(List<Sensor9in1> Sensor9in1s) {
        return Sensor9in1Repository.saveAll(Sensor9in1s);
    }

    // ========= READ =========

    /**
     * Récupérer une Sensor9in1 par ID
     */
    public Optional<Sensor9in1> findById(Integer id) {
        return Sensor9in1Repository.findById(id);
    }

    /**
     * Récupérer toutes les Sensor9in1s
     */
    public List<Sensor9in1> findAll() {
        return Sensor9in1Repository.findAll();
    }

    /**
     * Récupérer les Sensor9in1s par ID de salle
     */
    public List<Sensor9in1> findByIdRoom(Number roomId) {
        return Sensor9in1Repository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les Sensor9in1s via l'objet Room
     */
    public List<Sensor9in1> findByRoomId(Long roomId) {
        return Sensor9in1Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sensor9in1s par nom personnalisé
     */
    public List<Sensor9in1> findByCustomName(String customName) {
        return Sensor9in1Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Sensor9in1 par ID
     */
    public void deleteById(Integer id) {
        Sensor9in1Repository.deleteById(id);
    }

    /**
     * Supprimer un objet Sensor9in1
     */
    public void delete(Sensor9in1 Sensor9in1) {
        Sensor9in1Repository.delete(Sensor9in1);
    }

    /**
     * Supprimer toutes les Sensor9in1s
     */
    public void deleteAll() {
        Sensor9in1Repository.deleteAll();
    }

    /**
     * Supprimer toutes les Sensor9in1s d'une salle
     */
    @Transactional
    public void deleteByIdRoom(Number roomId) {
        Sensor9in1Repository.deleteByIdRoom(roomId);
    }

    /**
     * Supprimer les Sensor9in1s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        Sensor9in1Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Sensor9in1 par ID
     */
    public boolean existsById(Integer id) {
        return Sensor9in1Repository.existsById(id);
    }

    /**
     * Compter le nombre total de Sensor9in1s
     */
    public long count() {
        return Sensor9in1Repository.count();
    }
}
