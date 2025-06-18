package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.RoomObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomObjectRepository extends JpaRepository<RoomObject, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver tous les objets d'une salle spécifique
     */
    List<RoomObject> findByRoomId(Integer roomId);

    /**
     * Trouver tous les objets d'une salle (alternative avec l'objet Room)
     */
    List<RoomObject> findByRoom_Id(Long roomId);

    /**
     * Trouver les objets par nom personnalisé exact
     */
    List<RoomObject> findByCustomName(String customName);

    /**
     * Supprimer tous les objets d'une salle
     */
    @Modifying
    @Transactional
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les objets par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
