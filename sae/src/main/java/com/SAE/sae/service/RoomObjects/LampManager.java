package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.repository.RoomObjects.LampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LampManager {

    private final LampRepository LampRepository;

    @Autowired
    public LampManager(LampRepository LampRepository) {
        this.LampRepository = LampRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Lamp
     */
    public Lamp save(Lamp Lamp) {
        return LampRepository.save(Lamp);
    }

    /**
     * Créer ou mettre à jour une liste de Lamps
     */
    public List<Lamp> saveAll(List<Lamp> Lamps) {
        return LampRepository.saveAll(Lamps);
    }

    // ========= READ =========

    /**
     * Récupérer une Lamp par ID
     */
    public Optional<Lamp> findById(Integer id) {
        return LampRepository.findById(id);
    }

    /**
     * Récupérer toutes les Lamps
     */
    public List<Lamp> findAll() {
        return LampRepository.findAll();
    }

    /**
     * Récupérer les Lamps par ID de salle
     */
    public List<Lamp> findByRoomId(Integer  roomId) {
        return LampRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Lamps via l'objet Room
     */
    public List<Lamp> findByRoomId(Long roomId) {
        return LampRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Lamps par nom personnalisé
     */
    public List<Lamp> findByCustomName(String customName) {
        return LampRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Lamp par ID
     */
    public void deleteById(Integer id) {
        LampRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Lamp
     */
    public void delete(Lamp Lamp) {
        LampRepository.delete(Lamp);
    }

    /**
     * Supprimer toutes les Lamps
     */
    public void deleteAll() {
        LampRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Lamps d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        LampRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Lamps par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        LampRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Lamp par ID
     */
    public boolean existsById(Integer id) {
        return LampRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Lamps
     */
    public long count() {
        return LampRepository.count();
    }
}
