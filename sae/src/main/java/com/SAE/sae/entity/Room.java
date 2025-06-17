package com.SAE.sae.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
    private int id;

    private String name;
    private double width;
    private double length;
    private double height;

    @JoinColumn(name="fk_building_id")
    private int fkBuildingId;
    @JoinColumn(name="fk_room_type_id")
    private int fkRoomTypeId;
}
