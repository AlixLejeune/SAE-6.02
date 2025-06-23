package com.SAE.sae.view;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.RoomType;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.service.RoomTypeManager;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.SAE.sae.view.editor.RoomEditor;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "rooms", layout = MainLayout.class)
public class RoomView extends VerticalLayout {

    private final RoomManager roomManager;
    private final RoomTypeManager roomTypeManager;
    private final RoomEditor roomEditor;
    private final Grid<Room> grid = new Grid<>(Room.class);
    private Room selectedRoom = null;
    private List<Room> allRooms; // Liste complète pour la recherche
    private TextField searchField; // Champ de recherche

    // Boutons d'action
    private Button refreshButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    @Autowired
    public RoomView(RoomManager roomManager, BuildingManager buildingManager, RoomTypeManager roomTypeManager) {
        this.roomManager = roomManager;
        this.roomTypeManager = roomTypeManager;
        this.roomEditor = new RoomEditor(roomManager, buildingManager, roomTypeManager);
        // Configuration du callback pour rafraîchir les données
        this.roomEditor.setOnDataChanged(this::loadData);
        
        // Configuration générale de la vue
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        createHeader();
        createToolbar();
        createGrid();
        createLayout();
        
        // Chargement initial
        loadData();
    }

    private void createHeader() {
        H2 title = new H2("🏠 Gestion des Salles");
        title.getStyle()
            .set("color", "white")
            .set("margin", "0 0 20px 0")
            .set("text-align", "center")
            .set("font-weight", "300")
            .set("text-shadow", "2px 2px 4px rgba(0,0,0,0.3)");
        
        add(title);
    }

    private void createSearchField() {
        searchField = new TextField();
        searchField.setPlaceholder("🔍 Rechercher une salle...");
        searchField.setWidth("300px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(300);
        
        searchField.getStyle()
            .set("border-radius", "8px")
            .set("font-size", "14px");

        searchField.addValueChangeListener(e -> filterRooms(e.getValue()));
    }

    private void createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.getStyle()
            .set("background", "white")
            .set("padding", "15px 20px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("margin-bottom", "20px");

        // Groupe de boutons principaux (gauche)
        HorizontalLayout leftGroup = new HorizontalLayout();
        leftGroup.setSpacing(true);
        leftGroup.setAlignItems(FlexComponent.Alignment.CENTER);

        refreshButton = createStyledButton("🔄 Actualiser", VaadinIcon.REFRESH, "#3498db", ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(e -> loadData());

        addButton = createStyledButton("➕ Ajouter", VaadinIcon.PLUS, "#27ae60", ButtonVariant.LUMO_SUCCESS);
        addButton.addClickListener(e -> roomEditor.openAddDialog());

        leftGroup.add(refreshButton, addButton);

        // Groupe central avec la recherche
        HorizontalLayout centerGroup = new HorizontalLayout();
        centerGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        centerGroup.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        // Créer le champ de recherche
        createSearchField();
        centerGroup.add(searchField);

        // Groupe de boutons d'action sur sélection (droite)
        HorizontalLayout rightGroup = new HorizontalLayout();
        rightGroup.setSpacing(true);
        rightGroup.setAlignItems(FlexComponent.Alignment.CENTER);

        editButton = createStyledButton("✏️ Modifier", VaadinIcon.EDIT, "#f39c12", ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(e -> roomEditor.openEditDialog(selectedRoom));
        editButton.setEnabled(false);

        deleteButton = createStyledButton("🗑️ Supprimer", VaadinIcon.TRASH, "#e74c3c", ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> roomEditor.confirmDelete(selectedRoom));
        deleteButton.setEnabled(false);

        rightGroup.add(editButton, deleteButton);

        toolbar.add(leftGroup, centerGroup, rightGroup);
        toolbar.setFlexGrow(0, leftGroup);
        toolbar.setFlexGrow(1, centerGroup);
        toolbar.setFlexGrow(0, rightGroup);
        
        add(toolbar);
    }

    private Button createStyledButton(String text, VaadinIcon icon, String color, ButtonVariant variant) {
        Button button = new Button(text, new Icon(icon));
        button.addThemeVariants(variant);
        button.getStyle()
            .set("border-radius", "8px")
            .set("font-weight", "500")
            .set("padding", "10px 16px")
            .set("transition", "all 0.3s ease")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle().set("transform", "translateY(-2px)");
        });
        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle().set("transform", "translateY(0)");
        });

