package com.SAE.sae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SAE.sae.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByBuilding_Id(int buildingId);
}
