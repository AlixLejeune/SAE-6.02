package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.Room;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class RoomObject {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    protected int id;

    @Column(name = "rob_name")
    protected String customName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room")
    private Room room;
}
