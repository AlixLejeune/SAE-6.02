package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.repository.RoomObjects.PlugRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "plugs", layout = MainLayout.class)
public class PlugView extends VerticalLayout {

    private final PlugRepository plugRepository;
    private final Grid<Plug> grid = new Grid<>(Plug.class);

    @Autowired
    public PlugView(PlugRepository plugRepository) {
        this.plugRepository = plugRepository;

        // Titre
        add("📋 Liste des Plugs");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Plugs", e -> loadData());

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
            List<Plug> plugs = plugRepository.findAll();
            grid.setItems(plugs);
            Notification.show("✅ " + plugs.size() + " Plugs chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
