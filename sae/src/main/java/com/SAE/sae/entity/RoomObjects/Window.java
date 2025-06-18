package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_window_win")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Window  extends RoomObject implements IPosition, ISize {
    @Column(name = "win_posx")
    private Double posX;
    
    @Column(name = "win_posy")
    private Double posY;
    
    @Column(name = "win_posz")
    private Double posZ;
        
    @Column(name = "win_sizex")
    private Double sizeX;
    
    @Column(name = "win_sizey")
    private Double sizeY;
    
    @Column(name = "win_sizez")
    private Double sizeZ;
}
