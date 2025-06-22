package com.SAE.sae.view;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.service.RoomObjects.*;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    private final BuildingManager buildingManager;
    private final RoomManager roomManager;
    private final LampManager lampManager;
    private final SensorCO2Manager sensorCO2Manager;
    private final Sensor6in1Manager sensor6in1Manager;
    private final HeaterManager heaterManager;
    private final WindowManager windowManager;
    private final DoorManager doorManager;

    @Autowired
    public HomeView(BuildingManager buildingManager, RoomManager roomManager,
                    LampManager lampManager, SensorCO2Manager sensorCO2Manager,
                    Sensor6in1Manager sensor6in1Manager, HeaterManager heaterManager,
                    WindowManager windowManager, DoorManager doorManager) {
        this.buildingManager = buildingManager;
        this.roomManager = roomManager;
        this.lampManager = lampManager;
        this.sensorCO2Manager = sensorCO2Manager;
        this.sensor6in1Manager = sensor6in1Manager;
        this.heaterManager = heaterManager;
        this.windowManager = windowManager;
        this.doorManager = doorManager;
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        // Correction pour que le fond prenne toute la hauteur
        getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("min-height", "100vh") // Hauteur minimale de la viewport
            .set("background-attachment", "fixed"); // Le fond reste fixe lors du scroll
        
        add(createHeader());
        add(createKPISection());
        add(createSystemOverview());
    }
    
    private VerticalLayout createHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(false);
        
        H1 title = new H1("🏠 Home Assistant Dashboard");
        title.getStyle()
            .set("color", "white")
            .set("text-align", "center")
            .set("margin", "0")
            .set("font-size", "2.5em")
            .set("text-shadow", "2px 2px 4px rgba(0,0,0,0.3)");
        
        Span subtitle = new Span("Tableau de bord intelligent pour la gestion de votre domicile");
        subtitle.getStyle()
            .set("color", "rgba(255,255,255,0.8)")
            .set("text-align", "center")
            .set("font-size", "1.1em")
            .set("margin-top", "10px");
        
        header.add(title, subtitle);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        
        return header;
    }
    
    private HorizontalLayout createKPISection() {
        HorizontalLayout kpiLayout = new HorizontalLayout();
        kpiLayout.setWidthFull();
        kpiLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        kpiLayout.setSpacing(true);
        
        try {
            List<Building> buildings = buildingManager.getAllBuildings();
            List<Room> rooms = roomManager.getAllRooms();
            long totalDevices = getTotalDevices();
            long activeDevices = getActiveDevices();
            
            kpiLayout.add(
                createKPICard("🏢", "Bâtiments", buildings.size(), "Structures", "#3498db"),
                createKPICard("🚪", "Salles", rooms.size(), "Espaces", "#2ecc71"),
                createKPICard("📱", "Appareils", (int)totalDevices, "Connectés", "#e74c3c"),
                createKPICard("⚡", "Actifs", (int)activeDevices, "En ligne", "#f39c12")
            );
            
        } catch (Exception e) {
            kpiLayout.add(createErrorCard("Erreur de chargement des données"));
        }
        
        return kpiLayout;
    }
    
    private VerticalLayout createKPICard(String icon, String title, int value, String subtitle, String color) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background", "white")
            .set("border-radius", "15px")
            .set("padding", "25px")
            .set("text-align", "center")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)")
            .set("transition", "transform 0.3s ease")
            .set("cursor", "pointer")
            .set("min-width", "180px")
            .set("position", "relative")
            .set("overflow", "hidden");
        
        // Effet de hover
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle().set("transform", "translateY(-5px)");
        });
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle().set("transform", "translateY(0)");
        });
        
        // Barre colorée en haut
        Div colorBar = new Div();
        colorBar.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("right", "0")
            .set("height", "4px")
            .set("background", color);
        
        Span iconSpan = new Span(icon);
        iconSpan.getStyle()
            .set("font-size", "3em")
            .set("margin-bottom", "10px")
            .set("display", "block");
        
        H2 valueH2 = new H2(String.valueOf(value));
        valueH2.getStyle()
            .set("margin", "0")
            .set("color", color)
            .set("font-size", "2.5em")
            .set("font-weight", "bold");
        
        H4 titleH4 = new H4(title);
        titleH4.getStyle()
            .set("margin", "5px 0")
            .set("color", "#2c3e50")
            .set("font-size", "1.2em");
        
        Span subtitleSpan = new Span(subtitle);
        subtitleSpan.getStyle()
            .set("color", "#7f8c8d")
            .set("font-size", "0.9em");
        
        card.add(colorBar, iconSpan, valueH2, titleH4, subtitleSpan);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        
        return card;
    }
    
    private HorizontalLayout createSystemOverview() {
        HorizontalLayout overview = new HorizontalLayout();
        overview.setWidthFull();
        overview.setSpacing(true);
        
        // Graphique de répartition des appareils
        VerticalLayout deviceChart = createDeviceDistributionChart();
        
        // Statut des bâtiments
        VerticalLayout buildingStatus = createBuildingStatusPanel();
        
        overview.add(deviceChart, buildingStatus);
        overview.setFlexGrow(1, deviceChart);
        overview.setFlexGrow(1, buildingStatus);
        
        return overview;
    }
    
    private VerticalLayout createDeviceDistributionChart() {
        VerticalLayout container = new VerticalLayout();
        container.getStyle()
            .set("background", "white")
            .set("border-radius", "15px")
            .set("padding", "25px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)")
            .set("min-height", "400px") // Hauteur minimale
            .set("max-height", "600px") // Hauteur maximale
            .set("overflow-y", "auto"); // Scroll si nécessaire
        
        // Réduire l'espacement du container
        container.setSpacing(false);
        container.setPadding(false);
        container.getStyle().set("padding", "20px");
        
        H3 title = new H3("📊 Répartition des Appareils");
        title.getStyle()
            .set("margin-top", "0")
            .set("margin-bottom", "15px")
            .set("color", "#2c3e50")
            .set("text-align", "center");
        
        container.add(title);
        
        try {
            // Container pour les barres avec espacement approprié
            VerticalLayout barsContainer = new VerticalLayout();
            barsContainer.setPadding(false);
            barsContainer.setSpacing(false);
            barsContainer.getStyle().set("gap", "5px");
            
            // Création d'un graphique simple avec des barres CSS
            barsContainer.add(createDeviceBar("💡 Lampes", (int)lampManager.count(), "#f1c40f"));
            barsContainer.add(createDeviceBar("🌡️ Capteurs CO2", (int)sensorCO2Manager.count(), "#e74c3c"));
            barsContainer.add(createDeviceBar("📡 Capteurs 6-in-1", (int)sensor6in1Manager.count(), "#3498db"));
            barsContainer.add(createDeviceBar("🔥 Radiateurs", (int)heaterManager.count(), "#e67e22"));
            barsContainer.add(createDeviceBar("🪟 Fenêtres", (int)windowManager.count(), "#9b59b6"));
            barsContainer.add(createDeviceBar("🚪 Portes", (int)doorManager.count(), "#34495e"));
            
            container.add(barsContainer);
            
            // Résumé total
            long totalDevices = getTotalDevices();
            if (totalDevices > 0) {
                Span totalSummary = new Span("Total : " + totalDevices + " appareils");
                totalSummary.getStyle()
                    .set("font-size", "0.9em")
                    .set("color", "#6c757d")
                    .set("text-align", "center")
                    .set("margin-top", "15px")
                    .set("padding-top", "15px")
                    .set("border-top", "1px solid #ecf0f1");
                container.add(totalSummary);
            }
            
        } catch (Exception e) {
            container.add(new Span("❌ Erreur lors du chargement des données"));
        }
        
        return container;
    }
    
    private HorizontalLayout createDeviceBar(String label, int count, String color) {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.setAlignItems(FlexComponent.Alignment.CENTER);
        bar.getStyle().set("margin", "5px 0"); // Réduire la marge de 10px à 5px
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("min-width", "130px") // Réduire légèrement la largeur
            .set("font-weight", "500")
            .set("color", "#2c3e50")
            .set("font-size", "0.9em"); // Réduire légèrement la taille de police
        
        Div progressContainer = new Div();
        progressContainer.getStyle()
            .set("flex", "1")
            .set("background", "#ecf0f1")
            .set("border-radius", "8px") // Réduire le border-radius
            .set("height", "18px") // Réduire la hauteur de 20px à 18px
            .set("margin", "0 12px") // Réduire la marge de 15px à 12px
            .set("position", "relative")
            .set("overflow", "hidden");
        
        Div progress = new Div();
        // Calculer le pourcentage par rapport au maximum
        int maxDevices = Math.max(1, getMaxDeviceCount());
        double percentage = (double) count / maxDevices * 100;
        
        progress.getStyle()
            .set("background", color)
            .set("height", "100%")
            .set("border-radius", "8px")
            .set("width", Math.min(percentage, 100) + "%")
            .set("transition", "width 1s ease");
        
        progressContainer.add(progress);
        
        Span countSpan = new Span(String.valueOf(count));
        countSpan.getStyle()
            .set("font-weight", "bold")
            .set("color", color)
            .set("min-width", "25px") // Réduire légèrement
            .set("font-size", "0.9em"); // Réduire la taille de police
        
        bar.add(labelSpan, progressContainer, countSpan);
        
        return bar;
    }
    
    private VerticalLayout createBuildingStatusPanel() {
        VerticalLayout panel = new VerticalLayout();
        panel.getStyle()
            .set("background", "white")
            .set("border-radius", "15px")
            .set("padding", "25px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)")
            .set("min-height", "400px") // Hauteur minimale au lieu de hauteur fixe
            .set("max-height", "600px") // Hauteur maximale pour éviter un cadre trop grand
            .set("overflow-y", "auto"); // Scroll vertical si nécessaire
        
        // Ajuster l'espacement et le padding du panel
        panel.setSpacing(true);
        panel.setPadding(false);
        panel.getStyle().set("padding", "25px"); // Garder le padding CSS
        
        H3 title = new H3("🏘️ Statut des Bâtiments");
        title.getStyle()
            .set("margin-top", "0")
            .set("margin-bottom", "20px") // Espacement cohérent
            .set("color", "#2c3e50")
            .set("text-align", "center");
        
        panel.add(title);
        
        try {
            List<Building> buildings = buildingManager.getAllBuildings();
            
            if (buildings.isEmpty()) {
                // Message quand aucun bâtiment n'est présent
                Div emptyState = new Div();
                emptyState.getStyle()
                    .set("text-align", "center")
                    .set("padding", "40px 20px")
                    .set("color", "#6c757d");
                
                Span emptyIcon = new Span("🏗️");
                emptyIcon.getStyle().set("font-size", "3em").set("display", "block");
                
                Span emptyText = new Span("Aucun bâtiment configuré");
                emptyText.getStyle()
                    .set("font-size", "1.1em")
                    .set("margin-top", "10px")
                    .set("display", "block");
                
                emptyState.add(emptyIcon, emptyText);
                panel.add(emptyState);
            } else {
                // Container pour les cartes de bâtiments avec espacement approprié
                VerticalLayout buildingsContainer = new VerticalLayout();
                buildingsContainer.setPadding(false);
                buildingsContainer.setSpacing(true);
                buildingsContainer.getStyle().set("gap", "8px"); // Espacement entre les cartes
                
                for (Building building : buildings) {
                    List<Room> buildingRooms = roomManager.getRoomsByBuildingId(building.getId());
                    buildingsContainer.add(createBuildingCard(building, buildingRooms.size()));
                }
                
                panel.add(buildingsContainer);
                
                // Indicateur du nombre total de bâtiments
                if (buildings.size() > 3) { // Afficher seulement s'il y a beaucoup de bâtiments
                    Span totalIndicator = new Span("Total : " + buildings.size() + " bâtiments");
                    totalIndicator.getStyle()
                        .set("font-size", "0.9em")
                        .set("color", "#6c757d")
                        .set("text-align", "center")
                        .set("margin-top", "15px")
                        .set("padding-top", "15px")
                        .set("border-top", "1px solid #ecf0f1");
                    panel.add(totalIndicator);
                }
            }
            
        } catch (Exception e) {
            Div errorDiv = new Div();
            errorDiv.getStyle()
                .set("text-align", "center")
                .set("padding", "40px 20px")
                .set("background", "#f8d7da")
                .set("color", "#721c24")
                .set("border-radius", "8px")
                .set("margin", "10px 0");
            
            errorDiv.add(new Span("❌ Erreur lors du chargement: " + e.getMessage()));
            panel.add(errorDiv);
        }
        
        return panel;
    }
    
    private HorizontalLayout createBuildingCard(Building building, int roomCount) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidthFull();
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.getStyle()
            .set("background", "#f8f9fa")
            .set("border-radius", "10px")
            .set("padding", "15px")
            .set("margin", "8px 0")
            .set("border-left", "4px solid #3498db");
        
        Icon buildingIcon = new Icon(VaadinIcon.BUILDING);
        buildingIcon.getStyle()
            .set("color", "#3498db")
            .set("margin-right", "15px");
        
        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);
        
        H4 name = new H4(building.getName());
        name.getStyle()
            .set("margin", "0")
            .set("color", "#2c3e50");
        
        Span rooms = new Span(roomCount + " salle(s)");
        rooms.getStyle()
            .set("color", "#7f8c8d")
            .set("font-size", "0.9em");
        
        info.add(name, rooms);
        
        Span status = new Span("🟢 Actif");
        status.getStyle()
            .set("font-size", "0.9em")
            .set("color", "#27ae60");
        
        card.add(buildingIcon, info, status);
        card.setFlexGrow(1, info);
        
        return card;
    }
        
    // Méthodes utilitaires
    private long getTotalDevices() {
        try {
            return lampManager.count() + sensorCO2Manager.count() + 
                   sensor6in1Manager.count() + heaterManager.count() + 
                   windowManager.count() + doorManager.count();
        } catch (Exception e) {
            return 0;
        }
    }
    
    private long getActiveDevices() {
        // Simulation - en réalité, vous pourriez avoir un statut "actif" dans vos entités
        return (long) (getTotalDevices() * 0.85); // 85% des appareils actifs
    }
    
    private int getMaxDeviceCount() {
        try {
            return (int) Math.max(lampManager.count(), 
                   Math.max(sensorCO2Manager.count(),
                   Math.max(sensor6in1Manager.count(),
                   Math.max(heaterManager.count(),
                   Math.max(windowManager.count(), doorManager.count())))));
        } catch (Exception e) {
            return 1;
        }
    }
    
    private VerticalLayout createErrorCard(String message) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background", "white")
            .set("border-radius", "15px")
            .set("padding", "25px")
            .set("text-align", "center")
            .set("border-left", "4px solid #e74c3c");
        
        card.add(new Span("❌ " + message));
        return card;
    }
}
