package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.repository.RoomObjects.Sensor9in1Repository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "sensor9in1", layout = MainLayout.class)
public class Sensor9in1View extends VerticalLayout {

    private final Sensor9in1Repository sensor9in1Repository;
    private final Grid<Sensor9in1> grid = new Grid<>(Sensor9in1.class);

    @Autowired
    public Sensor9in1View(Sensor9in1Repository sensor9in1Repository) {
        this.sensor9in1Repository = sensor9in1Repository;

        // Titre
        add("📋 Liste des Sensor9in1s");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Sensor9in1s", e -> loadData());

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
            List<Sensor9in1> sensor9in1s = sensor9in1Repository.findAll();
            grid.setItems(sensor9in1s);
            Notification.show("✅ " + sensor9in1s.size() + " Sensor9in1s chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
