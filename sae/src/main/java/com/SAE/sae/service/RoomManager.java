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

    public List<Room> getAllRooms(){
        return roomRepo.findAll();
    }
    public Room getRoomById(int id){
        Optional<Room> optionalroom = roomRepo.findById(id);
        if(optionalroom.isPresent()){
            log.debug("room {} has been returned", id);
            return optionalroom.get();
        }
        log.error("room with id {} doesn't exist.", id);
        return null;
    }

    public Room saveRoom (Room room){
        Room savedroom = roomRepo.save(room);
        log.info("room with id {} saved sucessfully.", room.getId());
        return savedroom;
    }

    public Room updateRoom(Room room){
        Optional<Room> existingRoom = roomRepo.findById(room.getId());

        Room updatedroom = roomRepo.save(room);
        log.info("room with id {} has been updated sucessfully.", room.getId());

        return updatedroom;
    }

    public void deleteRoomById(int id){
        roomRepo.deleteById(id);
        log.info("room with id {} has been deleted sucessfully.", id);
    }

    public List<Room> getRoomsByBuildingId(int buildingId) {
        return roomRepo.findByFkBuildingId(buildingId);
    }
}
