package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.Room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
