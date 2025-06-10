package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.*;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

public class RoomObject {

    private Number id;

    private String CustomName;

    public Number IdRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room")
    private Room room;
}
