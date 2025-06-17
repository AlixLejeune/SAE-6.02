package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.repository.RoomObjects.HeaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HeaterManager {

    private final HeaterRepository HeaterRepository;

    @Autowired
    public HeaterManager(HeaterRepository HeaterRepository) {
        this.HeaterRepository = HeaterRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Heater
     */
    public Heater save(Heater Heater) {
        return HeaterRepository.save(Heater);
    }

    /**
     * Créer ou mettre à jour une liste de Heaters
     */
    public List<Heater> saveAll(List<Heater> Heaters) {
        return HeaterRepository.saveAll(Heaters);
    }

    // ========= READ =========

    /**
     * Récupérer une Heater par ID
     */
    public Optional<Heater> findById(Integer id) {
        return HeaterRepository.findById(id);
    }

    /**
     * Récupérer toutes les Heaters
     */
    public List<Heater> findAll() {
        return HeaterRepository.findAll();
    }

    /**
     * Récupérer les Heaters par ID de salle
     */
    public List<Heater> findByRoomId(Integer  roomId) {
        return HeaterRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Heaters via l'objet Room
     */
    public List<Heater> findByRoomId(Long roomId) {
        return HeaterRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Heaters par nom personnalisé
     */
    public List<Heater> findByCustomName(String customName) {
        return HeaterRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Heater par ID
     */
    public void deleteById(Integer id) {
        HeaterRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Heater
     */
    public void delete(Heater Heater) {
        HeaterRepository.delete(Heater);
    }

    /**
     * Supprimer toutes les Heaters
     */
    public void deleteAll() {
        HeaterRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Heaters d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        HeaterRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Heaters par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        HeaterRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Heater par ID
     */
    public boolean existsById(Integer id) {
        return HeaterRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Heaters
     */
    public long count() {
        return HeaterRepository.count();
    }
}
