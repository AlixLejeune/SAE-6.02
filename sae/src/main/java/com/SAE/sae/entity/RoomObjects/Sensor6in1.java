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
@Table(name = "t_e_sensor6in1_sio")
@NoArgsConstructor
@AllArgsConstructor
public class Sensor6in1 extends RoomObject implements IPosition {
    @Column(name = "sio_posx")
    private Double posX;

    @Column(name = "sio_posy")
    private Double posY;

    @Column(name = "sio_posz")
    private Double posZ;

    public Sensor6in1(String name){
        this.customName = name;
    }
}
