package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Lamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LampRepository extends JpaRepository<Lamp, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les lampes d'une salle spécifique
     */
    List<Lamp> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les lampes d'une salle (alternative avec l'objet Room)
     */
    List<Lamp> findByRoom_Id(Long roomId);

    /**
     * Trouver les lampes par nom personnalisé exact
     */
    List<Lamp> findByCustomName(String customName);

    /**
     * Supprimer toutes les lampes d'une salle
     */
    @Modifying
    @Transactional
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les lampes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
