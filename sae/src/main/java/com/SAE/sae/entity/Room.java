package com.SAE.sae.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Classe représentant les pièces
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_e_room_roo")
public class Room {

    public Room(String name, double width, double length, double height, int fkRoomTypeId) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.height = height;
        this.fkRoomTypeId = fkRoomTypeId;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private double width;
    private double length;
    private double height;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_building_id") 
    private Building building;
    

    private int fkRoomTypeId;

    
}
