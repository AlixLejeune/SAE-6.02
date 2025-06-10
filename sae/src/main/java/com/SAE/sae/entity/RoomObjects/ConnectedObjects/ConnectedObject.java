package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import jakarta.persistence.*;

import lombok.*;

import com.SAE.sae.entity.RoomObjects.RoomObject;

@Entity
@Table(name = "t_e_connectedobjects_cobj")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedObject extends RoomObject {
    @Column(name = "cobj_id")
    private String CustomId;
}
