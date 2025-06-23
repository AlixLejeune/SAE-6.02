package com.SAE.sae.view.editor;

import com.SAE.sae.entity.RoomType;
import com.SAE.sae.service.RoomTypeManager;

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


public class RoomTypeEditor {
    
    private final RoomTypeManager roomTypeManager;
    private Runnable onDataChanged;

    public RoomTypeEditor(RoomTypeManager roomTypeManager) {
        this.roomTypeManager = roomTypeManager;
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void openAddDialog() {
        Dialog dialog = createRoomTypeDialog("Nouveau Type de Salle", null);
        dialog.open();
    }

    public void openEditDialog(RoomType roomType) {
        if (roomType != null) {
            Dialog dialog = createRoomTypeDialog("Modifier le Type de Salle", roomType);
            dialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un type de salle à modifier");
        }
    }

    public void confirmDelete(RoomType roomType) {
        if (roomType != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmer la suppression");
            confirmDialog.setText("Êtes-vous sûr de vouloir supprimer le type de salle \"" + 
                                 roomType.getName() + "\" ? Cette action est irréversible.");

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("❌ Annuler");
            confirmDialog.setConfirmText("🗑️ Supprimer");
            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(e -> {
                try {
                    roomTypeManager.deleteRoomTypeById(roomType.getId());
                    showSuccessNotification("Type de salle supprimé avec succès !");
                    if (onDataChanged != null) {
                        onDataChanged.run();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la suppression", ex.getMessage());
                }
            });

            confirmDialog.open();
        } else {
            showWarningNotification("Veuillez sélectionner un type de salle à supprimer");
        }
    }

    private Dialog createRoomTypeDialog(String title, RoomType roomType) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        dialog.getElement().getStyle()
            .set("border-radius", "12px")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.15)");

        // Champ de saisie
        TextField nameField = new TextField("Nom du type de salle");
        nameField.setPlaceholder("Entrez le nom du type de salle...");
        nameField.setWidthFull();
        nameField.setRequired(true);

        if (roomType != null) {
            nameField.setValue(roomType.getName() != null ? roomType.getName() : "");
        }

        VerticalLayout content = new VerticalLayout(nameField);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        // Boutons
        Button saveButton = new Button(roomType == null ? "✅ Créer" : "💾 Sauvegarder", 
            new Icon(roomType == null ? VaadinIcon.CHECK : VaadinIcon.DOWNLOAD));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("❌ Annuler", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            if (name != null && !name.trim().isEmpty()) {
                try {
                    if (roomType == null) {
                        // Nouveau type de salle
                        RoomType newRoomType = new RoomType();
                        newRoomType.setName(name.trim());
                        roomTypeManager.saveRoomType(newRoomType);
                        showSuccessNotification("Type de salle créé avec succès !");
                    } else {
                        // Modification
                        roomType.setName(name.trim());
                        roomTypeManager.updateRoomType(roomType);
                        showSuccessNotification("Type de salle modifié avec succès !");
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
