package com.SAE.sae.entity.RoomObjects;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * CLasse représentant les tables (à cause des mots-clés déjà utilisés, il a été impossible de la nommer Table)
 * @param customName String hérité de RoomObject
 * @param room object Room hérité de RoomObject
 * @param pos x,y,z valeurs numériques indiquant la position dans la pièce
 * @param size x,y,z valeurs numériques indiquant la taille de l'objet
 */

@Data
@Entity
@Table(name = "t_e_table_tab")
@NoArgsConstructor
@AllArgsConstructor
public class DataTable extends RoomObject implements IPosition, ISize {
    @Column(name = "tab_posx")
    private Double posX;

    @Column(name = "tab_posy")
    private Double posY;

    @Column(name = "tab_posz")
    private Double posZ;

    @Column(name = "tab_sizex")
    private Double sizeX;

    @Column(name = "tab_sizey")
    private Double sizeY;

    @Column(name = "tab_sizez")
    private Double sizeZ;

    public DataTable(String name){
        this.customName = name;
    }
}