package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.repository.RoomObjects.SirenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SirenManager {

    private final SirenRepository SirenRepository;

    @Autowired
    public SirenManager(SirenRepository SirenRepository) {
        this.SirenRepository = SirenRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Siren
     */
    public Siren save(Siren Siren) {
        return SirenRepository.save(Siren);
    }

    /**
     * Créer ou mettre à jour une liste de Sirens
     */
    public List<Siren> saveAll(List<Siren> Sirens) {
        return SirenRepository.saveAll(Sirens);
    }

    // ========= READ =========

    /**
     * Récupérer une Siren par ID
     */
    public Optional<Siren> findById(Integer id) {
        return SirenRepository.findById(id);
    }

    /**
     * Récupérer toutes les Sirens
     */
    public List<Siren> findAll() {
        return SirenRepository.findAll();
    }

    /**
     * Récupérer les Sirens par ID de salle
     */
    public List<Siren> findByIdRoom(Number roomId) {
        return SirenRepository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les Sirens via l'objet Room
     */
    public List<Siren> findByRoomId(Long roomId) {
        return SirenRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sirens par nom personnalisé
     */
    public List<Siren> findByCustomName(String customName) {
        return SirenRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Siren par ID
     */
    public void deleteById(Integer id) {
        SirenRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Siren
     */
    public void delete(Siren Siren) {
        SirenRepository.delete(Siren);
    }

    /**
     * Supprimer toutes les Sirens
     */
    public void deleteAll() {
        SirenRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Sirens d'une salle
     */
    @Transactional
    public void deleteByIdRoom(Number roomId) {
        SirenRepository.deleteByIdRoom(roomId);
    }

    /**
     * Supprimer les Sirens par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        SirenRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Siren par ID
     */
    public boolean existsById(Integer id) {
        return SirenRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Sirens
     */
    public long count() {
        return SirenRepository.count();
    }
}
