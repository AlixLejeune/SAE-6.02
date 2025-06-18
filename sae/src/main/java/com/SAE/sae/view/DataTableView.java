package com.SAE.sae.view;

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.repository.RoomObjects.DataTableRepository;
import com.SAE.sae.view.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "data-tables", layout = MainLayout.class)
public class DataTableView extends VerticalLayout {

    private final DataTableRepository dataTableRepository;
    private final Grid<DataTable> grid = new Grid<>(DataTable.class);

    @Autowired
    public DataTableView(DataTableRepository dataTableRepository) {
        this.dataTableRepository = dataTableRepository;

        // Titre
        add("📋 Liste des DataTables");

        // Bouton de chargement
        Button loadButton = new Button("🔄 Charger les DataTables", e -> loadData());

        // Configuration du grid
        grid.setColumns("id", "customName", "posX", "posY", "posZ", "sizeX", "sizeY", "sizeZ");
        grid.setWidthFull();

        // Ajout des composants
        add(loadButton, grid);

        // Chargement initial
        loadData();
    }

    private void loadData() {
        try {
            List<DataTable> dataTables = dataTableRepository.findAll();
            grid.setItems(dataTables);
            Notification.show("✅ " + dataTables.size() + " DataTables chargées");
        } catch (Exception e) {
            Notification.show("❌ Erreur lors du chargement : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
