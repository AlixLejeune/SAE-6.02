package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorCO2Repository extends JpaRepository<SensorCO2, Integer> {

    // ========== REQUÊTES DE BASE ==========

    /**
     * Trouver toutes les portes d'une salle spécifique
     */
    List<SensorCO2> findByIdRoom(Number roomId);

    /**
     * Trouver toutes les portes d'une salle (alternative avec l'objet Room)
     */
    List<SensorCO2> findByRoom_Id(Long roomId);

    /**
     * Trouver par nom personnalisé exact
     */
    List<SensorCO2> findByCustomName(String customName);

    /**
     * Trouver un objet par son ID (read standard)
     */
    Optional<SensorCO2> findById(Integer id);

    /**
     * Créer ou mettre à jour un objet (save standard)
     */
    @Override
    <S extends SensorCO2> S save(S entity);

    /**
     * Créer ou mettre à jour une liste d’objets
     */
    @Override
    <S extends SensorCO2> List<S> saveAll(Iterable<S> entities);

    /**
     * Supprimer un objet par son ID
     */
    @Override
    void deleteById(Integer id);

    /**
     * Supprimer un objet donné
     */
    @Override
    void delete(SensorCO2 entity);

    /**
     * Supprimer tous les objets
     */
    @Override
    void deleteAll();

    /**
     * Supprimer une liste d’objets
     */
    @Override
    void deleteAll(Iterable<? extends SensorCO2> entities);

    /**
     * Vérifier l’existence d’un objet par son ID
     */
    @Override
    boolean existsById(Integer id);

    /**
     * Compter le nombre total d’objets
     */
    @Override
    long count();

    /**
     * Supprimer toutes les portes d'une salle
     */
    @Modifying
    @Transactional
    void deleteByIdRoom(Number roomId);

    /**
     * Supprimer les portes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
