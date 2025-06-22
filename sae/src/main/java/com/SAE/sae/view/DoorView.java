package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.DoorManager;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.view.layouts.MainLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "doors", layout = MainLayout.class)
public class DoorView extends VerticalLayout {

    private final DoorManager doorManager;
    private final RoomManager roomManager;
    private final Grid<Door> grid = new Grid<>(Door.class);
    private Door selectedDoor = null;
    private List<Door> allDoors; // Liste complète pour la recherche
    private TextField searchField; // Champ de recherche

    // Boutons d'action
    private Button refreshButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    @Autowired
    public DoorView(DoorManager doorManager, RoomManager roomManager) {
        this.doorManager = doorManager;
        this.roomManager = roomManager;
        
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
        H2 title = new H2("🚪 Gestion des Portes");
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
        searchField.setPlaceholder("🔍 Rechercher une porte...");
        searchField.setWidth("300px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(300);
        
        searchField.getStyle()
            .set("border-radius", "8px")
            .set("font-size", "14px");

        searchField.addValueChangeListener(e -> filterDoors(e.getValue()));
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
        addButton.addClickListener(e -> openAddDialog());

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
        editButton.addClickListener(e -> openEditDialog());
        editButton.setEnabled(false);

        deleteButton = createStyledButton("🗑️ Supprimer", VaadinIcon.TRASH, "#e74c3c", ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> confirmDelete());
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
        grid.addColumn(Door::getId)
            .setHeader("ID")
            .setWidth("80px")
            .setFlexGrow(0);
            
        grid.addColumn(Door::getCustomName)
            .setHeader("Nom")
            .setFlexGrow(1);

        grid.addColumn(door -> door.getRoom() != null ? door.getRoom().getName() : "Aucune")
            .setHeader("Salle")
            .setFlexGrow(1);

        grid.addColumn(door -> String.format("%.1f, %.1f, %.1f", 
            door.getPosX(), door.getPosY(), door.getPosZ()))
            .setHeader("Position (X, Y, Z)")
            .setFlexGrow(1);

        grid.addColumn(door -> String.format("%.1f × %.1f × %.1f", 
            door.getSizeX(), door.getSizeY(), door.getSizeZ()))
            .setHeader("Taille (L × l × H)")
            .setFlexGrow(1);

        // Style du grid
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.getStyle()
            .set("border-radius", "12px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Gestion de la sélection
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedDoor = event.getValue();
            updateButtonStates();
        });

        // Style des lignes
        grid.setClassNameGenerator(door -> "custom-grid-row");
        
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
                infoContainer.setText("🚪 Porte sélectionnée : " + event.getValue().getCustomName());
                infoContainer.setVisible(true);
            } else {
                infoContainer.setVisible(false);
            }
        });

        infoContainer.setVisible(false);
        add(infoContainer);
    }

    private void filterDoors(String searchTerm) {
        if (allDoors == null) {
            return;
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            grid.setItems(allDoors);
            updateResultsInfo(allDoors.size(), allDoors.size());
            return;
        }

        String lowerCaseFilter = searchTerm.toLowerCase().trim();
        List<Door> filteredDoors = allDoors.stream()
            .filter(door -> matchesSearch(door, lowerCaseFilter))
            .collect(Collectors.toList());

        grid.setItems(filteredDoors);
        updateResultsInfo(filteredDoors.size(), allDoors.size());
        
        // Clear selection après filtrage
        grid.asSingleSelect().clear();
    }

    private boolean matchesSearch(Door door, String searchTerm) {
        // Recherche dans le nom
        if (door.getCustomName() != null && 
            door.getCustomName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans le nom de la salle
        if (door.getRoom() != null && door.getRoom().getName() != null &&
            door.getRoom().getName().toLowerCase().contains(searchTerm)) {
            return true;
        }

        // Recherche dans les coordonnées (position)
        String position = String.format("%.1f %.1f %.1f", 
            door.getPosX(), door.getPosY(), door.getPosZ());
        if (position.contains(searchTerm)) {
            return true;
        }

        // Recherche dans les dimensions (taille)
        String size = String.format("%.1f %.1f %.1f", 
            door.getSizeX(), door.getSizeY(), door.getSizeZ());
        if (size.contains(searchTerm)) {
            return true;
        }

        // Recherche dans l'ID (converti en string)
        if (String.valueOf(door.getId()).contains(searchTerm)) {
            return true;
        }

        return false;
    }

    private void updateResultsInfo(int filteredCount, int totalCount) {
        if (filteredCount != totalCount) {
            String message = String.format("🔍 %d résultat(s) sur %d porte(s)", 
                filteredCount, totalCount);
            Notification notification = Notification.show(message, 2000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
    }

    private void updateButtonStates() {
        // CORRECTION : Récupérer la sélection actuelle à chaque fois
        Door currentSelection = grid.asSingleSelect().getValue();
        boolean hasSelection = currentSelection != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        
        // Mettre à jour selectedDoor avec la sélection actuelle
        selectedDoor = currentSelection;
    }

    private void loadData() {
        try {
            allDoors = doorManager.findAll(); // Stocker la liste complète
            grid.setItems(allDoors);
            
            Notification notification = Notification.show(
                "✅ " + allDoors.size() + " porte(s) chargée(s)", 
                3000, 
                Notification.Position.TOP_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            // Clear la sélection et la recherche
            grid.asSingleSelect().clear();
            searchField.clear();
            
        } catch (Exception e) {
            showErrorNotification("Erreur lors du chargement", e.getMessage());
        }
    }

    private void openAddDialog() {
        Dialog dialog = createDoorDialog("Nouvelle Porte", null);
        dialog.open();
    }

    private void openEditDialog() {
        // CORRECTION : Récupérer la sélection actuelle au moment de l'action
        Door currentSelection = grid.asSingleSelect().getValue();
        if (currentSelection != null) {
            Dialog dialog = createDoorDialog("Modifier la Porte", currentSelection);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une porte à modifier");
        }
    }

    private Dialog createDoorDialog(String title, Door door) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom de la porte");
        nameField.setPlaceholder("Entrez le nom de la porte...");
        nameField.setWidthFull();

        // ComboBox pour la room
        ComboBox<Room> roomComboBox = new ComboBox<>("Salle");
        roomComboBox.setPlaceholder("Sélectionnez une salle...");
        roomComboBox.setWidthFull();
        roomComboBox.setItemLabelGenerator(Room::getName);
        roomComboBox.setItems(roomManager.getAllRooms());

        // Position
        HorizontalLayout positionLayout = new HorizontalLayout();
        NumberField posXField = new NumberField("Position X");
        NumberField posYField = new NumberField("Position Y");
        NumberField posZField = new NumberField("Position Z");
        posXField.setStep(0.1);
        posYField.setStep(0.1);
        posZField.setStep(0.1);
        positionLayout.add(posXField, posYField, posZField);

        // Taille
        HorizontalLayout sizeLayout = new HorizontalLayout();
        NumberField sizeXField = new NumberField("Largeur");
        NumberField sizeYField = new NumberField("Profondeur");
        NumberField sizeZField = new NumberField("Hauteur");
        sizeXField.setStep(0.1);
        sizeYField.setStep(0.1);
        sizeZField.setStep(0.1);
        sizeLayout.add(sizeXField, sizeYField, sizeZField);

        if (door != null) {
            nameField.setValue(door.getCustomName() != null ? door.getCustomName() : "");
            roomComboBox.setValue(door.getRoom());
            posXField.setValue(door.getPosX());
            posYField.setValue(door.getPosY());
            posZField.setValue(door.getPosZ());
            sizeXField.setValue(door.getSizeX());
            sizeYField.setValue(door.getSizeY());
            sizeZField.setValue(door.getSizeZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout, sizeLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(door == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(door == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            Room selectedRoom = roomComboBox.getValue();
            System.out.println("Room sélectionnée: " + selectedRoom.getName() + " (ID: " + selectedRoom.getId() + ")");
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField)) {
                try {
                    if (door == null) {
                        Door newDoor = new Door();
                        setDoorFields(newDoor, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        doorManager.save(newDoor);
                        showSuccessNotification("Porte créée avec succès !");
                    } else {
                        setDoorFields(door, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        doorManager.save(door);
                        showSuccessNotification("Porte modifiée avec succès !");
                    }
                    
                    loadData();
                    dialog.close();
                    
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la sauvegarde", ex.getMessage());
                }
            }
        });

        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        dialog.getFooter().add(buttonLayout);

        dialog.addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                nameField.focus();
            }
        });

        return dialog;
    }

    private boolean validateFields(TextField nameField, ComboBox<Room> roomComboBox, NumberField... numberFields) {
        if (nameField.getValue() == null || nameField.getValue().trim().isEmpty()) {
            showWarningNotification("Le nom ne peut pas être vide");
            nameField.focus();
            return false;
        }

        if (roomComboBox.getValue() == null) {
            showWarningNotification("Veuillez sélectionner une salle");
            roomComboBox.focus();
            return false;
        }

        for (NumberField field : numberFields) {
            if (field.getValue() == null) {
                showWarningNotification("Tous les champs numériques doivent être remplis");
                field.focus();
                return false;
            }
        }
        return true;
    }

    private void setDoorFields(Door door, TextField nameField, ComboBox<Room> roomComboBox,
                              NumberField posXField, NumberField posYField, NumberField posZField,
                              NumberField sizeXField, NumberField sizeYField, NumberField sizeZField) {
        door.setCustomName(nameField.getValue().trim());
        door.setRoom(roomComboBox.getValue());
        door.setPosX(posXField.getValue());
        door.setPosY(posYField.getValue());
        door.setPosZ(posZField.getValue());
        door.setSizeX(sizeXField.getValue());
        door.setSizeY(sizeYField.getValue());
        door.setSizeZ(sizeZField.getValue());
    }

    private void confirmDelete() {
        // CORRECTION : Récupérer la sélection actuelle au moment de l'action
        Door currentSelection = grid.asSingleSelect().getValue();
        if (currentSelection != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer la porte \"" + 
                                 currentSelection.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    doorManager.deleteById(currentSelection.getId());
                    showSuccessNotification("Porte supprimée avec succès !");
                    loadData();
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une porte à supprimer");
        }
    }

    // Méthodes utilitaires pour les notifications
    private void showSuccessNotification(String message) {
        Notification notification = Notification.show("✅ " + message, 3000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showWarningNotification(String message) {
        Notification notification = Notification.show("⚠️ " + message, 3000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    private void showErrorNotification(String title, String message) {
        Notification notification = Notification.show("❌ " + title + ": " + message, 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}