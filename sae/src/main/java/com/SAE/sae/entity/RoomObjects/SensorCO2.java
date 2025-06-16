package com.SAE.sae.entity.RoomObjects;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

@Entity
@Table(name = "t_e_sensorco2_co2")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorCO2  extends RoomObject implements IPosition, ISize{
     @Column(name = "co2_posx")
    private Double posX;

    @Column(name = "co2_posy")
    private Double posY;

    @Column(name = "co2_posz")
    private Double posZ;
}
