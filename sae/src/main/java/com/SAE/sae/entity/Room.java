package com.SAE.sae.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_e_room_roo")
public class Room {
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
