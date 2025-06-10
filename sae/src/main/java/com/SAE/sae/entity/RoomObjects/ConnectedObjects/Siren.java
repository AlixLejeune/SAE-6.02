package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;

public class Siren  extends ConnectedObject implements IPosition, ISize{
     @Column(name = "sir_posx")
    private Double posX;

    @Column(name = "sir_posy")
    private Double posY;

    @Column(name = "sir_posy")
    private Double posZ;
}
