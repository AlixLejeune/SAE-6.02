package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.Sensor9in1Manager;
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


public class Sensor9in1Editor {
    
    private final Sensor9in1Manager sensor9in1Manager;
    private final RoomManager roomManager;
    private Runnable onDataChanged;

    public Sensor9in1Editor(Sensor9in1Manager sensor9in1Manager, RoomManager roomManager) {
        this.sensor9in1Manager = sensor9in1Manager;
        this.roomManager = roomManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createSensor9in1Dialog("Nouveau Capteur 9-en-1", null);
        dialog.open();
    }

    public void openEditDialog(Sensor9in1 sensor9in1) {
        if (sensor9in1 != null) {
            Dialog dialog = createSensor9in1Dialog("Modifier le Capteur 9-en-1", sensor9in1);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un capteur 9-en-1 à modifier");
        }
    }

    public void confirmDelete(Sensor9in1 sensor9in1) {
        if (sensor9in1 != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le capteur 9-en-1 \"" + 
                                 sensor9in1.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    sensor9in1Manager.deleteById(sensor9in1.getId());
                    showSuccessNotification("Capteur 9-en-1 supprimé avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un capteur 9-en-1 à supprimer");
        }
    }

    private Dialog createSensor9in1Dialog(String title, Sensor9in1 sensor9in1) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom du capteur 9-en-1");
        nameField.setPlaceholder("Entrez le nom du capteur 9-en-1...");
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

        if (sensor9in1 != null) {
            nameField.setValue(sensor9in1.getCustomName() != null ? sensor9in1.getCustomName() : "");
            roomComboBox.setValue(sensor9in1.getRoom());
            posXField.setValue(sensor9in1.getPosX());
            posYField.setValue(sensor9in1.getPosY());
            posZField.setValue(sensor9in1.getPosZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(sensor9in1 == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(sensor9in1 == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField)) {
                try {
                    if (sensor9in1 == null) {
                        Sensor9in1 newSensor9in1 = new Sensor9in1();
                        setSensor9in1Fields(newSensor9in1, nameField, roomComboBox, posXField, posYField, posZField);
                        sensor9in1Manager.save(newSensor9in1);
                        showSuccessNotification("Capteur 9-en-1 créé avec succès !");
                    } else {
                        setSensor9in1Fields(sensor9in1, nameField, roomComboBox, posXField, posYField, posZField);
                        sensor9in1Manager.save(sensor9in1);
                        showSuccessNotification("Capteur 9-en-1 modifié avec succès !");
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

    private void setSensor9in1Fields(Sensor9in1 sensor9in1, TextField nameField, ComboBox<Room> roomComboBox,
                                    NumberField posXField, NumberField posYField, NumberField posZField) {
        sensor9in1.setCustomName(nameField.getValue().trim());
        sensor9in1.setRoom(roomComboBox.getValue());
        sensor9in1.setPosX(posXField.getValue());
        sensor9in1.setPosY(posYField.getValue());
        sensor9in1.setPosZ(posZField.getValue());
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
