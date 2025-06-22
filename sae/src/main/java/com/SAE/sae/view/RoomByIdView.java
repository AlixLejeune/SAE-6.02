package com.SAE.sae.view;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.RoomObjects.*;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "room", layout = MainLayout.class)
@PageTitle("Détails de la salle")
public class RoomByIdView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RoomManager roomManager;

    @Autowired
    public RoomByIdView(RoomManager roomManager) {
        this.roomManager = roomManager;
        setSpacing(true);
        setPadding(true);
        setMaxWidth("1200px");
        setMargin(true);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        removeAll(); // Clear previous content
        
        if (id == null) {
            displayError("Aucun ID fourni", "Veuillez spécifier un ID de salle valide dans l'URL.");
            return;
        }

        try {
            Room room = roomManager.getRoomById(id);
            
            if (room == null) {
                displayError("Salle introuvable", "Aucune salle trouvée avec l'ID " + id);
            } else {
                displayRoomDetails(room);
            }
        } catch (Exception e) {
            displayError("Erreur", "Erreur lors de la récupération de la salle : " + e.getMessage());
        }
    }

    private void displayRoomDetails(Room room) {
        // Header with room name
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        
        Icon roomIcon = new Icon(VaadinIcon.BUILDING);
        roomIcon.setSize("2em");
        roomIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        H1 title = new H1(room.getName());
        title.getStyle().set("margin", "0").set("color", "var(--lumo-primary-color)");
        
        header.add(roomIcon, title);
        add(header);

        // Room ID badge
        if (room.getId() != null) {
            Span idBadge = new Span("ID: " + room.getId());
            idBadge.getStyle()
                .set("background-color", "var(--lumo-primary-color-10pct)")
                .set("color", "var(--lumo-primary-text-color)")
                .set("padding", "0.25rem 0.5rem")
                .set("border-radius", "1rem")
                .set("font-size", "0.875rem")
                .set("font-weight", "500");
            add(idBadge);
        }

        // Main content in horizontal layout for larger screens
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setWidthFull();
        mainContent.setSpacing(true);

        // Left column - Room info
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setWidth("40%");
        leftColumn.setSpacing(true);

        // Dimensions section
        leftColumn.add(createSectionTitle("Dimensions", VaadinIcon.RESIZE_H));
        leftColumn.add(createDimensionsCard(room));

        // Building information section
        if (room.getBuilding() != null) {
            leftColumn.add(createSectionTitle("Bâtiment", VaadinIcon.BUILDING_O));
            leftColumn.add(createBuildingCard(room));
        }

        // Room type section
        leftColumn.add(createSectionTitle("Type de salle", VaadinIcon.TAG));
        leftColumn.add(createRoomTypeCard(room));

        // Volume calculation
        leftColumn.add(createSectionTitle("Informations calculées", VaadinIcon.CALC));
        leftColumn.add(createVolumeCard(room));

        // Right column - Room objects
        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setWidth("60%");
        rightColumn.setSpacing(true);

        rightColumn.add(createSectionTitle("Équipements et objets", VaadinIcon.CUBE));
        rightColumn.add(createRoomObjectsSection(room));

        mainContent.add(leftColumn, rightColumn);
        add(mainContent);
    }

    private Div createRoomObjectsSection(Room room) {
        Div section = new Div();
        section.setWidthFull();

        // Group room objects by type
        Map<String, List<RoomObject>> groupedObjects = groupRoomObjectsByType(room);

        if (groupedObjects.isEmpty()) {
            Div emptyState = createCard();
            HorizontalLayout emptyLayout = new HorizontalLayout();
            emptyLayout.setAlignItems(Alignment.CENTER);
            
            Icon emptyIcon = new Icon(VaadinIcon.INFO_CIRCLE);
            emptyIcon.getStyle().set("color", "var(--lumo-secondary-text-color)");
            
            Span emptyMessage = new Span("Aucun équipement répertorié dans cette salle");
            emptyMessage.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-style", "italic");
            
            emptyLayout.add(emptyIcon, emptyMessage);
            emptyState.add(emptyLayout);
            section.add(emptyState);
            return section;
        }

        // Create sections for each object type
        groupedObjects.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String objectType = entry.getKey();
                List<RoomObject> objects = entry.getValue();
                
                section.add(createObjectTypeSection(objectType, objects));
            });

        return section;
    }

    private Div createObjectTypeSection(String objectType, List<RoomObject> objects) {
        Div typeSection = createCard();
        
        // Type header
        HorizontalLayout typeHeader = new HorizontalLayout();
        typeHeader.setAlignItems(Alignment.CENTER);
        typeHeader.setWidthFull();
        
        Icon typeIcon = getIconForObjectType(objectType);
        typeIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        H3 typeTitle = new H3(getDisplayNameForObjectType(objectType));
        typeTitle.getStyle().set("margin", "0").set("flex-grow", "1");
        
        Span countBadge = new Span(String.valueOf(objects.size()));
        countBadge.getStyle()
            .set("background-color", "var(--lumo-primary-color)")
            .set("color", "var(--lumo-primary-contrast-color)")
            .set("padding", "0.25rem 0.5rem")
            .set("border-radius", "50%")
            .set("font-size", "0.75rem")
            .set("font-weight", "bold")
            .set("min-width", "1.5rem")
            .set("text-align", "center");
        
        typeHeader.add(typeIcon, typeTitle, countBadge);
        typeSection.add(typeHeader);

        // Objects grid
        Grid<RoomObject> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("auto");
        
        grid.addColumn(obj -> obj.getId()).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(obj -> obj.getCustomName() != null ? obj.getCustomName() : "Sans nom")
            .setHeader("Nom personnalisé").setFlexGrow(1);
        
        // Position columns (if object implements IPosition)
        grid.addColumn(obj -> formatPosition(obj)).setHeader("Position (X, Y, Z)").setFlexGrow(1);
        
        // Size columns (if object implements ISize)
        grid.addColumn(obj -> formatSize(obj)).setHeader("Dimensions (L×l×h)").setFlexGrow(1);

        grid.setItems(objects);
        typeSection.add(grid);
        
        return typeSection;
    }

    private String formatPosition(RoomObject obj) {
        try {
            // Use reflection to get position values
            Double posX = getFieldValue(obj, "posX");
            Double posY = getFieldValue(obj, "posY");
            Double posZ = getFieldValue(obj, "posZ");
            
            if (posX != null && posY != null && posZ != null) {
                return String.format("%.1f, %.1f, %.1f", posX, posY, posZ);
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }
        return "Non définie";
    }

    private String formatSize(RoomObject obj) {
        try {
            // Use reflection to get size values
            Double sizeX = getFieldValue(obj, "sizeX");
            Double sizeY = getFieldValue(obj, "sizeY");
            Double sizeZ = getFieldValue(obj, "sizeZ");
            
            if (sizeX != null && sizeY != null && sizeZ != null) {
                return String.format("%.1f×%.1f×%.1f", sizeX, sizeY, sizeZ);
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }
        return "Non définie";
    }

    private Double getFieldValue(Object obj, String fieldName) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Double) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, List<RoomObject>> groupRoomObjectsByType(Room room) {
        // This would need to be implemented based on your service layer
        // For now, return empty map as we don't have access to the room objects
        // You'll need to add a method to get room objects by room ID
        return new HashMap<>();
    }

    private Icon getIconForObjectType(String objectType) {
        return switch (objectType.toLowerCase()) {
            case "door" -> new Icon(VaadinIcon.ARROW_RIGHT);
            case "window" -> new Icon(VaadinIcon.MODAL);
            case "datatable" -> new Icon(VaadinIcon.TABLE);
            case "heater" -> new Icon(VaadinIcon.FIRE);
            case "lamp" -> new Icon(VaadinIcon.LIGHTBULB);
            case "plug" -> new Icon(VaadinIcon.PLUG);
            case "siren" -> new Icon(VaadinIcon.BELL);
            case "sensor6in1", "sensor9in1", "sensorco2" -> new Icon(VaadinIcon.DOT_CIRCLE);
            default -> new Icon(VaadinIcon.CUBE);
        };
    }

    private String getDisplayNameForObjectType(String objectType) {
        return switch (objectType.toLowerCase()) {
            case "door" -> "Portes";
            case "window" -> "Fenêtres";
            case "datatable" -> "Tables";
            case "heater" -> "Radiateurs";
            case "lamp" -> "Éclairages";
            case "plug" -> "Prises électriques";
            case "siren" -> "Sirènes";
            case "sensor6in1" -> "Capteurs 6-en-1";
            case "sensor9in1" -> "Capteurs 9-en-1";
            case "sensorco2" -> "Capteurs CO2";
            default -> objectType;
        };
    }

    private HorizontalLayout createSectionTitle(String title, VaadinIcon iconType) {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.getStyle().set("margin-top", "1.5rem").set("margin-bottom", "0.5rem");
        
        Icon icon = new Icon(iconType);
        icon.setSize("1.2em");
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)");
        
        H2 sectionTitle = new H2(title);
        sectionTitle.getStyle().set("margin", "0").set("font-size", "1.25rem");
        
        titleLayout.add(icon, sectionTitle);
        return titleLayout;
    }

    private Div createDimensionsCard(Room room) {
        Div card = createCard();
        
        HorizontalLayout dimensionsLayout = new HorizontalLayout();
        dimensionsLayout.setWidthFull();
        dimensionsLayout.setJustifyContentMode(JustifyContentMode.AROUND);
        
        dimensionsLayout.add(
            createDimensionItem("Longueur", room.getLength(), "m", VaadinIcon.ARROW_CIRCLE_RIGHT_O),
            createDimensionItem("Largeur", room.getWidth(), "m", VaadinIcon.ARROW_CIRCLE_LEFT_O),
            createDimensionItem("Hauteur", room.getHeight(), "m", VaadinIcon.ARROW_CIRCLE_UP_O)
        );
        
        card.add(dimensionsLayout);
        return card;
    }

    private VerticalLayout createDimensionItem(String label, double value, String unit, VaadinIcon iconType) {
        VerticalLayout item = new VerticalLayout();
        item.setAlignItems(Alignment.CENTER);
        item.setSpacing(false);
        
        Icon icon = new Icon(iconType);
        icon.getStyle().set("color", "var(--lumo-primary-color)");
        
        Span valueSpan = new Span(String.format("%.1f %s", value, unit));
        valueSpan.getStyle().set("font-size", "1.5rem").set("font-weight", "bold");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size", "0.875rem");
        
        item.add(icon, valueSpan, labelSpan);
        return item;
    }

    private Div createBuildingCard(Room room) {
        Div card = createCard();
        
        HorizontalLayout buildingLayout = new HorizontalLayout();
        buildingLayout.setAlignItems(Alignment.CENTER);
        
        Icon buildingIcon = new Icon(VaadinIcon.BUILDING);
        buildingIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        VerticalLayout buildingInfo = new VerticalLayout();
        buildingInfo.setSpacing(false);
        buildingInfo.setPadding(false);
        
        Span buildingName = new Span(room.getBuilding().getName() != null ? room.getBuilding().getName() : "Nom non disponible");
        buildingName.getStyle().set("font-weight", "bold");
        
        buildingInfo.add(buildingName);
        
        buildingLayout.add(buildingIcon, buildingInfo);
        card.add(buildingLayout);
        return card;
    }

    private Div createRoomTypeCard(Room room) {
        Div card = createCard();
        
        HorizontalLayout typeLayout = new HorizontalLayout();
        typeLayout.setAlignItems(Alignment.CENTER);
        
        Icon typeIcon = new Icon(VaadinIcon.TAG);
        typeIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        Span typeId = new Span("ID du type: " + room.getFkRoomTypeId());
        typeId.getStyle().set("font-weight", "500");
        
        typeLayout.add(typeIcon, typeId);
        card.add(typeLayout);
        return card;
    }

    private Div createVolumeCard(Room room) {
        Div card = createCard();
        
        double volume = room.getLength() * room.getWidth() * room.getHeight();
        
        HorizontalLayout volumeLayout = new HorizontalLayout();
        volumeLayout.setAlignItems(Alignment.CENTER);
        
        Icon volumeIcon = new Icon(VaadinIcon.CUBE);
        volumeIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        VerticalLayout volumeInfo = new VerticalLayout();
        volumeInfo.setSpacing(false);
        volumeInfo.setPadding(false);
        
        Span volumeValue = new Span(String.format("%.2f m³", volume));
        volumeValue.getStyle().set("font-size", "1.25rem").set("font-weight", "bold");
        
        Span volumeLabel = new Span("Volume total");
        volumeLabel.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size", "0.875rem");
        
        volumeInfo.add(volumeValue, volumeLabel);
        volumeLayout.add(volumeIcon, volumeInfo);
        card.add(volumeLayout);
        return card;
    }

    private Div createCard() {
        Div card = new Div();
        card.getStyle()
            .set("background-color", "var(--lumo-base-color)")
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "8px")
            .set("padding", "1rem")
            .set("margin-bottom", "1rem")
            .set("box-shadow", "0 1px 3px rgba(0, 0, 0, 0.1)");
        return card;
    }

    private void displayError(String title, String message) {
        HorizontalLayout errorHeader = new HorizontalLayout();
        errorHeader.setAlignItems(Alignment.CENTER);
        
        Icon errorIcon = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        errorIcon.setSize("1.5em");
        errorIcon.getStyle().set("color", "var(--lumo-error-color)");
        
        H2 errorTitle = new H2(title);
        errorTitle.getStyle().set("margin", "0").set("color", "var(--lumo-error-color)");
        
        errorHeader.add(errorIcon, errorTitle);
        add(errorHeader);
        
        Div errorCard = new Div();
        errorCard.getStyle()
            .set("background-color", "var(--lumo-error-color-10pct)")
            .set("border", "1px solid var(--lumo-error-color-50pct)")
            .set("border-radius", "8px")
            .set("padding", "1rem")
            .set("margin-top", "1rem");
        
        Span errorMessage = new Span(message);
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorCard.add(errorMessage);
        add(errorCard);
    }
}