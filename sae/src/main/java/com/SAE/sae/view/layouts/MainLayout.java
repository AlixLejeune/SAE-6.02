package com.SAE.sae.view.layouts;

import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.Room;
import com.SAE.sae.repository.BuildingRepository;
import com.SAE.sae.repository.RoomRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private final VerticalLayout menuContainer = new VerticalLayout();

    @Autowired
    public MainLayout(BuildingRepository buildingRepository, RoomRepository roomRepository) {
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;

        // Assurer que le layout prend toute la hauteur
        getElement().getStyle()
            .set("min-height", "100vh")
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("background-attachment", "fixed");

        // --- HEADER ---
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Home Assistant 2.0");
        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        addToNavbar(header);

        createNavigationDrawer();
    }
    
    private void createNavigationDrawer() {
        // Container principal du menu
        menuContainer.setPadding(false);
        menuContainer.setSpacing(false);
        menuContainer.getStyle()
            .set("background", "linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%)")
            .set("min-height", "100vh")
            .set("padding", "0");
        
        // Header du drawer
        createDrawerHeader();
        
        // Menu principal
        createMainMenu();
        
        // Section des bâtiments
        createBuildingsSection();
        
        // Footer du drawer
        createDrawerFooter();
        
        addToDrawer(menuContainer);
    }
    
    private void createDrawerHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setPadding(true);
        header.setSpacing(false);
        header.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("color", "white")
            .set("margin-bottom", "0");
        
        Icon dashboardIcon = new Icon(VaadinIcon.DASHBOARD);
        dashboardIcon.getStyle()
            .set("font-size", "2em")
            .set("color", "white")
            .set("margin-bottom", "8px");
        
        H4 headerTitle = new H4("Navigation");
        headerTitle.getStyle()
            .set("margin", "0")
            .set("color", "white")
            .set("font-weight", "300")
            .set("text-align", "center");
        
        header.add(dashboardIcon, headerTitle);
        header.setAlignItems(Alignment.CENTER);
        
        menuContainer.add(header);
    }
    
    private void createMainMenu() {
        VerticalLayout mainMenu = new VerticalLayout();
        mainMenu.setPadding(true);
        mainMenu.setSpacing(true);
        
        // Bouton Home
        Button homeBtn = createMenuButton("🏠 Accueil", VaadinIcon.HOME, () -> {
            UI.getCurrent().navigate("");
        });
        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        // Autres boutons principaux
        Button buildingsBtn = createMenuButton("🏢 Bâtiments", VaadinIcon.BUILDING, () -> {
            UI.getCurrent().navigate("buildings");
        });
        
        Button roomsBtn = createMenuButton("🚪 Toutes les Salles", VaadinIcon.GRID_SMALL, () -> {
            UI.getCurrent().navigate("rooms");
        });
        
        Button roomTypesBtn = createMenuButton("📋 Types de Salles", VaadinIcon.LIST, () -> {
            UI.getCurrent().navigate("room_types");
        });
        
        mainMenu.add(homeBtn, buildingsBtn, roomsBtn, roomTypesBtn);
        
        // Séparateur
        Hr separator1 = new Hr();
        separator1.getStyle().set("margin", "10px 0").set("border-color", "#dee2e6");
        
        // Section des appareils
        Span devicesLabel = new Span("📱 APPAREILS");
        devicesLabel.getStyle()
            .set("font-size", "0.8em")
            .set("font-weight", "bold")
            .set("color", "#6c757d")
            .set("margin", "10px 0 5px 15px");
        
        Button lampsBtn = createMenuButton("💡 Lampes", VaadinIcon.LIGHTBULB, () -> {
            UI.getCurrent().navigate("lamps");
        });
        
        Button sensorsBtnCo2 = createMenuButton("🌡️ Capteurs CO2", VaadinIcon.SIGNAL, () -> {
            UI.getCurrent().navigate("sensorco2");
        });

        Button sensors9Btn = createMenuButton("📡 Capteurs 9in1", VaadinIcon.SIGNAL, () -> {
            UI.getCurrent().navigate("sensor9in1");
        });
        
        Button sensors6Btn = createMenuButton("📡 Capteurs 6-in-1", VaadinIcon.SIGNAL, () -> {
            UI.getCurrent().navigate("sensor6in1");
        });
        
        Button heatersBtn = createMenuButton("🔥 Radiateurs", VaadinIcon.FIRE, () -> {
            UI.getCurrent().navigate("heaters");
        });
        
        Button windowsBtn = createMenuButton("🪟 Fenêtres", VaadinIcon.HOME, () -> {
            UI.getCurrent().navigate("windows");
        });
        
        Button doorsBtn = createMenuButton("🚪 Portes", VaadinIcon.HOME, () -> {
            UI.getCurrent().navigate("doors");
        });

        Button plugBtn = createMenuButton("🔌 Prises", VaadinIcon.PLUG, () -> {
            UI.getCurrent().navigate("plugs");
        });

        Button sirenBtn = createMenuButton("🚨 Alarmes", VaadinIcon.ALARM, () -> {
            UI.getCurrent().navigate("sirens");
        });

        Button tableBtn = createMenuButton("🗄 Tables", VaadinIcon.HOME, () -> {
            UI.getCurrent().navigate("data-tables");
        });
        
        mainMenu.add(separator1, devicesLabel, lampsBtn, sensorsBtnCo2, sensors6Btn, sensors9Btn, plugBtn, sirenBtn, tableBtn,
                    heatersBtn, windowsBtn, doorsBtn);
        
        menuContainer.add(mainMenu);
    }
    
    private void createBuildingsSection() {
        // Séparateur
        Hr separator2 = new Hr();
        separator2.getStyle().set("margin", "10px 0").set("border-color", "#dee2e6");
        
        // Label section
        Span buildingsLabel = new Span("🏘️ BÂTIMENTS & SALLES");
        buildingsLabel.getStyle()
            .set("font-size", "0.8em")
            .set("font-weight", "bold")
            .set("color", "#6c757d")
            .set("margin", "10px 0 5px 15px");
        
        VerticalLayout buildingsSection = new VerticalLayout();
        buildingsSection.setPadding(false);
        buildingsSection.setSpacing(false);
        buildingsSection.getStyle().set("padding", "0 15px");
        
        buildingsSection.add(separator2, buildingsLabel);
        
        try {
            List<Building> buildings = buildingRepository.findAll();
            
            if (buildings.isEmpty()) {
                Span emptyMessage = new Span("Aucun bâtiment configuré");
                emptyMessage.getStyle()
                    .set("color", "#6c757d")
                    .set("font-style", "italic")
                    .set("padding", "10px 15px");
                buildingsSection.add(emptyMessage);
            } else {
                for (Building building : buildings) {
                    buildingsSection.add(createBuildingSection(building));
                }
            }
            
        } catch (Exception ex) {
            Div errorDiv = new Div();
            errorDiv.getStyle()
                .set("background", "#f8d7da")
                .set("color", "#721c24")
                .set("padding", "10px")
                .set("border-radius", "5px")
                .set("margin", "5px")
                .set("font-size", "0.9em");
            errorDiv.add(new Span("❌ Erreur: " + ex.getMessage()));
            buildingsSection.add(errorDiv);
        }
        
        menuContainer.add(buildingsSection);
    }
    
    private VerticalLayout createBuildingSection(Building building) {
        VerticalLayout buildingSection = new VerticalLayout();
        buildingSection.setPadding(false);
        buildingSection.setSpacing(false);
        buildingSection.getStyle()
            .set("margin", "5px 0")
            .set("border-radius", "8px")
            .set("overflow", "hidden");
        
        // Bouton du bâtiment
        Button buildingBtn = new Button();
        buildingBtn.setWidthFull();
        
        // Layout pour le contenu du bouton
        HorizontalLayout buttonContent = new HorizontalLayout();
        buttonContent.setWidthFull();
        buttonContent.setAlignItems(Alignment.CENTER);
        buttonContent.setPadding(false);
        buttonContent.setSpacing(true);
        
        Icon buildingIcon = new Icon(VaadinIcon.BUILDING);
        buildingIcon.getStyle().set("color", "#3498db");
        
        Span buildingName = new Span(building.getName());
        buildingName.getStyle()
            .set("font-weight", "500")
            .set("color", "#2c3e50");
        
        // Compter les salles
        List<Room> rooms = roomRepository.findByBuilding_Id(building.getId());
        Span roomCount = new Span("(" + rooms.size() + ")");
        roomCount.getStyle()
            .set("color", "#6c757d")
            .set("font-size", "0.9em");
        
        Icon expandIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        expandIcon.getStyle()
            .set("color", "#6c757d")
            .set("font-size", "0.8em");
        
        buttonContent.add(buildingIcon, buildingName, roomCount);
        buttonContent.setFlexGrow(1, buildingName);
        buttonContent.add(expandIcon);
        
        buildingBtn.getElement().appendChild(buttonContent.getElement());
        
        buildingBtn.getStyle()
            .set("background", "white")
            .set("border", "1px solid #e9ecef")
            .set("border-radius", "8px")
            .set("margin", "2px 0")
            .set("padding", "12px 15px")
            .set("text-align", "left")
            .set("transition", "all 0.2s ease");
        
        // Effet hover
        buildingBtn.getElement().addEventListener("mouseenter", e -> {
            buildingBtn.getStyle()
                .set("background", "#f8f9fa")
                .set("transform", "translateX(5px)")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        });
        buildingBtn.getElement().addEventListener("mouseleave", e -> {
            buildingBtn.getStyle()
                .set("background", "white")
                .set("transform", "translateX(0)")
                .set("box-shadow", "none");
        });
        
        // Sous-menu des salles
        VerticalLayout roomsSubMenu = new VerticalLayout();
        roomsSubMenu.setPadding(false);
        roomsSubMenu.setSpacing(false);
        roomsSubMenu.setVisible(false);
        roomsSubMenu.getStyle()
            .set("background", "#f8f9fa")
            .set("border-radius", "0 0 8px 8px")
            .set("margin-top", "-2px")
            .set("padding", "5px");
        
        // Action du bouton building
        buildingBtn.addClickListener(ev -> {
            boolean isVisible = roomsSubMenu.isVisible();
            roomsSubMenu.setVisible(!isVisible);
            
            // Changer l'icône
            expandIcon.getElement().setAttribute("icon", 
                isVisible ? "vaadin:chevron-down" : "vaadin:chevron-up");
        });
        
        // Ajouter les salles
        for (Room room : rooms) {
            Button roomBtn = createRoomButton(room);
            roomsSubMenu.add(roomBtn);
        }
        
        if (rooms.isEmpty()) {
            Span noRooms = new Span("Aucune salle");
            noRooms.getStyle()
                .set("color", "#6c757d")
                .set("font-style", "italic")
                .set("padding", "8px 20px")
                .set("font-size", "0.9em");
            roomsSubMenu.add(noRooms);
        }
        
        buildingSection.add(buildingBtn, roomsSubMenu);
        
        return buildingSection;
    }
    
    private Button createRoomButton(Room room) {
        Button roomBtn = new Button();
        roomBtn.setWidthFull();
        
        HorizontalLayout roomContent = new HorizontalLayout();
        roomContent.setWidthFull();
        roomContent.setAlignItems(Alignment.CENTER);
        roomContent.setPadding(false);
        roomContent.setSpacing(true);
        
        Icon roomIcon = new Icon(VaadinIcon.HOME);
        roomIcon.getStyle()
            .set("color", "#2ecc71")
            .set("font-size", "0.9em");
        
        Span roomName = new Span(room.getName());
        roomName.getStyle()
            .set("color", "#495057")
            .set("font-size", "0.9em");
        
        roomContent.add(roomIcon, roomName);
        roomContent.setFlexGrow(1, roomName);
        
        roomBtn.getElement().appendChild(roomContent.getElement());
        
        roomBtn.getStyle()
            .set("background", "transparent")
            .set("border", "none")
            .set("padding", "8px 20px")
            .set("margin", "1px 0")
            .set("border-radius", "5px")
            .set("text-align", "left")
            .set("transition", "background 0.2s ease");
        
        roomBtn.getElement().addEventListener("mouseenter", e -> {
            roomBtn.getStyle().set("background", "rgba(52, 152, 219, 0.1)");
        });
        roomBtn.getElement().addEventListener("mouseleave", e -> {
            roomBtn.getStyle().set("background", "transparent");
        });
        
        roomBtn.addClickListener(click -> {
            UI.getCurrent().navigate("room/" + room.getId());
        });
        
        return roomBtn;
    }
    
    private Button createMenuButton(String text, VaadinIcon icon, Runnable action) {
        Button button = new Button();
        button.setWidthFull();
        
        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.setAlignItems(Alignment.CENTER);
        content.setPadding(false);
        content.setSpacing(true);
        
        Icon buttonIcon = new Icon(icon);
        buttonIcon.getStyle().set("font-size", "1.1em");
        
        Span buttonText = new Span(text);
        buttonText.getStyle().set("font-weight", "500");
        
        content.add(buttonIcon, buttonText);
        content.setFlexGrow(1, buttonText);
        
        button.getElement().appendChild(content.getElement());
        
        button.getStyle()
            .set("background", "white")
            .set("border", "1px solid #e9ecef")
            .set("border-radius", "8px")
            .set("margin", "2px 0")
            .set("padding", "12px 15px")
            .set("text-align", "left")
            .set("transition", "all 0.2s ease");
        
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                .set("background", "#3498db")
                .set("color", "white")
                .set("transform", "translateX(5px)")
                .set("box-shadow", "0 2px 8px rgba(52, 152, 219, 0.3)");
            buttonIcon.getStyle().set("color", "white");
            buttonText.getStyle().set("color", "white");
        });
        
        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                .set("background", "white")
                .set("color", "inherit")
                .set("transform", "translateX(0)")
                .set("box-shadow", "none");
            buttonIcon.getStyle().set("color", "inherit");
            buttonText.getStyle().set("color", "inherit");
        });
        
        button.addClickListener(e -> action.run());
        
        return button;
    }
    
    private void createDrawerFooter() {
        VerticalLayout footer = new VerticalLayout();
        footer.setPadding(true);
        footer.setSpacing(false);
        footer.getStyle()
            .set("margin-top", "auto")
            .set("background", "rgba(108, 117, 125, 0.1)")
            .set("border-top", "1px solid #dee2e6");
        
        Span version = new Span("Home Assistant v2.0");
        version.getStyle()
            .set("font-size", "0.8em")
            .set("color", "#6c757d")
            .set("text-align", "center");
        
        Span copyright = new Span("© 2024 SAE Project");
        copyright.getStyle()
            .set("font-size", "0.7em")
            .set("color", "#adb5bd")
            .set("text-align", "center");
        
        footer.add(version, copyright);
        footer.setAlignItems(Alignment.CENTER);
        
        menuContainer.add(footer);
    }
}
