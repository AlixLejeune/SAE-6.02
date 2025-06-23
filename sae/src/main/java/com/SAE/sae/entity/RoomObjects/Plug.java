package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "t_e_plug_plu")
@NoArgsConstructor
@AllArgsConstructor
public class Plug extends RoomObject implements IPosition{
     @Column(name = "plu_posx")
    private Double posX;

    @Column(name = "plu_posy")
    private Double posY;

    @Column(name = "plu_posz")
    private Double posZ;

    public Plug(String name){
        this.customName = name;
    }
}
