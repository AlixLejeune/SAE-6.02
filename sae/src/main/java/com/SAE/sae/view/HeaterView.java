package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.repository.RoomObjects.HeaterRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("heaters") 
public class HeaterView extends VerticalLayout {

    private final HeaterRepository heaterRepository;
    private final Grid<Heater> grid = new Grid<>(Heater.class);

    @Autowired
    public HeaterView(HeaterRepository heaterRepository) {
        this.heaterRepository = heaterRepository;

        // Titre
        add("📋 Liste des Heaters");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Heaters", e -> loadData());

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
            List<Heater> heaters = heaterRepository.findAll();
            grid.setItems(heaters);
            Notification.show("✅ " + heaters.size() + " Heaters chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
