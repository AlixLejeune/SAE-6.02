package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.repository.RoomObjects.PlugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlugManager {

    private final PlugRepository PlugRepository;

    @Autowired
    public PlugManager(PlugRepository PlugRepository) {
        this.PlugRepository = PlugRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Plug
     */
    public Plug save(Plug Plug) {
        return PlugRepository.save(Plug);
    }

    /**
     * Créer ou mettre à jour une liste de Plugs
     */
    public List<Plug> saveAll(List<Plug> Plugs) {
        return PlugRepository.saveAll(Plugs);
    }

    // ========= READ =========

    /**
     * Récupérer une Plug par ID
     */
    public Optional<Plug> findById(Integer id) {
        return PlugRepository.findById(id);
    }

    /**
     * Récupérer toutes les Plugs
     */
    public List<Plug> findAll() {
        return PlugRepository.findAll();
    }

    /**
     * Récupérer les Plugs par ID de salle
     */
    public List<Plug> findByIdRoom(Number roomId) {
        return PlugRepository.findByIdRoom(roomId);
    }

    /**
     * Récupérer les Plugs via l'objet Room
     */
    public List<Plug> findByRoomId(Long roomId) {
        return PlugRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Plugs par nom personnalisé
     */
    public List<Plug> findByCustomName(String customName) {
        return PlugRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Plug par ID
     */
    public void deleteById(Integer id) {
        PlugRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Plug
     */
    public void delete(Plug Plug) {
        PlugRepository.delete(Plug);
    }

    /**
     * Supprimer toutes les Plugs
     */
    public void deleteAll() {
        PlugRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Plugs d'une salle
     */
    @Transactional
    public void deleteByIdRoom(Number roomId) {
        PlugRepository.deleteByIdRoom(roomId);
    }

    /**
     * Supprimer les Plugs par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        PlugRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Plug par ID
     */
    public boolean existsById(Integer id) {
        return PlugRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Plugs
     */
    public long count() {
        return PlugRepository.count();
    }
}
