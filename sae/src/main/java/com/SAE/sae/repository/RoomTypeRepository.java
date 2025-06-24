package com.SAE.sae.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SAE.sae.entity.RoomType;

/**
 * Default JpaRepository pour le CRUD des RoomType
 */

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer>{
    
}
