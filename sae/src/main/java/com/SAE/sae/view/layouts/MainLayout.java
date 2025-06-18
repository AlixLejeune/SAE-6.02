package com.SAE.sae.view.layouts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.SAE.sae.entity.Building;
import com.SAE.sae.repository.BuildingRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class MainLayout extends AppLayout {

       private final BuildingRepository buildingRepository;
    private final Button parentButtonBuilding = new Button("Les différents batiments");
    private final Button parentButtonSensor = new Button("Les différents capteur ");
    private final VerticalLayout sousMenuBuilding = new VerticalLayout();

    @Autowired
    public MainLayout(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
        // --- HEADER ---
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Home Assistant 2.0");
        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        addToNavbar(header);

        // --- MENU ---
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setPadding(false);
        menuLayout.setSpacing(false);

        // Menu principal
        parentButtonBuilding.setWidthFull();
        parentButtonSensor.setWidthFull();

        // Sous-menu

        sousMenuBuilding.setPadding(false);
        sousMenuBuilding.setSpacing(false);
        sousMenuBuilding.setVisible(false);

        // Actions du sous-menu
        Button plugBtn = new Button("Prises", e -> Notification.show("Affichage des prises"));
        Button lightBtn = new Button("Lumières", e -> Notification.show("Affichage des lumières"));
        plugBtn.setWidthFull();
        lightBtn.setWidthFull();
        sousMenuBuilding.add(plugBtn, lightBtn);

        // Action du parent
        parentButtonBuilding.addClickListener(e -> {
            sousMenuBuilding.setVisible(!sousMenuBuilding.isVisible());

            // Recharge proprement uniquement si visible
            if (sousMenuBuilding.isVisible()) {
                sousMenuBuilding.removeAll(); // Supprime anciens boutons
                loadBuildingsData(); // Recharge proprement
            }
        });

        // Autres menus simples
        Button dashboard = new Button("Tableau de bord", e -> Notification.show("Dashboard"));
        dashboard.setWidthFull();

        // Ajouter tout dans le drawer
        menuLayout.add(parentButtonBuilding, sousMenuBuilding, dashboard);
        addToDrawer(menuLayout);

        // --- Contenu principal ---
        setContent(new Label("Bienvenue sur la page d'accueil"));

    }

    private void loadBuildingsData() {
        try {
            List<Building> buildings = buildingRepository.findAll();

            for (Building building : buildings) {
                Button plugBtn = new Button(building.getName(), e ->  UI.getCurrent().navigate("buildings"));
                sousMenuBuilding.add(plugBtn);
            }
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
