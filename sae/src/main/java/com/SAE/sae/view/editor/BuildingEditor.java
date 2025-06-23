package com.SAE.sae.view.editor;

import com.SAE.sae.entity.Building;
import com.SAE.sae.service.BuildingManager;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;


public class BuildingEditor {
    
    private final BuildingManager buildingManager;
    private Runnable onDataChanged;

    public BuildingEditor(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createBuildingDialog("Nouveau Bâtiment", null);
        dialog.open();
    }

    public void openEditDialog(Building building) {
        if (building != null) {
            Dialog dialog = createBuildingDialog("Modifier le Bâtiment", building);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un bâtiment à modifier");
        }
    }

    public void confirmDelete(Building building) {
        if (building != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le bâtiment \"" + 
                                 building.getName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    buildingManager.deleteBuildingById(building.getId());
                    showSuccessNotification("Bâtiment supprimé avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un bâtiment à supprimer");
        }
    }

    private Dialog createBuildingDialog(String title, Building building) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champ de saisie
        TextField nameField = new TextField("Nom du bâtiment");
        nameField.setPlaceholder("Entrez le nom du bâtiment...");
        nameField.setWidthFull();
        nameField.setRequired(true);

        if (building != null) {
            nameField.setValue(building.getName() != null ? building.getName() : "");
        }

        VerticalLayout content = new VerticalLayout(nameField);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(building == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(building == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

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
                    
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
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

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        dialog.getFooter().add(buttonLayout);

        dialog.addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                nameField.focus();
            }
        });

        return dialog;
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
