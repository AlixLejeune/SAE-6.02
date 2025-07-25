package com.SAE.sae.entity.RoomObjects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.SAE.sae.entity.transform.IPosition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * CLasse représentant les lampes
 */

@Data
@Entity
@Table(name = "t_e_lamp_lam")
@NoArgsConstructor
@AllArgsConstructor
public class Lamp extends RoomObject implements IPosition{
    @Column(name = "lam_posx")
    private Double posX;

    @Column(name = "lam_posy")
    private Double posY;

    @Column(name = "lam_posz")
    private Double posZ;

    public Lamp(String name){
        this.customName = name;
    }
}
