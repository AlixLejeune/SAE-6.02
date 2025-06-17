package com.SAE.sae.repository.RoomObjects;

import com.SAE.sae.entity.RoomObjects.Heater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeaterRepository extends JpaRepository<Heater, Integer> {

    // ========== REQUÊTES DE BASE ==========

    /**
     * Trouver toutes les portes d'une salle spécifique
     */
    List<Heater> findByRoomId(Integer  roomId);

    /**
     * Trouver toutes les portes d'une salle (alternative avec l'objet Room)
     */
    List<Heater> findByRoom_Id(Long roomId);

    /**
     * Trouver par nom personnalisé exact
     */
    List<Heater> findByCustomName(String customName);

    /**
     * Trouver un objet par son ID (read standard)
     */
    Optional<Heater> findById(Integer id);

    /**
     * Créer ou mettre à jour un objet (save standard)
     */
    @Override
    <S extends Heater> S save(S entity);

    /**
     * Créer ou mettre à jour une liste d’objets
     */
    @Override
    <S extends Heater> List<S> saveAll(Iterable<S> entities);

    /**
     * Supprimer un objet par son ID
     */
    @Override
    void deleteById(Integer id);

    /**
     * Supprimer un objet donné
     */
    @Override
    void delete(Heater entity);

    /**
     * Supprimer tous les objets
     */
    @Override
    void deleteAll();

    /**
     * Supprimer une liste d’objets
     */
    @Override
    void deleteAll(Iterable<? extends Heater> entities);

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
    void deleteByRoomId(Integer id);

    /**
     * Supprimer les portes par nom personnalisé
     */
    @Modifying
    @Transactional
    void deleteByCustomName(String customName);
}
