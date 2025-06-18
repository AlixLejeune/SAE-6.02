package com.SAE.sae.view;

import com.SAE.sae.entity.Building;
import com.SAE.sae.repository.BuildingRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("buildings") 
public class BuildingView extends VerticalLayout {

    private final BuildingRepository buildingRepository;
    private final Grid<Building> grid = new Grid<>(Building.class);

    @Autowired
    public BuildingView(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;

        // Titre
        add("📋 Liste des Buildings");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Buildings", e -> loadData());

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
            List<Building> buildings = buildingRepository.findAll();
            grid.setItems(buildings);
            Notification.show("✅ " + buildings.size() + " Buildings chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
