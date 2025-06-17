package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "t_e_siren_sir")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Siren  extends RoomObject implements IPosition, ISize{
     @Column(name = "sir_posx")
    private Double posX;

    @Column(name = "sir_posy")
    private Double posY;

    @Column(name = "sir_posz")
    private Double posZ;
}
