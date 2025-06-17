package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_lamp_lam")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lamp extends RoomObject implements IPosition, ISize{
    @Column(name = "lam_posx")
    private Double posX;

    @Column(name = "lam_posy")
    private Double posY;

    @Column(name = "lam_posz")
    private Double posZ;
}
