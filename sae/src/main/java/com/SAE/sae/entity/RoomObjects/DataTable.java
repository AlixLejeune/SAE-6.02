package com.SAE.sae.entity.RoomObjects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.SAE.sae.entity.transform.IPosition;
import com.SAE.sae.entity.transform.ISize;

@Entity
@Table(name = "t_e_table_tab")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTable extends RoomObject implements IPosition, ISize {
    @Column(name = "tab_posx")
    private Double posX;

    @Column(name = "tab_posy")
    private Double posY;

    @Column(name = "tab_posz")
    private Double posZ;

    @Column(name = "tab_sizex")
    private Double sizeX;

    @Column(name = "tab_sizey")
    private Double sizeY;

    @Column(name = "tab_sizez")
    private Double sizeZ;
}