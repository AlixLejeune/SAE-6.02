package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.SensorCO2Manager;
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


public class SensorCO2Editor {
    
    private final SensorCO2Manager sensorCO2Manager;
    private final RoomManager roomManager;
    private Runnable onDataChanged;

    public SensorCO2Editor(SensorCO2Manager sensorCO2Manager, RoomManager roomManager) {
        this.sensorCO2Manager = sensorCO2Manager;
        this.roomManager = roomManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createSensorCO2Dialog("Nouveau Capteur CO2", null);
        dialog.open();
    }

    public void openEditDialog(SensorCO2 sensorCO2) {
        if (sensorCO2 != null) {
            Dialog dialog = createSensorCO2Dialog("Modifier le Capteur CO2", sensorCO2);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un capteur CO2 à modifier");
        }
    }

    public void confirmDelete(SensorCO2 sensorCO2) {
        if (sensorCO2 != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le capteur CO2 \"" + 
                                 sensorCO2.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    sensorCO2Manager.deleteById(sensorCO2.getId());
                    showSuccessNotification("Capteur CO2 supprimé avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un capteur CO2 à supprimer");
        }
    }

    private Dialog createSensorCO2Dialog(String title, SensorCO2 sensorCO2) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom du capteur CO2");
        nameField.setPlaceholder("Entrez le nom du capteur CO2...");
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

        if (sensorCO2 != null) {
            nameField.setValue(sensorCO2.getCustomName() != null ? sensorCO2.getCustomName() : "");
            roomComboBox.setValue(sensorCO2.getRoom());
            posXField.setValue(sensorCO2.getPosX());
            posYField.setValue(sensorCO2.getPosY());
            posZField.setValue(sensorCO2.getPosZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(sensorCO2 == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(sensorCO2 == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField)) {
                try {
                    if (sensorCO2 == null) {
                        SensorCO2 newSensorCO2 = new SensorCO2();
                        setSensorCO2Fields(newSensorCO2, nameField, roomComboBox, posXField, posYField, posZField);
                        sensorCO2Manager.save(newSensorCO2);
                        showSuccessNotification("Capteur CO2 créé avec succès !");
                    } else {
                        setSensorCO2Fields(sensorCO2, nameField, roomComboBox, posXField, posYField, posZField);
                        sensorCO2Manager.save(sensorCO2);
                        showSuccessNotification("Capteur CO2 modifié avec succès !");
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

    private void setSensorCO2Fields(SensorCO2 sensorCO2, TextField nameField, ComboBox<Room> roomComboBox,
                                   NumberField posXField, NumberField posYField, NumberField posZField) {
        sensorCO2.setCustomName(nameField.getValue().trim());
        sensorCO2.setRoom(roomComboBox.getValue());
        sensorCO2.setPosX(posXField.getValue());
        sensorCO2.setPosY(posYField.getValue());
        sensorCO2.setPosZ(posZField.getValue());
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
