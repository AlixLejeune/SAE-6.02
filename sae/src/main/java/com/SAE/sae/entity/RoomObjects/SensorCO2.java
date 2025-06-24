package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * CLasse représentant les capteurs de CO2
 */

@Data
@Entity
@Table(name = "t_e_sensorco2_co2")
@NoArgsConstructor
@AllArgsConstructor
public class SensorCO2  extends RoomObject implements IPosition{
    @Column(name = "co2_posx")
    private Double posX;

    @Column(name = "co2_posy")
    private Double posY;

    @Column(name = "co2_posz")
    private Double posZ;

    public SensorCO2(String name){
        this.customName = name;
    }
}
