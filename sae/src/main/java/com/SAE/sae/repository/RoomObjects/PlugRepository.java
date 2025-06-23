package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Plug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlugRepository extends JpaRepository<Plug, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les prises d'une salle spécifique
     */
    List<Plug> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les prises d'une salle (alternative avec l'objet Room)
     */
    List<Plug> findByRoom_Id(Long roomId);

    /**
     * Trouver les prises par nom personnalisé exact
     */
    List<Plug> findByCustomName(String customName);


    /**
     * Supprimer les prises par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
