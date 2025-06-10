package com.SAE.sae.entity.RoomObjects.ConnectedObjects;

import jakarta.persistence.*;
import lombok.*;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_door_doo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lamp extends ConnectedObject implements IPosition, ISize{
    @Column(name = "lam_posx")
    private Double posX;

    @Column(name = "lam_posy")
    private Double posY;

    @Column(name = "lam_posz")
    private Double posZ;
}
