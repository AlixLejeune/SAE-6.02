package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.repository.RoomObjects.Sensor6in1Repository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "sensor6in1", layout = MainLayout.class)
public class Sensor6in1View extends VerticalLayout {

    private final Sensor6in1Repository sensor6in1Repository;
    private final Grid<Sensor6in1> grid = new Grid<>(Sensor6in1.class);

    @Autowired
    public Sensor6in1View(Sensor6in1Repository sensor6in1Repository) {
        this.sensor6in1Repository = sensor6in1Repository;

        // Titre
        add("📋 Liste des Sensor6in1s");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Sensor6in1s", e -> loadData());

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
            List<Sensor6in1> sensor6in1s = sensor6in1Repository.findAll();
            grid.setItems(sensor6in1s);
            Notification.show("✅ " + sensor6in1s.size() + " Sensor6in1s chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
