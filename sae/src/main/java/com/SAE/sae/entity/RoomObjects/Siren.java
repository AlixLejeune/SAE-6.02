package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "t_e_siren_sir")
@NoArgsConstructor
@AllArgsConstructor
public class Siren  extends RoomObject implements IPosition{
    @Column(name = "sir_posx")
    private Double posX;

    @Column(name = "sir_posy")
    private Double posY;

    @Column(name = "sir_posz")
    private Double posZ;

    public Siren(String name){
        this.customName = name;
    }
}
