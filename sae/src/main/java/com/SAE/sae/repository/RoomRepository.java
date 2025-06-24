package com.SAE.sae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SAE.sae.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    /**
     * Méthode permettant de trouver les pièces d'un bâtiment en fonction de son Id
     * @param buildingId l'Id du bâtiment dans lequel on cherche les pièces
     * @return Une liste des pièces trouvées
     */
    List<Room> findByBuilding_Id(int buildingId);

    /**
     * Méthode permetttant de trouver les pièces qui portent le nom cherché
     * @param name nom des pièces à trovuer
     * @return Une liste des pièces trouvées
     */
    List<Room> findByName(String name);
}
