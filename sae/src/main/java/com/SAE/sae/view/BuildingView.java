package com.SAE.sae.view;

import com.SAE.sae.entity.Building;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.view.layouts.MainLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "buildings", layout = MainLayout.class)
public class BuildingView extends VerticalLayout {

    private final BuildingManager buildingManager;
    private final Grid<Building> grid = new Grid<>(Building.class);

    @Autowired
    public BuildingView(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;

        // Titre
        add("📋 Liste des Buildings");

        // Boutons
        Button loadButton = new Button("🔄 Charger les Buildings", e -> loadData());
        Button addNewBuildings = new Button("➕ Ajouter un nouveau Building", e -> newBuilding());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(loadButton, addNewBuildings);
        
        // Configuration du grid
        grid.setColumns("id", "name");
        grid.setWidthFull();

        // Ajout des composants
        add(buttonLayout, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<Building> buildings = buildingManager.getAllBuildings();
            grid.setItems(buildings);
            Notification.show("✅ " + buildings.size() + " Buildings chargés");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void newBuilding() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nouveau Building");
        
        // Champ de saisie
        TextField nameField = new TextField();
        nameField.setLabel("Nom du building");
        nameField.setPlaceholder("Entrez le nom...");
        nameField.setWidthFull();
        
        // Layout pour le contenu
        VerticalLayout dialogContent = new VerticalLayout(nameField);
        dialogContent.setPadding(false);
        dialog.add(dialogContent);

        // Boutons
        Button confirmButton = new Button("✅ Créer", e -> {
            String name = nameField.getValue();
            if (name != null && !name.trim().isEmpty()) {
                try {
                    Building building = new Building();
                    building.setName(name.trim());
                    buildingManager.saveBuilding(building);
                    
                    Notification.show("✅ Building créé avec succès !");
                    loadData(); // Recharger la grille
                    dialog.close();
                } catch (Exception ex) {
                    Notification.show("❌ Erreur lors de la création : " + ex.getMessage(), 
                                    3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("⚠️ Le nom ne peut pas être vide", 
                                2000, Notification.Position.MIDDLE);
            }
        });
        
        Button cancelButton = new Button("❌ Annuler", e -> dialog.close());
        
        // Configuration des boutons
        confirmButton.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY);
        
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        dialog.getFooter().add(buttonLayout);
        
        dialog.open();
        nameField.focus(); // Focus automatique sur le champ
    }
}