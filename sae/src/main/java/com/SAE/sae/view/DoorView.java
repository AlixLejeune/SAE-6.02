package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.RoomObjects.DoorRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "doors", layout = MainLayout.class)
public class DoorView extends VerticalLayout {

    private final DoorRepository doorRepository;
    private final Grid<Door> grid = new Grid<>(Door.class);

    @Autowired
    public DoorView(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;

        // Titre
        add("📋 Liste des Doors");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Doors", e -> loadData());

        // Configuration du grid
        grid.setColumns("id", "customName", "posX", "posY", "posZ", "sizeX", "sizeY", "sizeZ");
        grid.setWidthFull();

        // Ajout des composants
        add(loadButton, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<Door> doors = doorRepository.findAll();
            grid.setItems(doors);
            Notification.show("✅ " + doors.size() + " Doors chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
