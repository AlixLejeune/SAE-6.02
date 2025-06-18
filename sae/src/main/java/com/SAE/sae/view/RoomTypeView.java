package com.SAE.sae.view;

import com.SAE.sae.entity.RoomType;
import com.SAE.sae.repository.RoomTypeRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "room_types", layout = MainLayout.class)
public class RoomTypeView extends VerticalLayout {

    private final RoomTypeRepository roomTypeRepository;
    private final Grid<RoomType> grid = new Grid<>(RoomType.class);

    @Autowired
    public RoomTypeView(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;

        // Titre
        add("📋 Liste des RoomTypes");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les RoomTypes", e -> loadData());

        // Configuration du grid
        grid.setColumns("id", "name");
        grid.setWidthFull();

        // Ajout des composants
        add(loadButton, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            grid.setItems(roomTypes);
            Notification.show("✅ " + roomTypes.size() + " RoomTypes chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
