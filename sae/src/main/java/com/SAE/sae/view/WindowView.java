package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.service.RoomObjects.WindowManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "windows", layout = MainLayout.class)
public class WindowView extends VerticalLayout {

    private final WindowManager windowManager;
    private final Grid<Window> grid = new Grid<>(Window.class);

    @Autowired
    public WindowView(WindowManager windowManager) {
        this.windowManager = windowManager;

        // Titre
        add("📋 Liste des Windows");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les Windows", e -> loadData());

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
            List<Window> windows = windowManager.findAll();
            grid.setItems(windows);
            Notification.show("✅ " + windows.size() + " Windows chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
