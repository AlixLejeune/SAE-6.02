package com.SAE.sae.view.editor;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.RoomType;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.service.RoomTypeManager;

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


public class RoomEditor {
    
    private final RoomManager roomManager;
    private final BuildingManager buildingManager;
    private final RoomTypeManager roomTypeManager;
    private Runnable onDataChanged;

    public RoomEditor(RoomManager roomManager, BuildingManager buildingManager, RoomTypeManager roomTypeManager) {
        this.roomManager = roomManager;
        this.buildingManager = buildingManager;
        this.roomTypeManager = roomTypeManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createRoomDialog("Nouvelle Salle", null);
        dialog.open();
    }

    public void openEditDialog(Room room) {
        if (room != null) {
            Dialog dialog = createRoomDialog("Modifier la Salle", room);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une salle à modifier");
        }
    }

    public void confirmDelete(Room room) {
        if (room != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer la salle \"" + 
                                 room.getName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    roomManager.deleteRoomById(room.getId());
                    showSuccessNotification("Salle supprimée avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner une salle à supprimer");
        }
    }

    private Dialog createRoomDialog(String title, Room room) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champs de saisie
        TextField nameField = new TextField("Nom de la salle");
        nameField.setPlaceholder("Entrez le nom de la salle...");
        nameField.setWidthFull();

        // ComboBox pour le bâtiment
        ComboBox<Building> buildingComboBox = new ComboBox<>("Bâtiment");
        buildingComboBox.setPlaceholder("Sélectionnez un bâtiment...");
        buildingComboBox.setWidthFull();
        buildingComboBox.setItemLabelGenerator(Building::getName);
        buildingComboBox.setItems(buildingManager.getAllBuildings());

        // ComboBox pour le type de salle
        ComboBox<RoomType> roomTypeComboBox = new ComboBox<>("Type de salle");
        roomTypeComboBox.setPlaceholder("Sélectionnez un type de salle...");
        roomTypeComboBox.setWidthFull();
        roomTypeComboBox.setItemLabelGenerator(RoomType::getName);
        roomTypeComboBox.setItems(roomTypeManager.getAllRoomTypes());

        // Dimensions
        HorizontalLayout dimensionsLayout = new HorizontalLayout();
        NumberField widthField = new NumberField("Largeur");
        NumberField lengthField = new NumberField("Longueur");
        NumberField heightField = new NumberField("Hauteur");
        widthField.setStep(0.1);
        lengthField.setStep(0.1);
        heightField.setStep(0.1);
        widthField.setSuffixComponent(new com.vaadin.flow.component.html.Span("m"));
        lengthField.setSuffixComponent(new com.vaadin.flow.component.html.Span("m"));
        heightField.setSuffixComponent(new com.vaadin.flow.component.html.Span("m"));
        dimensionsLayout.add(widthField, lengthField, heightField);

        if (room != null) {
            nameField.setValue(room.getName() != null ? room.getName() : "");
            buildingComboBox.setValue(room.getBuilding());
            
            // Trouver le RoomType correspondant à l'ID
            if (room.getFkRoomTypeId() > 0) {
                RoomType roomType = roomTypeManager.getRoomTypeById(room.getFkRoomTypeId());
                if (roomType != null) {
                    roomTypeComboBox.setValue(roomType);
                }
            }
            
            widthField.setValue(room.getWidth());
            lengthField.setValue(room.getLength());
            heightField.setValue(room.getHeight());
        }

        VerticalLayout content = new VerticalLayout(nameField, buildingComboBox, roomTypeComboBox, dimensionsLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(room == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(room == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            if (validateFields(nameField, buildingComboBox, roomTypeComboBox, widthField, lengthField, heightField)) {
                try {
                    if (room == null) {
                        Room newRoom = new Room();
                        setRoomFields(newRoom, nameField, buildingComboBox, roomTypeComboBox, widthField, lengthField, heightField);
                        roomManager.saveRoom(newRoom);
                        showSuccessNotification("Salle créée avec succès !");
                    } else {
                        setRoomFields(room, nameField, buildingComboBox, roomTypeComboBox, widthField, lengthField, heightField);
                        roomManager.updateRoom(room);
                        showSuccessNotification("Salle modifiée avec succès !");
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

    private boolean validateFields(TextField nameField, ComboBox<Building> buildingComboBox, 
                                 ComboBox<RoomType> roomTypeComboBox, NumberField... numberFields) {
        if (nameField.getValue() == null || nameField.getValue().trim().isEmpty()) {
            showWarningNotification("Le nom ne peut pas être vide");
            nameField.focus();
            return false;
        }

        if (buildingComboBox.getValue() == null) {
            showWarningNotification("Veuillez sélectionner un bâtiment");
            buildingComboBox.focus();
            return false;
        }

        if (roomTypeComboBox.getValue() == null) {
            showWarningNotification("Veuillez sélectionner un type de salle");
            roomTypeComboBox.focus();
            return false;
        }

        for (NumberField field : numberFields) {
            if (field.getValue() == null || field.getValue() <= 0) {
                showWarningNotification("Toutes les dimensions doivent être positives");
                field.focus();
                return false;
            }
        }
        return true;
    }

    private void setRoomFields(Room room, TextField nameField, ComboBox<Building> buildingComboBox,
                              ComboBox<RoomType> roomTypeComboBox, NumberField widthField, 
                              NumberField lengthField, NumberField heightField) {
        room.setName(nameField.getValue().trim());
        room.setBuilding(buildingComboBox.getValue());
        room.setFkRoomTypeId(roomTypeComboBox.getValue().getId());
        room.setWidth(widthField.getValue());
        room.setLength(lengthField.getValue());
        room.setHeight(heightField.getValue());
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
