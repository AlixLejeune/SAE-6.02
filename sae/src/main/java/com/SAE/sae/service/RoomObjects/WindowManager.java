package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.repository.RoomObjects.WindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WindowManager {

    private final WindowRepository windowRepository;

    @Autowired
    public WindowManager(WindowRepository windowRepository) {
        this.windowRepository = windowRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une Window
     */
    public Window save(Window window) {
        return windowRepository.save(window);
    }

    /**
     * Créer ou mettre à jour une liste de Windows
     */
    public List<Window> saveAll(List<Window> windows) {
        return windowRepository.saveAll(windows);
    }

    // ========= READ =========

    /**
     * Récupérer une Window par ID
     */
    public Window findById(Integer id) {
        return windowRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune fenêtre trouvée avec l'ID : " + id));
    }

    /**
     * Récupérer toutes les Windows
     */
    public List<Window> findAll() {
        return windowRepository.findAll();
    }

    /**
     * Récupérer les Windows par ID de salle
     */
    public List<Window> findByRoomId(Integer roomId) {
        return windowRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les Windows via l'objet Room
     */
    public List<Window> findByRoomId(Long roomId) {
        return windowRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les Windows par nom personnalisé
     */
    public List<Window> findByCustomName(String customName) {
        return windowRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une Window par ID
     */
    public void deleteById(Integer id) {
        windowRepository.deleteById(id);
    }

    /**
     * Supprimer un objet Window
     */
    public void delete(Window window) {
        windowRepository.delete(window);
    }

    /**
     * Supprimer toutes les Windows
     */
    public void deleteAll() {
        windowRepository.deleteAll();
    }

    /**
     * Supprimer toutes les Windows d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        windowRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les Windows par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        windowRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une Window par ID
     */
    public boolean existsById(Integer id) {
        return windowRepository.existsById(id);
    }

    /**
     * Compter le nombre total de Windows
     */
    public long count() {
        return windowRepository.count();
    }
}
