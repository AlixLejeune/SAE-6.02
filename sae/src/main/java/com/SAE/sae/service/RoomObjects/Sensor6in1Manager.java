package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.repository.RoomObjects.Sensor6in1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class Sensor6in1Manager {

    private final Sensor6in1Repository Sensor6in1Repository;

    @Autowired
    public Sensor6in1Manager(Sensor6in1Repository Sensor6in1Repository) {
        this.Sensor6in1Repository = Sensor6in1Repository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Sensor6in1
     */
    public Sensor6in1 save(Sensor6in1 Sensor6in1) {
        return Sensor6in1Repository.save(Sensor6in1);
    }

    /**
     * Créer ou mettre à jour une liste de Sensor6in1s
     */
    public List<Sensor6in1> saveAll(List<Sensor6in1> Sensor6in1s) {
        return Sensor6in1Repository.saveAll(Sensor6in1s);
    }

    // ========= READ =========

    /**
     * Récupérer une Sensor6in1 par ID
     */
    public Optional<Sensor6in1> findById(Integer id) {
        return Sensor6in1Repository.findById(id);
    }

    /**
     * Récupérer toutes les Sensor6in1s
     */
    public List<Sensor6in1> findAll() {
        return Sensor6in1Repository.findAll();
    }

    /**
     * Récupérer les Sensor6in1s par ID de salle
     */
    public List<Sensor6in1> findByRoomId(Integer  roomId) {
        return Sensor6in1Repository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Sensor6in1s via l'objet Room
     */
    public List<Sensor6in1> findByRoomId(Long roomId) {
        return Sensor6in1Repository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sensor6in1s par nom personnalisé
     */
    public List<Sensor6in1> findByCustomName(String customName) {
        return Sensor6in1Repository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Sensor6in1 par ID
     */
    public void deleteById(Integer id) {
        Sensor6in1Repository.deleteById(id);
    }

    /**
     * Supprimer un objet Sensor6in1
     */
    public void delete(Sensor6in1 Sensor6in1) {
        Sensor6in1Repository.delete(Sensor6in1);
    }

    /**
     * Supprimer toutes les Sensor6in1s
     */
    public void deleteAll() {
        Sensor6in1Repository.deleteAll();
    }

    /**
     * Supprimer toutes les Sensor6in1s d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        Sensor6in1Repository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Sensor6in1s par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        Sensor6in1Repository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Sensor6in1 par ID
     */
    public boolean existsById(Integer id) {
        return Sensor6in1Repository.existsById(id);
    }

    /**
     * Compter le nombre total de Sensor6in1s
     */
    public long count() {
        return Sensor6in1Repository.count();
    }
}
