package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SensorCO2Repository extends JpaRepository<SensorCO2, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver tous les capteurs CO₂ d'une salle spécifique
     */
    List<SensorCO2> findByRoomId(Integer roomId);

    /**
     * Trouver tous les capteurs CO₂ d'une salle (alternative avec l'objet Room)
     */
    List<SensorCO2> findByRoom_Id(Long roomId);

    /**
     * Trouver les capteurs CO₂ par nom personnalisé exact
     */
    List<SensorCO2> findByCustomName(String customName);

    /**
     * Supprimer tous les capteurs CO₂ d'une salle
     */
    @Modifying
    @Transactional
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les capteurs CO₂ par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
