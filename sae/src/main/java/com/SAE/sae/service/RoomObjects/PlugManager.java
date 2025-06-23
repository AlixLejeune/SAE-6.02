package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.repository.RoomObjects.PlugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlugManager {

    private final PlugRepository plugRepository;

    @Autowired
    public PlugManager(PlugRepository plugRepository) {
        this.plugRepository = plugRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Plug
     */
    public Plug save(Plug plug) {
        return plugRepository.save(plug);
    }

    /**
     * Créer ou mettre à jour une liste de Plugs
     */
    public List<Plug> saveAll(List<Plug> plugs) {
        return plugRepository.saveAll(plugs);
    }

    // ========= READ =========

    /**
     * Récupérer une Plug par ID
     */
    public Plug findById(Integer id) {
        return plugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune prise trouvée avec l'ID : " + id));
    }

    /**
     * Récupérer toutes les Plugs
     */
    public List<Plug> findAll() {
        return plugRepository.findAll();
    }

    /**
     * Récupérer les Plugs par ID de salle
     */
    public List<Plug> findByRoomId(Integer roomId) {
        return plugRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Plugs via l'objet Room (ID de Room)
     */
    public List<Plug> findByRoomId(Long roomId) {
        return plugRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Plugs par nom personnalisé
     */
    public List<Plug> findByCustomName(String customName) {
        return plugRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Plug par ID
     */
    public void deleteById(Integer id) {
        plugRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Plug
     */
    public void delete(Plug plug) {
        plugRepository.delete(plug);
    }

    /**
     * Supprimer toutes les Plugs
     */
    public void deleteAll() {
        plugRepository.deleteAll();
    }

    /**
     * Supprimer les Plugs par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        plugRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Plug par ID
     */
    public boolean existsById(Integer id) {
        return plugRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Plugs
     */
    public long count() {
        return plugRepository.count();
    }
}
