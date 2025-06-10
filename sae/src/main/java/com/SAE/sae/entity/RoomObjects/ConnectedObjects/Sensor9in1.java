package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;

public class Sensor9in1  extends ConnectedObject implements IPosition, ISize{
     @Column(name = "nio_posx")
    private Double posX;

    @Column(name = "nio_posy")
    private Double posY;

    @Column(name = "nio_posz")
    private Double posZ;
}
