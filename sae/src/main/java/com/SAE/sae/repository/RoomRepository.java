package com.SAE.sae.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SAE.sae.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer>{
    
}
