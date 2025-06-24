package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * CLasse représentant les capteurs 9 en 1
 */

@Data
@Entity
@Table(name = "t_e_sensor9in1_nio")
@NoArgsConstructor
@AllArgsConstructor
public class Sensor9in1  extends RoomObject implements IPosition{
     @Column(name = "nio_posx")
    private Double posX;

    @Column(name = "nio_posy")
    private Double posY;

    @Column(name = "nio_posz")
    private Double posZ;

    public Sensor9in1(String name){
        this.customName = name;
    }
}
