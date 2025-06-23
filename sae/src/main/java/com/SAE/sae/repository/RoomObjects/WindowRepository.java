package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WindowRepository extends JpaRepository<Window, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les fenêtres d'une salle spécifique
     */
    List<Window> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les fenêtres d'une salle (alternative avec l'objet Room)
     */
    List<Window> findByRoom_Id(Long roomId);

    /**
     * Trouver les fenêtres par nom personnalisé exact
     */
    List<Window> findByCustomName(String customName);

    /**
     * Supprimer les fenêtres par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
