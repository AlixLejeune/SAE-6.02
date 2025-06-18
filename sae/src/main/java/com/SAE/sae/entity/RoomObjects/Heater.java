package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_heater_hea")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Heater extends RoomObject implements IPosition, ISize{
    @Column(name = "hea_posx")
    private Double posX;
    
    @Column(name = "hea_posy")
    private Double posY;
    
    @Column(name = "hea_posz")
    private Double posZ;
        
    @Column(name = "hea_sizex")
    private Double sizeX;
    
    @Column(name = "hea_sizey")
    private Double sizeY;
    
    @Column(name = "hea_sizez")
    private Double sizeZ;
}
