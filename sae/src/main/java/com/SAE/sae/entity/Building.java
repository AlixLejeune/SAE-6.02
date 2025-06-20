package com.SAE.sae.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.SAE.sae.entity.Room;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_e_building_bui")
public class Building {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(targetEntity = Room.class, mappedBy = "fkBuildingId")
    private List<Room> rooms;

}