        return button;
    }

    private void createGrid() {
        // Configuration du grid avec style moderne
        grid.removeAllColumns();
        
        // Colonnes personnalisées avec tailles ultra-compactes
        grid.addColumn(Room::getId)
            .setHeader("ID")
            .setWidth("50px")
            .setFlexGrow(0)
            .setResizable(true);
            
        grid.addColumn(Room::getName)
            .setHeader("Nom")
            .setWidth("80px")
            .setFlexGrow(1)
            .setResizable(true);

        grid.addColumn(room -> room.getBuilding() != null ? room.getBuilding().getName() : "Aucun")
            .setHeader("Bâtiment")
            .setWidth("85px")
            .setFlexGrow(1)
            .setResizable(true);

        // Colonne pour le type de salle optimisée
        grid.addColumn(room -> {
            if (room.getFkRoomTypeId() > 0) {
                try {
                    RoomType roomType = roomTypeManager.getRoomTypeById(room.getFkRoomTypeId());
                    return roomType != null ? roomType.getName() : "Inconnu";
                } catch (Exception e) {
                    return "Erreur";
                }
            }
            return "Aucun";
        })
            .setHeader("Type")
            .setWidth("75px")
            .setFlexGrow(1)
            .setResizable(true);

        grid.addColumn(room -> String.format("%.1f×%.1f×%.1f", 
            room.getWidth(), room.getLength(), room.getHeight()))
            .setHeader("Dim.")
            .setWidth("85px")
            .setFlexGrow(0)
            .setResizable(true);

        grid.addColumn(room -> String.format("%.0f", 
            room.getWidth() * room.getLength() * room.getHeight()))
            .setHeader("Vol")
            .setWidth("50px")
            .setFlexGrow(0)
            .setResizable(true);

        // Configuration responsive du grid
        grid.setSizeFull();
        grid.setMinHeight("300px");
        grid.setMaxHeight("600px");
        
        // Style du grid avec optimisations responsive
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addThemeVariants(
            com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT,
            com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES,
            com.vaadin.flow.component.grid.GridVariant.LUMO_WRAP_CELL_CONTENT
        );
        
        grid.getStyle()
            .set("border-radius", "12px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Gestion de la sélection
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedRoom = event.getValue();
            updateButtonStates();
        });

        // Style des lignes avec police optimisée
        grid.setClassNameGenerator(room -> "custom-grid-row");
        
        grid.getElement().executeJs(
            "this.shadowRoot.querySelector('style').textContent += " +
            "'.custom-grid-row { transition: background-color 0.2s ease; }' + " +
            "'.custom-grid-row:hover { background-color: #f8f9fa !important; }' + " +
            "'vaadin-grid { font-size: 12px; }' + " +
            "'vaadin-grid-cell-content { padding: 6px 4px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }' + " +
            "'vaadin-grid thead th { font-size: 11px; font-weight: 600; }'"
        );
    }

    private void createLayout() {
        // Container principal pour le contenu avec scroll si nécessaire
        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        contentContainer.getStyle()
            .set("background", "white")
            .set("border-radius", "12px")
            .set("padding", "15px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("overflow", "auto");

        contentContainer.add(grid);
        add(contentContainer);

        // Informations sur la sélection avec plus de détails
        createSelectionInfo();
    }

    private void createSelectionInfo() {
        Div infoContainer = new Div();
        infoContainer.getStyle()
            .set("background", "rgba(255,255,255,0.1)")
            .set("color", "white")
            .set("padding", "12px")
            .set("border-radius", "12px")
            .set("margin-top", "15px")
            .set("text-align", "center")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("backdrop-filter", "blur(10px)")
            .set("border", "1px solid rgba(255,255,255,0.2)")
            .set("font-size", "14px");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Room room = event.getValue();
                String buildingName = room.getBuilding() != null ? room.getBuilding().getName() : "Aucun bâtiment";
                
                // Récupérer le type de salle
                String roomTypeName = "Aucun type";
                if (room.getFkRoomTypeId() > 0) {
                    try {
                        RoomType roomType = roomTypeManager.getRoomTypeById(room.getFkRoomTypeId());
                        if (roomType != null) {
                            roomTypeName = roomType.getName();
                        }
                    } catch (Exception e) {
                        roomTypeName = "Type inconnu";
                    }
                }
                
                String info = String.format("🏠 %s | 🏢 %s | 📋 %s | 📐 %.1f×%.1f×%.1f m | 📦 %.1f m³", 
                    room.getName(), buildingName, roomTypeName,
                    room.getWidth(), room.getLength(), room.getHeight(),
                    room.getWidth() * room.getLength() * room.getHeight());
                infoContainer.setText(info);
                infoContainer.setVisible(true);
            } else {
                infoContainer.setVisible(false);
            }
        });

        infoContainer.setVisible(false);
        add(infoContainer);
    }

    private void filterRooms(String searchTerm) {
        if (allRooms == null) {
            return;
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            grid.setItems(allRooms);
            updateResultsInfo(allRooms.size(), allRooms.size());
            return;
        }

        String lowerCaseFilter = searchTerm.toLowerCase().trim();
        List<Room> filteredRooms = allRooms.stream()
            .filter(room -> matchesSearch(room, lowerCaseFilter))
            .collect(Collectors.toList());

        grid.setItems(filteredRooms);
        updateResultsInfo(filteredRooms.size(), allRooms.size());
        
        // Clear selection après filtrage
        grid.asSingleSelect().clear();
    }

    private boolean matchesSearch(Room room, String searchTerm) {
        // Recherche dans le nom
        if (room.getName() != null && 
            room.getName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans le nom du bâtiment
        if (room.getBuilding() != null && room.getBuilding().getName() != null &&
            room.getBuilding().getName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans le type de salle
        if (room.getFkRoomTypeId() > 0) {
            try {
                RoomType roomType = roomTypeManager.getRoomTypeById(room.getFkRoomTypeId());
                if (roomType != null && roomType.getName() != null &&
                    roomType.getName().toLowerCase().contains(searchTerm)) {
                    return true;
                }
            } catch (Exception e) {
                // Ignorer les erreurs de recherche
            }
        }

        // Recherche dans les dimensions
        String dimensions = String.format("%.1f %.1f %.1f", 
            room.getWidth(), room.getLength(), room.getHeight());
        if (dimensions.contains(searchTerm)) {
            return true;
        }

        // Recherche dans l'ID (converti en string)
        if (String.valueOf(room.getId()).contains(searchTerm)) {
            return true;
        }

        return false;
    }

    private void updateResultsInfo(int filteredCount, int totalCount) {
        if (filteredCount != totalCount) {
            String message = String.format("🔍 %d résultat(s) sur %d salle(s)", 
                filteredCount, totalCount);
            Notification notification = Notification.show(message, 2000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedRoom != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    private void loadData() {
        try {
            allRooms = roomManager.getAllRooms(); // Stocker la liste complète
            grid.setItems(allRooms);
            
            // Notification de succès
            Notification notification = Notification.show(
                "✅ " + allRooms.size() + " salle(s) chargée(s)", 
                3000, 
                Notification.Position.TOP_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            // Clear la sélection et la recherche
            grid.asSingleSelect().clear();
            if (searchField != null) {
                searchField.clear();
            }
            
        } catch (Exception e) {
            showErrorNotification("Erreur lors du chargement", e.getMessage());
        }
    }

    private void showErrorNotification(String title, String message) {
        Notification notification = Notification.show("❌ " + title + ": " + message, 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
