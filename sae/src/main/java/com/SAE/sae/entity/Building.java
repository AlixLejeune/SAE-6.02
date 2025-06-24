package com.SAE.sae.entity;

import java.util.ArrayList;
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

/** 
 * Classe représentant les bâtiments
 * @param name String qui contient le nom du bâtiment
 * @param rooms Liste des pièces du bâtiment permettant d'effectuer la jointure
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_e_building_bui")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(targetEntity = Room.class, mappedBy = "building")
    private List<Room> rooms = new ArrayList<>();


    public Building(String name){
        this.name = name;
    }
}
