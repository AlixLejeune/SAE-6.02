package com.SAE.sae.view;

import com.SAE.sae.entity.Building;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.view.layouts.MainLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Route(value = "buildings", layout = MainLayout.class)
public class BuildingView extends VerticalLayout {

    private final BuildingManager buildingManager;
    private final Grid<Building> grid = new Grid<>(Building.class);
    private Building selectedBuilding = null;

    // Boutons d'action
    private Button refreshButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    @Autowired
    public BuildingView(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
        
        // Configuration générale de la vue
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)");

        createHeader();
        createToolbar();
        createGrid();
        createLayout();
        
        // Chargement initial
        loadData();
    }

    private void createHeader() {
        H2 title = new H2("🏢 Gestion des Bâtiments");
        title.getStyle()
            .set("color", "#2c3e50")
            .set("margin", "0 0 20px 0")
            .set("text-align", "center")
            .set("font-weight", "300")
            .set("text-shadow", "1px 1px 2px rgba(0,0,0,0.1)");
        
        add(title);
    }

    private void createToolbar() {
        // Container pour la barre d'outils
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
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setSpacing(true);

        // Bouton Actualiser
        refreshButton = createStyledButton("🔄 Actualiser", VaadinIcon.REFRESH, "#3498db", ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(e -> loadData());

        // Bouton Ajouter
        addButton = createStyledButton("➕ Ajouter", VaadinIcon.PLUS, "#27ae60", ButtonVariant.LUMO_SUCCESS);
        addButton.addClickListener(e -> openAddDialog());

        actionButtons.add(refreshButton, addButton);

        // Groupe de boutons d'action sur sélection (droite)
        HorizontalLayout selectionButtons = new HorizontalLayout();
        selectionButtons.setSpacing(true);

        // Bouton Modifier
        editButton = createStyledButton("✏️ Modifier", VaadinIcon.EDIT, "#f39c12", ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(e -> openEditDialog());
        editButton.setEnabled(false);

        // Bouton Supprimer
        deleteButton = createStyledButton("🗑️ Supprimer", VaadinIcon.TRASH, "#e74c3c", ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> confirmDelete());
        deleteButton.setEnabled(false);

        selectionButtons.add(editButton, deleteButton);

        toolbar.add(actionButtons, selectionButtons);
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

        // Effet hover personnalisé
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
        
        // Colonnes personnalisées
        grid.addColumn(Building::getId)
            .setHeader("ID")
            .setWidth("80px")
            .setFlexGrow(0);
            
        grid.addColumn(Building::getName)
            .setHeader("Nom du Bâtiment")
            .setFlexGrow(1);

        // Style du grid
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.getStyle()
            .set("border-radius", "12px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        // Gestion de la sélection
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedBuilding = event.getValue();
            updateButtonStates();
        });

        // Style des lignes
        grid.setClassNameGenerator(building -> "custom-grid-row");
        
        // CSS personnalisé pour le grid
        grid.getElement().executeJs(
            "this.shadowRoot.querySelector('style').textContent += " +
            "'.custom-grid-row { transition: background-color 0.2s ease; }' + " +
            "'.custom-grid-row:hover { background-color: #f8f9fa !important; }'"
        );
    }

    private void createLayout() {
        // Container principal pour le contenu
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

        // Informations sur la sélection
        createSelectionInfo();
    }

    private void createSelectionInfo() {
        Div infoContainer = new Div();
        infoContainer.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("color", "white")
            .set("padding", "15px")
            .set("border-radius", "12px")
            .set("margin-top", "20px")
            .set("text-align", "center")
            .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                infoContainer.setText("🏢 Bâtiment sélectionné : " + event.getValue().getName());
                infoContainer.setVisible(true);
            } else {
                infoContainer.setVisible(false);
            }
        });

        infoContainer.setVisible(false);
        add(infoContainer);
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedBuilding != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    private void loadData() {
        try {
            List<Building> buildings = buildingManager.getAllBuildings();
            grid.setItems(buildings);
            
            // Notification de succès
            Notification notification = Notification.show(
                "✅ " + buildings.size() + " bâtiment(s) chargé(s)", 
                3000, 
                Notification.Position.TOP_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            // Reset de la sélection
            grid.asSingleSelect().clear();
            
        } catch (Exception e) {
            showErrorNotification("Erreur lors du chargement", e.getMessage());
        }
    }

    private void openAddDialog() {
        Dialog dialog = createBuildingDialog("Nouveau Bâtiment", null);
        dialog.open();
    }

    private void openEditDialog() {
        if (selectedBuilding != null) {
            Dialog dialog = createBuildingDialog("Modifier le Bâtiment", selectedBuilding);
            dialog.open();
        }
    }

    private Dialog createBuildingDialog(String title, Building building) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        // Style du dialog
        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champ de saisie
        TextField nameField = new TextField("Nom du bâtiment");
        nameField.setPlaceholder("Entrez le nom du bâtiment...");
        nameField.setWidthFull();
        nameField.getStyle()
            .set("margin-bottom", "20px");

        if (building != null) {
            nameField.setValue(building.getName());
        }

        // Layout du contenu
        VerticalLayout content = new VerticalLayout(nameField);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(building == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(building == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("margin-right", "10px");

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Actions des boutons
        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            if (name != null && !name.trim().isEmpty()) {
                try {
                    if (building == null) {
                        // Nouveau bâtiment
                        Building newBuilding = new Building();
                        newBuilding.setName(name.trim());
                        buildingManager.saveBuilding(newBuilding);
                        showSuccessNotification("Bâtiment créé avec succès !");
                    } else {
                        // Modification
                        building.setName(name.trim());
                        buildingManager.updateBuilding(building);
                        showSuccessNotification("Bâtiment modifié avec succès !");
                    }
                    
                    loadData();
                    dialog.close();
                    
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la sauvegarde", ex.getMessage());
                }
            } else {
                showWarningNotification("Le nom ne peut pas être vide");
                nameField.focus();
            }
        });

        cancelButton.addClickListener(e -> dialog.close());

        // Layout des boutons
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        dialog.getFooter().add(buttonLayout);

        // Focus automatique
        dialog.addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                nameField.focus();
            }
        });

        return dialog;
    }

    private void confirmDelete() {
        if (selectedBuilding != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le bâtiment \"" + 
                                 selectedBuilding.getName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    buildingManager.deleteBuildingById(selectedBuilding.getId());
                    showSuccessNotification("Bâtiment supprimé avec succès !");
                    loadData();
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
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