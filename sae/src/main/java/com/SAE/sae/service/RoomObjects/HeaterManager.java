package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.repository.RoomObjects.HeaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HeaterManager {

    private final HeaterRepository heaterRepository;

    @Autowired
    public HeaterManager(HeaterRepository heaterRepository) {
        this.heaterRepository = heaterRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour un Heater
     */
    public Heater save(Heater heater) {
        return heaterRepository.save(heater);
    }

    /**
     * Créer ou mettre à jour une liste de Heaters
     */
    public List<Heater> saveAll(List<Heater> heaters) {
        return heaterRepository.saveAll(heaters);
    }

    // ========= READ =========

    /**
     * Récupérer un Heater par ID
     */
    public Heater findById(Integer id) {
        return heaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun chauffage trouvé avec l'ID : " + id));
    }

    /**
     * Récupérer tous les Heaters
     */
    public List<Heater> findAll() {
        return heaterRepository.findAll();
    }

    /**
     * Récupérer les Heaters par ID de salle
     */
    public List<Heater> findByRoomId(Integer roomId) {
        return heaterRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Heaters via l'objet Room
     */
    public List<Heater> findByRoomId(Long roomId) {
        return heaterRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Heaters par nom personnalisé
     */
    public List<Heater> findByCustomName(String customName) {
        return heaterRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer un Heater par ID
     */
    public void deleteById(Integer id) {
        heaterRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Heater
     */
    public void delete(Heater heater) {
        heaterRepository.delete(heater);
    }

    /**
     * Supprimer tous les Heaters
     */
    public void deleteAll() {
        heaterRepository.deleteAll();
    }

    /**
     * Supprimer les Heaters par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        heaterRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'un Heater par ID
     */
    public boolean existsById(Integer id) {
        return heaterRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Heaters
     */
    public long count() {
        return heaterRepository.count();
    }
}
