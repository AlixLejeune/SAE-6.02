package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.repository.RoomObjects.LampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LampManager {

    private final LampRepository lampRepository;

    @Autowired
    public LampManager(LampRepository lampRepository) {
        this.lampRepository = lampRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Lamp
     */
    public Lamp save(Lamp lamp) {
        return lampRepository.save(lamp);
    }

    /**
     * Créer ou mettre à jour une liste de Lamps
     */
    public List<Lamp> saveAll(List<Lamp> lamps) {
        return lampRepository.saveAll(lamps);
    }

    // ========= READ =========

    /**
     * Récupérer une Lamp par ID
     */
    public Lamp findById(Integer id) {
        return lampRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune lampe trouvée avec l'ID : " + id));
    }

    /**
     * Récupérer toutes les Lamps
     */
    public List<Lamp> findAll() {
        return lampRepository.findAll();
    }

    /**
     * Récupérer les Lamps par ID de salle
     */
    public List<Lamp> findByRoomId(Integer roomId) {
        return lampRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Lamps via l'objet Room (ID de Room)
     */
    public List<Lamp> findByRoomId(Long roomId) {
        return lampRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Lamps par nom personnalisé
     */
    public List<Lamp> findByCustomName(String customName) {
        return lampRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Lamp par ID
     */
    public void deleteById(Integer id) {
        lampRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Lamp
     */
    public void delete(Lamp lamp) {
        lampRepository.delete(lamp);
    }

    /**
     * Supprimer toutes les Lamps
     */
    public void deleteAll() {
        lampRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Lamps d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        lampRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Lamps par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        lampRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Lamp par ID
     */
    public boolean existsById(Integer id) {
        return lampRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Lamps
     */
    public long count() {
        return lampRepository.count();
    }
}
