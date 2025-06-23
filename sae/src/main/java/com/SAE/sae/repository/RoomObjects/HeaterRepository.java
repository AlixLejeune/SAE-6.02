package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Heater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HeaterRepository extends JpaRepository<Heater, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver tous les chauffages d'une salle spécifique
     */
    List<Heater> findByRoomId(Integer roomId);

    /**
     * Trouver tous les chauffages d'une salle (alternative avec l'objet Room)
     */
    List<Heater> findByRoom_Id(Long roomId);

    /**
     * Trouver par nom personnalisé exact
     */
    List<Heater> findByCustomName(String customName);

    /**
     * Supprimer les chauffages par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
