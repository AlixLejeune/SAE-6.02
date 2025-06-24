package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.SequenceGenerator;

import lombok.Data;

/**
 * CLasse abstraite généralisant tous les objets dans les pièces
 * @param customName String permettant d'enregistrer un nom personnalisé
 * @param room object Room permettant d'effectuer la jointure
 */

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class RoomObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_object_seq")
    @SequenceGenerator(name = "room_object_seq", sequenceName = "room_object_seq", allocationSize = 1)
    protected int id;

    @Column(name = "rob_name")
    protected String customName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room")
    @JsonIgnore
    private Room room;
}
