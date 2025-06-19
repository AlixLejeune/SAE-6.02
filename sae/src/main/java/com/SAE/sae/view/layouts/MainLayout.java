package com.SAE.sae.view.layouts;

import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.Room;
import com.SAE.sae.repository.BuildingRepository;
import com.SAE.sae.repository.RoomRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class MainLayout extends AppLayout {

    private final BuildingRepository buildingRepository;
    private final RoomRepository roomRepository;
    private final VerticalLayout sousMenuBuilding = new VerticalLayout();

    @Autowired
    public MainLayout(BuildingRepository buildingRepository, RoomRepository roomRepository) {
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;

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

        sousMenuBuilding.add(new Button("Home", click -> {UI.getCurrent().navigate("");}));
        // On charge directement les bâtiments et leurs salles
        try {
            List<Building> buildings = buildingRepository.findAll();
            for (Building building : buildings) {
                // Bouton pour le bâtiment
                Button buildingBtn = new Button("🏢 " + building.getName());
                buildingBtn.setWidthFull();

                // Sous-menu des salles
                VerticalLayout roomSubMenu = new VerticalLayout();
                roomSubMenu.setPadding(false);
                roomSubMenu.setSpacing(false);
                roomSubMenu.setVisible(false);

                buildingBtn.addClickListener(ev -> {
                    roomSubMenu.setVisible(!roomSubMenu.isVisible());
                });

                // Ajouter les salles
                List<Room> rooms = roomRepository.findByFkBuildingId(building.getId());
                for (Room room : rooms) {
                    Button roomBtn = new Button("🚪 " + room.getName(), click -> {
                        UI.getCurrent().navigate("rooms/" + room.getId());
                    });
                    roomBtn.setWidthFull();
                    roomSubMenu.add(roomBtn);
                }

                sousMenuBuilding.add(buildingBtn, roomSubMenu);
            }
        } catch (Exception ex) {
            Notification.show("❌ Erreur chargement bâtiments : " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
        }

        // Affichage direct
        menuLayout.add(sousMenuBuilding);
        addToDrawer(menuLayout);
    }
}
