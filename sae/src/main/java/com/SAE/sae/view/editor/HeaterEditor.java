package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.HeaterManager;
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


public class HeaterEditor {
    
    private final HeaterManager heaterManager;
    private final RoomManager roomManager;
    private Runnable onDataChanged;

    public HeaterEditor(HeaterManager heaterManager, RoomManager roomManager) {
        this.heaterManager = heaterManager;
        this.roomManager = roomManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createHeaterDialog("Nouveau Radiateur", null);
        dialog.open();
    }

    public void openEditDialog(Heater heater) {
        if (heater != null) {
            Dialog dialog = createHeaterDialog("Modifier le Radiateur", heater);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un radiateur à modifier");
        }
    }

    public void confirmDelete(Heater heater) {
        if (heater != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le radiateur \"" + 
                                 heater.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    heaterManager.deleteById(heater.getId());
                    showSuccessNotification("Radiateur supprimé avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un radiateur à supprimer");
        }
    }

    private Dialog createHeaterDialog(String title, Heater heater) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom du radiateur");
        nameField.setPlaceholder("Entrez le nom du radiateur...");
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

        if (heater != null) {
            nameField.setValue(heater.getCustomName() != null ? heater.getCustomName() : "");
            roomComboBox.setValue(heater.getRoom());
            posXField.setValue(heater.getPosX());
            posYField.setValue(heater.getPosY());
            posZField.setValue(heater.getPosZ());
            sizeXField.setValue(heater.getSizeX());
            sizeYField.setValue(heater.getSizeY());
            sizeZField.setValue(heater.getSizeZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout, sizeLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(heater == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(heater == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField)) {
                try {
                    if (heater == null) {
                        Heater newHeater = new Heater();
                        setHeaterFields(newHeater, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        heaterManager.save(newHeater);
                        showSuccessNotification("Radiateur créé avec succès !");
                    } else {
                        setHeaterFields(heater, nameField, roomComboBox, posXField, posYField, posZField, sizeXField, sizeYField, sizeZField);
                        heaterManager.save(heater);
                        showSuccessNotification("Radiateur modifié avec succès !");
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

    private void setHeaterFields(Heater heater, TextField nameField, ComboBox<Room> roomComboBox,
                                NumberField posXField, NumberField posYField, NumberField posZField,
                                NumberField sizeXField, NumberField sizeYField, NumberField sizeZField) {
        heater.setCustomName(nameField.getValue().trim());
        heater.setRoom(roomComboBox.getValue());
        heater.setPosX(posXField.getValue());
        heater.setPosY(posYField.getValue());
        heater.setPosZ(posZField.getValue());
        heater.setSizeX(sizeXField.getValue());
        heater.setSizeY(sizeYField.getValue());
        heater.setSizeZ(sizeZField.getValue());
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
