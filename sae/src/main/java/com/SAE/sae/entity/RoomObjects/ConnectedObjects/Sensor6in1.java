package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;

public class Sensor6in1  extends ConnectedObject implements IPosition, ISize{
     @Column(name = "sio_posx")
    private Double posX;

    @Column(name = "sio_posy")
    private Double posY;

    @Column(name = "sio_posz")
    private Double posZ;
}
