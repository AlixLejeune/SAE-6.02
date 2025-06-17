package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_door_doo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Door extends RoomObject implements IPosition, ISize {
    
    @Column(name = "doo_posx")
    private Double posX;
    
    @Column(name = "doo_posy")
    private Double posY;
    
    @Column(name = "doo_posz")
    private Double posZ;
        
    @Column(name = "doo_sizex")
    private Double sizeX;
    
    @Column(name = "doo_sizey")
    private Double sizeY;
    
    @Column(name = "doo_sizez")
    private Double sizeZ;
}