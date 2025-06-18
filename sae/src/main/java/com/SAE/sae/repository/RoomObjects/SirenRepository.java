package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Siren;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SirenRepository extends JpaRepository<Siren, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les sirènes d'une salle spécifique
     */
    List<Siren> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les sirènes d'une salle (alternative avec l'objet Room)
     */
    List<Siren> findByRoom_Id(Long roomId);

    /**
     * Trouver les sirènes par nom personnalisé exact
     */
    List<Siren> findByCustomName(String customName);

    /**
     * Supprimer toutes les sirènes d'une salle
     */
    @Modifying
    @Transactional
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les sirènes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
