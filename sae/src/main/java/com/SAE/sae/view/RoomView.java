package com.SAE.sae.view;

import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "rooms", layout = MainLayout.class)
public class RoomView extends VerticalLayout {

    private final RoomManager roomManager;
    private final Grid<Room> grid = new Grid<>(Room.class);

    @Autowired
    public RoomView(RoomManager roomManager) {
        this.roomManager = roomManager;

        // Titre
        add("📋 Liste des Rooms");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Rooms", e -> loadData());

        // Configuration du grid
        grid.setColumns("id", "name", "width", "length", "height");
        grid.setWidthFull();

        // Ajout des composants
        add(loadButton, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<Room> rooms = roomManager.getAllRooms();
            grid.setItems(rooms);
            Notification.show("✅ " + rooms.size() + " Rooms chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
