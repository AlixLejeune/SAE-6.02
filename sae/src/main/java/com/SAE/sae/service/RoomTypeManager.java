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

    public List<RoomType> getAllRoomTypes(){
        return roomTypeRepo.findAll();
    }
    public RoomType getRoomTypeById(int id){
        Optional<RoomType> optionalroomType = roomTypeRepo.findById(id);
        if(optionalroomType.isPresent()){
            log.debug("roomType {} has been returned", id);
            return optionalroomType.get();
        }
        log.error("roomType with id {} doesn't exist.", id);
        return null;
    }

    public RoomType saveRoomType (RoomType roomType){
        RoomType savedroomType = roomTypeRepo.save(roomType);
        log.info("roomType with id {} saved wucessfully.", roomType.getId());
        return savedroomType;
    }

    public RoomType updateRoomType(RoomType roomType){
        Optional<RoomType> existingRoomType = roomTypeRepo.findById(roomType.getId());

        RoomType updatedroomType = roomTypeRepo.save(roomType);
        log.info("roomType with id {} has been updated sucessfully.", roomType.getId());

        return updatedroomType;
    }

    public void deleteRoomTypeById(int id){
        roomTypeRepo.deleteById(id);
        log.info("roomType with id {} has been deleted sucessfully.", id);
    }
}
