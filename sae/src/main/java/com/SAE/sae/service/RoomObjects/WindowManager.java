package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.repository.RoomObjects.WindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WindowManager {

    private final WindowRepository WindowRepository;

    @Autowired
    public WindowManager(WindowRepository WindowRepository) {
        this.WindowRepository = WindowRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Window
     */
    public Window save(Window Window) {
        return WindowRepository.save(Window);
    }

    /**
     * Créer ou mettre à jour une liste de Windows
     */
    public List<Window> saveAll(List<Window> Windows) {
        return WindowRepository.saveAll(Windows);
    }

    // ========= READ =========

    /**
     * Récupérer une Window par ID
     */
    public Optional<Window> findById(Integer id) {
        return WindowRepository.findById(id);
    }

    /**
     * Récupérer toutes les Windows
     */
    public List<Window> findAll() {
        return WindowRepository.findAll();
    }

    /**
     * Récupérer les Windows par ID de salle
     */
    public List<Window> findByRoomId(Integer  roomId) {
        return WindowRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Windows via l'objet Room
     */
    public List<Window> findByRoomId(Long roomId) {
        return WindowRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Windows par nom personnalisé
     */
    public List<Window> findByCustomName(String customName) {
        return WindowRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Window par ID
     */
    public void deleteById(Integer id) {
        WindowRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Window
     */
    public void delete(Window Window) {
        WindowRepository.delete(Window);
    }

    /**
     * Supprimer toutes les Windows
     */
    public void deleteAll() {
        WindowRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Windows d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        WindowRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Windows par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        WindowRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Window par ID
     */
    public boolean existsById(Integer id) {
        return WindowRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Windows
     */
    public long count() {
        return WindowRepository.count();
    }
}
