package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;

public class SensorCO2  extends ConnectedObject implements IPosition, ISize{
     @Column(name = "co2_posx")
    private Double posX;

    @Column(name = "co2_posy")
    private Double posY;

    @Column(name = "co2_posz")
    private Double posZ;
}
