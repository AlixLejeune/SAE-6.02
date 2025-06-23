package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Door;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DoorRepository extends JpaRepository<Door, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les portes d'une salle spécifique
     */
    List<Door> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les portes d'une salle (alternative avec l'objet Room)
     */
    List<Door> findByRoom_Id(Long roomId);

    /**
     * Trouver par nom personnalisé exact
     */
    List<Door> findByCustomName(String customName);

    /**
     * Supprimer les portes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
