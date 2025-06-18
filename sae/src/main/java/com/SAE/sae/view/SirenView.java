package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.repository.RoomObjects.SirenRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "sirens", layout = MainLayout.class)
public class SirenView extends VerticalLayout {

    private final SirenRepository sirenRepository;
    private final Grid<Siren> grid = new Grid<>(Siren.class);

    @Autowired
    public SirenView(SirenRepository sirenRepository) {
        this.sirenRepository = sirenRepository;

        // Titre
        add("📋 Liste des Sirens");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Sirens", e -> loadData());

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
            List<Siren> sirens = sirenRepository.findAll();
            grid.setItems(sirens);
            Notification.show("✅ " + sirens.size() + " Sirens chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
