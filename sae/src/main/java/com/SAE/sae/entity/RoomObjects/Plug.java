package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

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
