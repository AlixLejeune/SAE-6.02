package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "t_e_sensorco2_co2")
@NoArgsConstructor
@AllArgsConstructor
public class SensorCO2  extends RoomObject implements IPosition, ISize{
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
