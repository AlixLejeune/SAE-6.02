package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

import jakarta.persistence.Column;

public class Plug  extends ConnectedObject implements IPosition, ISize{
     @Column(name = "plu_posx")
    private Double posX;

    @Column(name = "plu_posy")
    private Double posY;

    @Column(name = "plu_posz")
    private Double posZ;
}
