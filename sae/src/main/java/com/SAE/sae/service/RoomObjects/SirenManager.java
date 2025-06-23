package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.repository.RoomObjects.SirenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SirenManager {

    private final SirenRepository sirenRepository;

    @Autowired
    public SirenManager(SirenRepository sirenRepository) {
        this.sirenRepository = sirenRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Siren
     */
    public Siren save(Siren siren) {
        return sirenRepository.save(siren);
    }

    /**
     * Créer ou mettre à jour une liste de Sirens
     */
    public List<Siren> saveAll(List<Siren> sirens) {
        return sirenRepository.saveAll(sirens);
    }

    // ========= READ =========

    /**
     * Récupérer une Siren par ID
     */
    public Siren findById(Integer id) {
        return sirenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune sirène trouvée avec l'ID : " + id));
    }

    /**
     * Récupérer toutes les Sirens
     */
    public List<Siren> findAll() {
        return sirenRepository.findAll();
    }

    /**
     * Récupérer les Sirens par ID de salle
     */
    public List<Siren> findByRoomId(Integer roomId) {
        return sirenRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Sirens via l'objet Room
     */
    public List<Siren> findByRoomId(Long roomId) {
        return sirenRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Sirens par nom personnalisé
     */
    public List<Siren> findByCustomName(String customName) {
        return sirenRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Siren par ID
     */
    public void deleteById(Integer id) {
        sirenRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Siren
     */
    public void delete(Siren siren) {
        sirenRepository.delete(siren);
    }

    /**
     * Supprimer toutes les Sirens
     */
    public void deleteAll() {
        sirenRepository.deleteAll();
    }

    /**
     * Supprimer les Sirens par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        sirenRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Siren par ID
     */
    public boolean existsById(Integer id) {
        return sirenRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Sirens
     */
    public long count() {
        return sirenRepository.count();
    }
}
