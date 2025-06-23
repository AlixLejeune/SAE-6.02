package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.DataTableManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.combobox.ComboBox;
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


public class DataTableEditor {
    
    private final DataTableManager dataTableManager;
    private final RoomManager roomManager;
    private Runnable onDataChanged;

    public DataTableEditor(DataTableManager dataTableManager, RoomManager roomManager) {
        this.dataTableManager = dataTableManager;
        this.roomManager = roomManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createDataTableDialog("Nouvelle Table", null);
        dialog.open();
    }

    public void openEditDialog(DataTable dataTable) {
        if (dataTable != null) {
            Dialog dialog = createDataTableDialog("Modifier la Table", dataTable);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une table à modifier");
        }
    }

    public void confirmDelete(DataTable dataTable) {
        if (dataTable != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer la table \"" + 
                                 dataTable.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    dataTableManager.deleteById(dataTable.getId());
                    showSuccessNotification("Table supprimée avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une table à supprimer");
        }
    }

    private Dialog createDataTableDialog(String title, DataTable dataTable) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom de la table");
        nameField.setPlaceholder("Entrez le nom de la table...");
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

        if (dataTable != null) {
            nameField.setValue(dataTable.getCustomName() != null ? dataTable.getCustomName() : "");
            roomComboBox.setValue(dataTable.getRoom());
            posXField.setValue(dataTable.getPosX());
            posYField.setValue(dataTable.getPosY());
            posZField.setValue(dataTable.getPosZ());
            sizeXField.setValue(dataTable.getSizeX());
            sizeYField.setValue(dataTable.getSizeY());
            sizeZField.setValue(dataTable.getSizeZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout, sizeLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(dataTable == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(dataTable == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField)) {
                try {
                    if (dataTable == null) {
                        DataTable newDataTable = new DataTable();
                        setDataTableFields(newDataTable, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        dataTableManager.save(newDataTable);
                        showSuccessNotification("Table créée avec succès !");
                    } else {
                        setDataTableFields(dataTable, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        dataTableManager.save(dataTable);
                        showSuccessNotification("Table modifiée avec succès !");
                    }
                    
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
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

    private void setDataTableFields(DataTable dataTable, TextField nameField, ComboBox<Room> roomComboBox,
                                   NumberField posXField, NumberField posYField, NumberField posZField,
                                   NumberField sizeXField, NumberField sizeYField, NumberField sizeZField) {
        dataTable.setCustomName(nameField.getValue().trim());
        dataTable.setRoom(roomComboBox.getValue());
        dataTable.setPosX(posXField.getValue());
        dataTable.setPosY(posYField.getValue());
        dataTable.setPosZ(posZField.getValue());
        dataTable.setSizeX(sizeXField.getValue());
        dataTable.setSizeY(sizeYField.getValue());
        dataTable.setSizeZ(sizeZField.getValue());
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
