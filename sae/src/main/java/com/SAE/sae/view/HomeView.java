package com.SAE.sae.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.html.Label;
@Route("")
public class HomeView extends AppLayout  {

    public HomeView() {
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
        Button parentButton = new Button("Gestion des objets");
        parentButton.setWidthFull();

        // Sous-menu (caché au début)
        VerticalLayout sousMenu = new VerticalLayout();
        sousMenu.setPadding(false);
        sousMenu.setSpacing(false);
        sousMenu.setVisible(false);

        // Actions du sous-menu
        Button plugBtn = new Button("Prises", e -> Notification.show("Affichage des prises"));
        Button lightBtn = new Button("Lumières", e -> Notification.show("Affichage des lumières"));
        plugBtn.setWidthFull();
        lightBtn.setWidthFull();
        sousMenu.add(plugBtn, lightBtn);

        // Action du parent + toggle
        parentButton.addClickListener(e -> {
            // Clique sur le parent → action + toggle sous-menu
            Notification.show("Gestion des objets");
            sousMenu.setVisible(!sousMenu.isVisible());
        });

        // Autres menus simples
        Button dashboard = new Button("Tableau de bord", e -> Notification.show("Dashboard"));
        Button settings = new Button("Paramètres", e -> Notification.show("Paramètres"));
        dashboard.setWidthFull();
        settings.setWidthFull();

        // Ajouter tout dans le drawer
        menuLayout.add(parentButton, sousMenu, dashboard, settings);
        addToDrawer(menuLayout);

        // --- Contenu principal ---
        setContent(new Label("Bienvenue sur la page d'accueil"));
    }
}
