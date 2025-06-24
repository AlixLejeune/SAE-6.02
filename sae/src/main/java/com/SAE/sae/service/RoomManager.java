package com.SAE.sae.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SAE.sae.entity.Room;
import com.SAE.sae.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomManager {
    private final RoomRepository roomRepo;

    /**
     * Récupére tous les Rooms
     */
    public List<Room> getAllRooms(){
        return roomRepo.findAll();
    }

    /**
     * Récupére une room avec son id
     * @param id
     * @return une Room si trouvé, null sinon
     */
    public Room getRoomById(int id){
        Optional<Room> optionalroom = roomRepo.findById(id);
        if(optionalroom.isPresent()){
            log.debug("room {} has been returned", id);
            return optionalroom.get();
        }
        log.error("room with id {} doesn't exist.", id);
        return null;
    }

    /**
     * Enregistre une nouveau Room
     * @param room a ajouter
     * @return le room ajouté dans la base
     */
    public Room saveRoom (Room room){
        Room savedroom = roomRepo.save(room);
        log.info("room with id {} saved sucessfully.", room.getId());
        return savedroom;
    }

    /**
     * Met à jour une room
     * @param room modifié
     * @return le room mis à jour dans la base
     */
    public Room updateRoom(Room room){
        Optional<Room> existingRoom = roomRepo.findById(room.getId());

        Room updatedroom = roomRepo.save(room);
        log.info("room with id {} has been updated sucessfully.", room.getId());

        return updatedroom;
    }

    /**
     * Supprime une room par id
     * @param id du room à supprimer
     */
    public void deleteRoomById(int id){
        roomRepo.deleteById(id);
        log.info("room with id {} has been deleted sucessfully.", id);
    }

    /**
     * Récupère tous les rooms d'un bâtiment spécifié
     * @param buildingId l'ID du bâtiment recherché
     * @return une liste des Rooms trouvés, vide si aucune
     */
    public List<Room> getRoomsByBuildingId(int buildingId) {
        return roomRepo.findByBuilding_Id(buildingId);
    }

    /**
     * Récupère les rooms avec ce nom
     * @param name des rooms cherchées
     * @return une liste des Rooms trouvés, vide si aucune
     */
    public List<Room> getRoomsByName(String name){
        log.info("Return all rooms named " + name);
        return roomRepo.findByName(name);
    }
}
