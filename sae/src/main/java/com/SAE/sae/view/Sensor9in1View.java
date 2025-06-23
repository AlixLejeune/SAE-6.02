package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.service.RoomObjects.Sensor9in1Manager;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.SAE.sae.view.editor.Sensor9in1Editor;

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

@Route(value = "sensor9in1", layout = MainLayout.class)
public class Sensor9in1View extends VerticalLayout {

    private final Sensor9in1Manager sensor9in1Manager;
    private final Sensor9in1Editor sensor9in1Editor;
    private final Grid<Sensor9in1> grid = new Grid<>(Sensor9in1.class);
    private Sensor9in1 selectedSensor9in1 = null;
    private List<Sensor9in1> allSensor9in1s; // Liste complète pour la recherche
    private TextField searchField; // Champ de recherche

    // Boutons d'action
    private Button refreshButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    @Autowired
    public Sensor9in1View(Sensor9in1Manager sensor9in1Manager, RoomManager roomManager) {
        this.sensor9in1Manager = sensor9in1Manager;
        this.sensor9in1Editor = new Sensor9in1Editor(sensor9in1Manager, roomManager);
        
        // Configuration du callback pour rafraîchir les données
        this.sensor9in1Editor.setOnDataChanged(this::loadData);
        
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
        H2 title = new H2("🔬 Gestion des Capteurs 9-en-1");
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
        searchField.setPlaceholder("🔍 Rechercher un capteur 9-en-1...");
        searchField.setWidth("300px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(300);
        
        searchField.getStyle()
            .set("border-radius", "8px")
            .set("font-size", "14px");

        searchField.addValueChangeListener(e -> filterSensor9in1s(e.getValue()));
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
        addButton.addClickListener(e -> sensor9in1Editor.openAddDialog());

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
        editButton.addClickListener(e -> sensor9in1Editor.openEditDialog(selectedSensor9in1));
        editButton.setEnabled(false);

        deleteButton = createStyledButton("🗑️ Supprimer", VaadinIcon.TRASH, "#e74c3c", ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> sensor9in1Editor.confirmDelete(selectedSensor9in1));
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
        grid.removeAllColumns();
        
        // Colonnes personnalisées
        grid.addColumn(Sensor9in1::getId)
            .setHeader("ID")
            .setWidth("80px")
            .setFlexGrow(0);
            
        grid.addColumn(Sensor9in1::getCustomName)
            .setHeader("Nom")
            .setFlexGrow(1);

        grid.addColumn(sensor9in1 -> sensor9in1.getRoom() != null ? sensor9in1.getRoom().getName() : "Aucune")
            .setHeader("Salle")
            .setFlexGrow(1);

        grid.addColumn(sensor9in1 -> String.format("%.1f, %.1f, %.1f", 
            sensor9in1.getPosX(), sensor9in1.getPosY(), sensor9in1.getPosZ()))
            .setHeader("Position (X, Y, Z)")
            .setFlexGrow(1);

        // Style du grid
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.getStyle()
            .set("border-radius", "12px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Gestion de la sélection
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedSensor9in1 = event.getValue();
            updateButtonStates();
        });

        // Style des lignes
        grid.setClassNameGenerator(sensor9in1 -> "custom-grid-row");
        
        grid.getElement().executeJs(
            "this.shadowRoot.querySelector('style').textContent += " +
            "'.custom-grid-row { transition: background-color 0.2s ease; }' + " +
            "'.custom-grid-row:hover { background-color: #f8f9fa !important; }'"
        );
    }

    private void createLayout() {
        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        contentContainer.getStyle()
            .set("background", "white")
            .set("border-radius", "12px")
            .set("padding", "20px")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("overflow", "hidden");

        contentContainer.add(grid);
        add(contentContainer);

        createSelectionInfo();
    }

    private void createSelectionInfo() {
        Div infoContainer = new Div();
        infoContainer.getStyle()
            .set("background", "rgba(255,255,255,0.1)")
            .set("color", "white")
            .set("padding", "15px")
            .set("border-radius", "12px")
            .set("margin-top", "20px")
            .set("text-align", "center")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
            .set("backdrop-filter", "blur(10px)")
            .set("border", "1px solid rgba(255,255,255,0.2)");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                infoContainer.setText("🔬 Capteur 9-en-1 sélectionné : " + event.getValue().getCustomName());
                infoContainer.setVisible(true);
            } else {
                infoContainer.setVisible(false);
            }
        });

        infoContainer.setVisible(false);
        add(infoContainer);
    }

    private void filterSensor9in1s(String searchTerm) {
        if (allSensor9in1s == null) {
            return;
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            grid.setItems(allSensor9in1s);
            updateResultsInfo(allSensor9in1s.size(), allSensor9in1s.size());
            return;
        }

        String lowerCaseFilter = searchTerm.toLowerCase().trim();
        List<Sensor9in1> filteredSensor9in1s = allSensor9in1s.stream()
            .filter(sensor9in1 -> matchesSearch(sensor9in1, lowerCaseFilter))
            .collect(Collectors.toList());

        grid.setItems(filteredSensor9in1s);
        updateResultsInfo(filteredSensor9in1s.size(), allSensor9in1s.size());
        
        // Clear selection après filtrage
        grid.asSingleSelect().clear();
    }

    private boolean matchesSearch(Sensor9in1 sensor9in1, String searchTerm) {
        // Recherche dans le nom
        if (sensor9in1.getCustomName() != null && 
            sensor9in1.getCustomName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans le nom de la salle
        if (sensor9in1.getRoom() != null && sensor9in1.getRoom().getName() != null &&
            sensor9in1.getRoom().getName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans les coordonnées (position)
        String position = String.format("%.1f %.1f %.1f", 
            sensor9in1.getPosX(), sensor9in1.getPosY(), sensor9in1.getPosZ());
        if (position.contains(searchTerm)) {
            return true;
        }

        // Recherche dans l'ID (converti en string)
        if (String.valueOf(sensor9in1.getId()).contains(searchTerm)) {
            return true;
        }

        return false;
    }

    private void updateResultsInfo(int filteredCount, int totalCount) {
        if (filteredCount != totalCount) {
            String message = String.format("🔍 %d résultat(s) sur %d capteur(s) 9-en-1", 
                filteredCount, totalCount);
            Notification notification = Notification.show(message, 2000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedSensor9in1 != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    private void loadData() {
        try {
            allSensor9in1s = sensor9in1Manager.findAll(); // Stocker la liste complète
            grid.setItems(allSensor9in1s);
            
            Notification notification = Notification.show(
                "✅ " + allSensor9in1s.size() + " capteur(s) 9-en-1 chargé(s)", 
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
