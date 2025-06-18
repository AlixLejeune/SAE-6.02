package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.DataTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DataTableRepository extends JpaRepository<DataTable, Integer> {

    // ========== REQUÊTES PERSONNALISÉES ==========

    /**
     * Trouver toutes les portes d'une salle spécifique
     */
    List<DataTable> findByRoomId(Integer roomId);

    /**
     * Trouver toutes les portes d'une salle (alternative avec l'objet Room)
     */
    List<DataTable> findByRoom_Id(Long roomId);

    /**
     * Trouver par nom personnalisé exact
     */
    List<DataTable> findByCustomName(String customName);

    /**
     * Supprimer toutes les portes d'une salle
     */
    @Modifying
    @Transactional
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les portes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
