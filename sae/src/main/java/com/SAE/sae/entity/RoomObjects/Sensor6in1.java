package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

@Entity
@Table(name = "t_e_sensor6in1_sio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor6in1 extends RoomObject implements IPosition, ISize{
     @Column(name = "sio_posx")
    private Double posX;

    @Column(name = "sio_posy")
    private Double posY;

    @Column(name = "sio_posz")
    private Double posZ;
}
