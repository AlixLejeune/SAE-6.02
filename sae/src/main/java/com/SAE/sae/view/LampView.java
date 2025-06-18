package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.service.RoomObjects.LampManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "lamps", layout = MainLayout.class)
public class LampView extends VerticalLayout {

    private final LampManager lampManager;
    private final Grid<Lamp> grid = new Grid<>(Lamp.class);

    @Autowired
    public LampView(LampManager lampManager) {
        this.lampManager = lampManager;

        // Titre
        add("📋 Liste des Lamps");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Lamps", e -> loadData());

        // Configuration du grid
        grid.setColumns("id", "customName", "posX", "posY", "posZ");
        grid.setWidthFull();

        // Ajout des composants
        add(loadButton, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<Lamp> lamps = lampManager.findAll();
            grid.setItems(lamps);
            Notification.show("✅ " + lamps.size() + " Lamps chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
