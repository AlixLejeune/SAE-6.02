package com.SAE.sae.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SAE.sae.entity.RoomType;
import com.SAE.sae.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeManager {
    private final RoomTypeRepository roomTypeRepo;

    /**
     * Récupére tous les RoomTypes
     */
    public List<RoomType> getAllRoomTypes(){
        return roomTypeRepo.findAll();
    }

    /**
     * Récupére un roomType avec son id
     * @param id
     * @return un RoomType si trouvé, null sinon
     */
    public RoomType getRoomTypeById(int id){
        Optional<RoomType> optionalroomType = roomTypeRepo.findById(id);
        if(optionalroomType.isPresent()){
            log.debug("roomType {} has been returned", id);
            return optionalroomType.get();
        }
        log.error("roomType with id {} doesn't exist.", id);
        return null;
    }

    /**
     * Enregistre un nouveau RoomType
     * @param roomType a ajouter
     * @return le roomType ajouté dans la base
     */
    public RoomType saveRoomType (RoomType roomType){
        RoomType savedroomType = roomTypeRepo.save(roomType);
        log.info("roomType with id {} saved wucessfully.", roomType.getId());
        return savedroomType;
    }

    /**
     * Met à jour un roomType
     * @param roomType modifié
     * @return le roomType mis à jour dans la base
     */
    public RoomType updateRoomType(RoomType roomType){
        Optional<RoomType> existingRoomType = roomTypeRepo.findById(roomType.getId());

        RoomType updatedroomType = roomTypeRepo.save(roomType);
        log.info("roomType with id {} has been updated sucessfully.", roomType.getId());

        return updatedroomType;
    }

    /**
     * Supprime un roomType par id
     * @param id du roomType à supprimer
     * @return void
     */
    public void deleteRoomTypeById(int id){
        roomTypeRepo.deleteById(id);
        log.info("roomType with id {} has been deleted sucessfully.", id);
    }
}
