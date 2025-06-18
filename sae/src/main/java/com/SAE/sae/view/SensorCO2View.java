package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.repository.RoomObjects.SensorCO2Repository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "buildings", layout = MainLayout.class)
public class SensorCO2View extends VerticalLayout {

    private final SensorCO2Repository sensorCO2Repository;
    private final Grid<SensorCO2> grid = new Grid<>(SensorCO2.class);

    @Autowired
    public SensorCO2View(SensorCO2Repository sensorCO2Repository) {
        this.sensorCO2Repository = sensorCO2Repository;

        // Titre
        add("📋 Liste des SensorCO2s");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les SensorCO2s", e -> loadData());

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
            List<SensorCO2> sensorCO2s = sensorCO2Repository.findAll();
            grid.setItems(sensorCO2s);
            Notification.show("✅ " + sensorCO2s.size() + " SensorCO2s chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
