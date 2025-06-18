package com.SAE.sae.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.SAE.sae.entity.Building;
import com.SAE.sae.repository.BuildingRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import com.SAE.sae.repository.BuildingRepository;



@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    @Autowired
    public HomeView() {
        // --- Contenu principal ---
        add(new Label("Bienvenue sur la page d'accueil"));

        Label portesOuvretes = new Label("La température moyennes dans les batiment :");

        add(portesOuvretes);
    }

}
