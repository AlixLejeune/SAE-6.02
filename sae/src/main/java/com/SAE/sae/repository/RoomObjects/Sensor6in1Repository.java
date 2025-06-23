package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Sensor6in1Repository extends JpaRepository<Sensor6in1, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver tous les capteurs d'une salle spécifique
     */
    List<Sensor6in1> findByRoomId(Integer roomId);

    /**
     * Trouver tous les capteurs d'une salle (alternative avec l'objet Room)
     */
    List<Sensor6in1> findByRoom_Id(Long roomId);

    /**
     * Trouver les capteurs par nom personnalisé exact
     */
    List<Sensor6in1> findByCustomName(String customName);

    /**
     * Supprimer les capteurs par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
