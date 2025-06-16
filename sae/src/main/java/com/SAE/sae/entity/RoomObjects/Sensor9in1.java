package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

@Entity
@Table(name = "t_e_sensor9in1_nio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor9in1  extends RoomObject implements IPosition, ISize{
     @Column(name = "nio_posx")
    private Double posX;

    @Column(name = "nio_posy")
    private Double posY;

    @Column(name = "nio_posz")
    private Double posZ;
}
