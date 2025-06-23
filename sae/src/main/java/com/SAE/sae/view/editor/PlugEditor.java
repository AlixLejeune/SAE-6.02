package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomObjects.PlugManager;
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



public class PlugEditor {
    
    private final PlugManager plugManager;
    private final RoomManager roomManager;
    private Runnable onDataChanged;

    public PlugEditor(PlugManager plugManager, RoomManager roomManager) {
        this.plugManager = plugManager;
        this.roomManager = roomManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createPlugDialog("Nouvelle Prise", null);
        dialog.open();
    }

    public void openEditDialog(Plug plug) {
        if (plug != null) {
            Dialog dialog = createPlugDialog("Modifier la Prise", plug);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une prise à modifier");
        }
    }

    public void confirmDelete(Plug plug) {
        if (plug != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer la prise \"" + 
                                 plug.getCustomName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    plugManager.deleteById(plug.getId());
                    showSuccessNotification("Prise supprimée avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une prise à supprimer");
        }
    }

    private Dialog createPlugDialog(String title, Plug plug) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom de la prise");
        nameField.setPlaceholder("Entrez le nom de la prise...");
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

        if (plug != null) {
            nameField.setValue(plug.getCustomName() != null ? plug.getCustomName() : "");
            roomComboBox.setValue(plug.getRoom());
            posXField.setValue(plug.getPosX());
            posYField.setValue(plug.getPosY());
            posZField.setValue(plug.getPosZ());
        }

        VerticalLayout content = new VerticalLayout(nameField, roomComboBox, positionLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(plug == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(plug == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, roomComboBox, posXField, posYField, posZField)) {
                try {
                    if (plug == null) {
                        Plug newPlug = new Plug();
                        setPlugFields(newPlug, nameField, roomComboBox, posXField, posYField, posZField);
                        plugManager.save(newPlug);
                        showSuccessNotification("Prise créée avec succès !");
                    } else {
                        setPlugFields(plug, nameField, roomComboBox, posXField, posYField, posZField);
                        plugManager.save(plug);
                        showSuccessNotification("Prise modifiée avec succès !");
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

    private void setPlugFields(Plug plug, TextField nameField, ComboBox<Room> roomComboBox,
                              NumberField posXField, NumberField posYField, NumberField posZField) {
        plug.setCustomName(nameField.getValue().trim());
        plug.setRoom(roomComboBox.getValue());
        plug.setPosX(posXField.getValue());
        plug.setPosY(posYField.getValue());
        plug.setPosZ(posZField.getValue());
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
