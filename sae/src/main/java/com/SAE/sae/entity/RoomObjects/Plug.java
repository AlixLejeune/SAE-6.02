package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "t_e_plug_plu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plug  extends RoomObject implements IPosition, ISize{
     @Column(name = "plu_posx")
    private Double posX;

    @Column(name = "plu_posy")
    private Double posY;

    @Column(name = "plu_posz")
    private Double posZ;
}
