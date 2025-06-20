package com.SAE.sae.view;

import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "room", layout = MainLayout.class)
public class RoomByIdView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RoomManager roomManager;

    @Autowired
    public RoomByIdView(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id == null) {
            add(new Label("Aucun ID fourni"));
            return;
        }

        Room room = roomManager.getRoomById(id);
//.orElse(null)
        if (room == null) {
            add(new Label("❌ Salle introuvable pour l'ID " + id));
        } else {
            add(new H1("Salle : " + room.getName()));
            add(new Label("Dimensions : " +
                    room.getLength() + "m x " +
                    room.getWidth() + "m x " +
                    room.getHeight() + "m"));

            add(new Label("ID du type de salle : " + room.getFkRoomTypeId()));
        }
    }
}
