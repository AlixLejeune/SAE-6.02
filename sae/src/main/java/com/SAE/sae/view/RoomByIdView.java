package com.SAE.sae.view;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.RoomType;
import com.SAE.sae.entity.RoomObjects.*;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.service.RoomTypeManager;
import com.SAE.sae.service.RoomObjects.*;
import com.SAE.sae.view.layouts.MainLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Route(value = "room", layout = MainLayout.class)
public class RoomByIdView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RoomManager roomManager;
    private final RoomTypeManager roomTypeManager;
    private final LampManager lampManager;
    private final PlugManager plugManager;
    private final SensorCO2Manager sensorCO2Manager;
    private final Sensor6in1Manager sensor6in1Manager;
    private final Sensor9in1Manager sensor9in1Manager;
    private final HeaterManager heaterManager;
    private final WindowManager windowManager;
    private final DoorManager doorManager;
    private final DataTableManager dataTableManager;
    private final SirenManager sirenManager;

    private Room currentRoom;
    private Grid<EquipmentItem> equipmentGrid = new Grid<>(EquipmentItem.class);
    private List<EquipmentItem> allEquipmentItems; // Liste complète pour la recherche
    private TextField searchField; // Champ de recherche

    @Autowired
    public RoomByIdView(RoomManager roomManager, RoomTypeManager roomTypeManager,
                       LampManager lampManager, PlugManager plugManager,
                       SensorCO2Manager sensorCO2Manager, Sensor6in1Manager sensor6in1Manager,
                       Sensor9in1Manager sensor9in1Manager, HeaterManager heaterManager,
                       WindowManager windowManager, DoorManager doorManager,
                       DataTableManager dataTableManager, SirenManager sirenManager) {
        this.roomManager = roomManager;
        this.roomTypeManager = roomTypeManager;
        this.lampManager = lampManager;
        this.plugManager = plugManager;
        this.sensorCO2Manager = sensorCO2Manager;
        this.sensor6in1Manager = sensor6in1Manager;
        this.sensor9in1Manager = sensor9in1Manager;
        this.heaterManager = heaterManager;
        this.windowManager = windowManager;
        this.doorManager = doorManager;
        this.dataTableManager = dataTableManager;
        this.sirenManager = sirenManager;

        // Configuration générale de la vue
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Integer roomId) {
        try {
            currentRoom = roomManager.getRoomById(roomId);
            initializeView();
        } catch (Exception e) {
            showErrorNotification("Erreur", "Salle non trouvée avec l'ID: " + roomId);
            // Rediriger vers la liste des salles
            event.getUI().navigate("rooms");
        }
    }

    private void initializeView() {
        removeAll();
        
        createHeader();
        createRoomInfoPanelCompact(); // Déplacé au sommet
        createEquipmentGrid();
        createLayout();
        
        loadEquipmentData();
    }

    private void createHeader() {
        H2 title = new H2("🏠 Détails de la Salle");
        title.getStyle()
            .set("color", "white")
            .set("margin", "0 0 20px 0")
            .set("text-align", "center")
            .set("font-weight", "300")
            .set("text-shadow", "2px 2px 4px rgba(0,0,0,0.3)");
        
        add(title);
    }

    private void createRoomInfoPanelCompact() {
        // Panel principal d'informations compact au sommet
        Div infoPanel = new Div();
        infoPanel.getStyle()
            .set("background", "white")
            .set("border-radius", "12px")
            .set("padding", "15px")
            .set("margin-bottom", "15px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Titre de la salle
        H3 roomTitle = new H3("📍 " + currentRoom.getName());
        roomTitle.getStyle()
            .set("margin", "0 0 12px 0")
            .set("color", "#2c3e50")
            .set("font-weight", "600")
            .set("font-size", "1.3em");

        // Layout horizontal pour les informations - SEULEMENT 2 colonnes
        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setWidthFull();
        infoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        infoLayout.setAlignItems(FlexComponent.Alignment.START);
        infoLayout.setSpacing(true);

        // Informations générales
        VerticalLayout generalInfo = createCompactInfoCard("🏢 Informations", 
            "ID: " + currentRoom.getId(),
            "Bâtiment: " + (currentRoom.getBuilding() != null ? currentRoom.getBuilding().getName() : "Aucun"),
            "Type: " + getRoomTypeName());

        // Dimensions
        VerticalLayout dimensionsInfo = createCompactInfoCard("📐 Dimensions",
            String.format("%.1f × %.1f × %.1f m", currentRoom.getWidth(), currentRoom.getLength(), currentRoom.getHeight()),
            String.format("Volume: %.1f m³", currentRoom.getWidth() * currentRoom.getLength() * currentRoom.getHeight()));

        // SEULEMENT 2 colonnes - pas de colonne équipements vide
        infoLayout.add(generalInfo, dimensionsInfo);
        infoPanel.add(roomTitle, infoLayout);
        add(infoPanel);
    }

    private VerticalLayout createCompactInfoCard(String title, String... items) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(false);
        card.setSpacing(false);
        card.getStyle()
            .set("background", "#f8f9fa")
            .set("border-radius", "8px")
            .set("padding", "12px")
            .set("min-width", "250px")
            .set("flex", "1"); // Permet aux cards de prendre l'espace disponible

        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
            .set("margin", "0 0 8px 0")
            .set("font-size", "1em")
            .set("color", "#495057")
            .set("font-weight", "500");

        card.add(cardTitle);

        for (String item : items) {
            Span itemSpan = new Span(item);
            itemSpan.getStyle()
                .set("font-size", "0.9em")
                .set("color", "#6c757d")
                .set("display", "block")
                .set("margin-bottom", "4px")
                .set("line-height", "1.3");
            card.add(itemSpan);
        }

        return card;
    }

    private void createEquipmentSearchField() {
        searchField = new TextField();
        searchField.setPlaceholder("🔍 Rechercher dans les équipements...");
        searchField.setWidth("400px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(300);
        
        searchField.getStyle()
            .set("border-radius", "8px")
            .set("font-size", "14px")
            .set("background", "white")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("border", "1px solid rgba(255,255,255,0.3)");

        searchField.addValueChangeListener(e -> filterEquipment(e.getValue()));

        // Container pour centrer la barre de recherche avec style blanc
        Div searchContainer = new Div();
        searchContainer.getStyle()
            .set("text-align", "center")
            .set("margin-bottom", "15px")
            .set("background", "white")
            .set("border-radius", "10px")
            .set("padding", "15px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("backdrop-filter", "blur(10px)")
            .set("border", "1px solid rgba(255,255,255,0.2)");
        
        searchContainer.add(searchField);
        add(searchContainer);
    }

    private void createEquipmentGrid() {
        // En-tête pour le tableau des équipements
        H3 equipmentTitle = new H3("🔧 Équipements de la salle");
        equipmentTitle.getStyle()
            .set("color", "white")
            .set("margin", "20px 0 10px 0")
            .set("text-align", "center")
            .set("font-weight", "300")
            .set("text-shadow", "2px 2px 4px rgba(0,0,0,0.3)");

        add(equipmentTitle);

        // Créer la barre de recherche
        createEquipmentSearchField();

        // Configuration du grid
        equipmentGrid.removeAllColumns();

        equipmentGrid.addColumn(EquipmentItem::getType)
            .setHeader("Type")
            .setWidth("140px")
            .setFlexGrow(0);

        equipmentGrid.addColumn(EquipmentItem::getName)
            .setHeader("Nom")
            .setFlexGrow(2);

        equipmentGrid.addColumn(EquipmentItem::getPosition)
            .setHeader("Position (X, Y, Z)")
            .setFlexGrow(1);

        equipmentGrid.addColumn(EquipmentItem::getSize)
            .setHeader("Taille (L × l × H)")
            .setFlexGrow(1);

        // Configuration pour occuper BEAUCOUP plus d'espace vertical
        equipmentGrid.setSizeFull();
        equipmentGrid.setMinHeight("600px"); // Augmenté de 400px à 600px
        equipmentGrid.setMaxHeight("90vh"); // Augmenté de 80vh à 90vh
        
        // Style du grid
        equipmentGrid.getStyle()
            .set("border-radius", "12px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Classe pour les types d'équipements avec couleurs
        equipmentGrid.setClassNameGenerator(item -> "equipment-" + item.getType().toLowerCase().replace(" ", "-").replace("💡", "").replace("🔌", "").replace("🌫️", "").replace("🔬", "").replace("🔥", "").replace("🪟", "").replace("🚪", "").replace("📊", "").replace("🚨", "").trim());

        // Injection CSS pour les couleurs
        equipmentGrid.getElement().executeJs(
            "this.shadowRoot.querySelector('style').textContent += " +
            "'.equipment-lampe { background-color: #fff3cd; }' + " +
            "'.equipment-prise { background-color: #d4edda; }' + " +
            "'.equipment-capteur-co2 { background-color: #cce5ff; }' + " +
            "'.equipment-capteur-6-en-1 { background-color: #e2d5f1; }' + " +
            "'.equipment-capteur-9-en-1 { background-color: #f8d7da; }' + " +
            "'.equipment-radiateur { background-color: #ffeaa7; }' + " +
            "'.equipment-fenêtre { background-color: #a8e6cf; }' + " +
            "'.equipment-porte { background-color: #dcedc1; }' + " +
            "'.equipment-table-de-données { background-color: #ffd3a5; }' + " +
            "'.equipment-sirène { background-color: #ffaaa5; }'"
        );
    }

    private void createLayout() {
        // Container pour le grid avec BEAUCOUP plus d'espace vertical
        Div gridContainer = new Div();
        gridContainer.setSizeFull();
        gridContainer.getStyle()
            .set("background", "white")
            .set("border-radius", "12px")
            .set("padding", "20px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("overflow", "hidden")
            .set("flex-grow", "2") // Augmenté de 1 à 2
            .set("min-height", "70vh"); // Hauteur minimale augmentée

        gridContainer.add(equipmentGrid);
        add(gridContainer);
        
        // Permet à ce container de prendre tout l'espace disponible
        expand(gridContainer);
    }

    private void filterEquipment(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            equipmentGrid.setItems(allEquipmentItems);
        } else {
            List<EquipmentItem> filteredItems = allEquipmentItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                item.getType().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
            equipmentGrid.setItems(filteredItems);
        }
    }

    private void loadEquipmentData() {
        List<EquipmentItem> equipmentList = new ArrayList<>();
        Long roomId = currentRoom.getId().longValue();

        try {
            // Charger tous les types d'équipements
            loadLamps(equipmentList, roomId);
            loadPlugs(equipmentList, roomId);
            loadSensorCO2s(equipmentList, roomId);
            loadSensor6in1s(equipmentList, roomId);
            loadSensor9in1s(equipmentList, roomId);
            loadHeaters(equipmentList, roomId);
            loadWindows(equipmentList, roomId);
            loadDoors(equipmentList, roomId);
            loadDataTables(equipmentList, roomId);
            loadSirens(equipmentList, roomId);

            // Stocker la liste complète pour la recherche
            allEquipmentItems = new ArrayList<>(equipmentList);
            
            equipmentGrid.setItems(equipmentList);
            updateEquipmentStats(equipmentList);

            Notification notification = Notification.show(
                "✅ " + equipmentList.size() + " équipement(s) trouvé(s)",
                3000,
                Notification.Position.TOP_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Clear la recherche
            if (searchField != null) {
                searchField.clear();
            }

        } catch (Exception e) {
            
        }
    }

    private void loadLamps(List<EquipmentItem> list, Long roomId) {
        List<Lamp> lamps = lampManager.findByRoomId(roomId);
        for (Lamp lamp : lamps) {
            list.add(new EquipmentItem(
                "💡 Lampe",
                lamp.getCustomName(),
                formatPosition(lamp.getPosX(), lamp.getPosY(), lamp.getPosZ()),
                "-"
            ));
        }
    }

    private void loadPlugs(List<EquipmentItem> list, Long roomId) {
        List<Plug> plugs = plugManager.findByRoomId(roomId);
        for (Plug plug : plugs) {
            list.add(new EquipmentItem(
                "🔌 Prise",
                plug.getCustomName(),
                formatPosition(plug.getPosX(), plug.getPosY(), plug.getPosZ()),
                "-"
            ));
        }
    }

    private void loadSensorCO2s(List<EquipmentItem> list, Long roomId) {
        List<SensorCO2> sensors = sensorCO2Manager.findByRoomId(roomId);
        for (SensorCO2 sensor : sensors) {
            list.add(new EquipmentItem(
                "🌫️ Capteur CO2",
                sensor.getCustomName(),
                formatPosition(sensor.getPosX(), sensor.getPosY(), sensor.getPosZ()),
                "-"
            ));
        }
    }

    private void loadSensor6in1s(List<EquipmentItem> list, Long roomId) {
        List<Sensor6in1> sensors = sensor6in1Manager.findByRoomId(roomId);
        for (Sensor6in1 sensor : sensors) {
            list.add(new EquipmentItem(
                "🔬 Capteur 6-en-1",
                sensor.getCustomName(),
                formatPosition(sensor.getPosX(), sensor.getPosY(), sensor.getPosZ()),
                "-"
            ));
        }
    }

    private void loadSensor9in1s(List<EquipmentItem> list, Long roomId) {
        List<Sensor9in1> sensors = sensor9in1Manager.findByRoomId(roomId);
        for (Sensor9in1 sensor : sensors) {
            list.add(new EquipmentItem(
                "🔬 Capteur 9-en-1",
                sensor.getCustomName(),
                formatPosition(sensor.getPosX(), sensor.getPosY(), sensor.getPosZ()),
                "-"
            ));
        }
    }

    private void loadHeaters(List<EquipmentItem> list, Long roomId) {
        List<Heater> heaters = heaterManager.findByRoomId(roomId);
        for (Heater heater : heaters) {
            list.add(new EquipmentItem(
                "🔥 Radiateur",
                heater.getCustomName(),
                formatPosition(heater.getPosX(), heater.getPosY(), heater.getPosZ()),
                formatSize(heater.getSizeX(), heater.getSizeY(), heater.getSizeZ())
            ));
        }
    }

    private void loadWindows(List<EquipmentItem> list, Long roomId) {
        List<Window> windows = windowManager.findByRoomId(roomId);
        for (Window window : windows) {
            list.add(new EquipmentItem(
                "🪟 Fenêtre",
                window.getCustomName(),
                formatPosition(window.getPosX(), window.getPosY(), window.getPosZ()),
                formatSize(window.getSizeX(), window.getSizeY(), window.getSizeZ())
            ));
        }
    }

    private void loadDoors(List<EquipmentItem> list, Long roomId) {
        List<Door> doors = doorManager.findByRoomId(roomId);
        for (Door door : doors) {
            list.add(new EquipmentItem(
                "🚪 Porte",
                door.getCustomName(),
                formatPosition(door.getPosX(), door.getPosY(), door.getPosZ()),
                formatSize(door.getSizeX(), door.getSizeY(), door.getSizeZ())
            ));
        }
    }

    private void loadDataTables(List<EquipmentItem> list, Long roomId) {
        List<DataTable> dataTables = dataTableManager.findByRoomId(roomId);
        for (DataTable dataTable : dataTables) {
            list.add(new EquipmentItem(
                "📊 Table de données",
                dataTable.getCustomName(),
                formatPosition(dataTable.getPosX(), dataTable.getPosY(), dataTable.getPosZ()),
                formatSize(dataTable.getSizeX(), dataTable.getSizeY(), dataTable.getSizeZ())
            ));
        }
    }

    private void loadSirens(List<EquipmentItem> list, Long roomId) {
        List<Siren> sirens = sirenManager.findByRoomId(roomId);
        for (Siren siren : sirens) {
            list.add(new EquipmentItem(
                "🚨 Sirène",
                siren.getCustomName(),
                formatPosition(siren.getPosX(), siren.getPosY(), siren.getPosZ()),
                "-"
            ));
        }
    }

    private void updateEquipmentStats(List<EquipmentItem> equipmentList) {
        // Compter par type pour information (optionnel, pour les logs)
        long lampCount = equipmentList.stream().filter(e -> e.getType().contains("Lampe")).count();
        long plugCount = equipmentList.stream().filter(e -> e.getType().contains("Prise")).count();
        long sensorCount = equipmentList.stream().filter(e -> e.getType().contains("Capteur")).count();
        long heaterCount = equipmentList.stream().filter(e -> e.getType().contains("Radiateur")).count();
        long windowCount = equipmentList.stream().filter(e -> e.getType().contains("Fenêtre")).count();
        long doorCount = equipmentList.stream().filter(e -> e.getType().contains("Porte")).count();
        long dataTableCount = equipmentList.stream().filter(e -> e.getType().contains("Table")).count();
        long sirenCount = equipmentList.stream().filter(e -> e.getType().contains("Sirène")).count();
        
        // Optionnel: Log pour debug
        System.out.println("Équipements chargés: " + equipmentList.size() + 
                      " (Lampes: " + lampCount + ", Prises: " + plugCount + ", etc.)");
    }

    private String getRoomTypeName() {
        if (currentRoom.getFkRoomTypeId() > 0) {
            try {
                RoomType roomType = roomTypeManager.getRoomTypeById(currentRoom.getFkRoomTypeId());
                return roomType != null ? roomType.getName() : "Type inconnu";
            } catch (Exception e) {
                return "Erreur de chargement";
            }
        }
        return "Aucun type défini";
    }

    private String formatPosition(Double x, Double y, Double z) {
        if (x == null || y == null || z == null) {
            return "Position non définie";
        }
        return String.format("%.1f, %.1f, %.1f", x, y, z);
    }

    private String formatSize(Double x, Double y, Double z) {
        if (x == null || y == null || z == null) {
            return "-";
        }
        return String.format("%.1f × %.1f × %.1f", x, y, z);
    }

    private void showErrorNotification(String title, String message) {
        Notification notification = Notification.show("❌ " + title + ": " + message, 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    // Classe interne pour représenter un équipement dans le grid
    public static class EquipmentItem {
        private String type;
        private String name;
        private String position;
        private String size;

        public EquipmentItem(String type, String name, String position, String size) {
            this.type = type;
            this.name = name;
            this.position = position;
            this.size = size;
        }

        public String getType() { return type; }
        public String getName() { return name; }
        public String getPosition() { return position; }
        public String getSize() { return size; }
    }
}