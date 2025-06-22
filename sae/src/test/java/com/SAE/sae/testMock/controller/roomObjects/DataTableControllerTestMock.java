package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.DataTableController;
import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.service.RoomObjects.DataTableManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DataTableControllerTestMock {

    @Mock
    private DataTableManager dataTableManager;

    @InjectMocks
    private DataTableController dataTableController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DataTable sampleDataTable;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataTableController).build();
        objectMapper = new ObjectMapper();

        // Création d'un objet DataTable d'exemple
        sampleDataTable = new DataTable();
        sampleDataTable.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/data-tables - Récupérer toutes les DataTables")
    void getAllDataTables_ShouldReturnAllDataTables() throws Exception {
        // Given
        List<DataTable> dataTables = Arrays.asList(sampleDataTable, new DataTable());
        when(dataTableManager.findAll()).thenReturn(dataTables);

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(dataTableManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/data-tables - Retourner liste vide quand aucune DataTable")
    void getAllDataTables_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(dataTableManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(dataTableManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/data-tables/{id} - Récupérer une DataTable par ID existant")
    void getDataTableById_WhenExists_ShouldReturnDataTable() throws Exception {
        // Given
        Integer id = 1;
        when(dataTableManager.findById(id)).thenReturn(sampleDataTable);

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(dataTableManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/data-tables/{id} - Retourner 404 pour ID inexistant")
    void getDataTableById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(dataTableManager.findById(id)).thenThrow(new IllegalArgumentException("DataTable non trouvée"));

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/{id}", id))
                .andExpect(status().isNotFound());

        verify(dataTableManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/data-tables/by-room/{roomId} - Récupérer DataTables par ID de salle")
    void getByRoomId_ShouldReturnDataTablesForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<DataTable> roomDataTables = Arrays.asList(sampleDataTable);
        when(dataTableManager.findByRoomId(roomId)).thenReturn(roomDataTables);

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(dataTableManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/data-tables/by-room/{roomId} - Retourner liste vide pour salle sans DataTables")
    void getByRoomId_WhenNoDataTables_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(dataTableManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(dataTableManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/data-tables/by-custom-name - Récupérer DataTables par nom personnalisé")
    void getByCustomName_ShouldReturnDataTablesWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<DataTable> namedDataTables = Arrays.asList(sampleDataTable);
        when(dataTableManager.findByCustomName(customName)).thenReturn(namedDataTables);

        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(dataTableManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/data-tables - Créer une nouvelle DataTable")
    void createDataTable_ShouldCreateAndReturnDataTable() throws Exception {
        // Given
        DataTable newDataTable = new DataTable();
        newDataTable.setCustomName("Nouvelle Table");

        when(dataTableManager.save(any(DataTable.class))).thenReturn(sampleDataTable);

        // When & Then
        mockMvc.perform(post("/api/v1/data-tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDataTable)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(dataTableManager, times(1)).save(any(DataTable.class));
    }

    @Test
    @DisplayName("PUT /api/v1/data-tables - Mettre à jour une DataTable existante")
    void updateDataTable_ShouldUpdateAndReturnDataTable() throws Exception {
        // Given
        DataTable updatedDataTable = new DataTable();
        updatedDataTable.setId(1);
        updatedDataTable.setCustomName("Table Modifiée");

        when(dataTableManager.save(any(DataTable.class))).thenReturn(updatedDataTable);

        // When & Then
        mockMvc.perform(put("/api/v1/data-tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDataTable)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(dataTableManager, times(1)).save(any(DataTable.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/data-tables/{id} - Supprimer une DataTable existante")
    void deleteDataTable_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(dataTableManager.existsById(id)).thenReturn(true);
        doNothing().when(dataTableManager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/data-tables/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("DataTable supprimée avec succès"));

        verify(dataTableManager, times(1)).existsById(id);
        verify(dataTableManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/data-tables/{id} - Retourner 404 pour ID inexistant")
    void deleteDataTable_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(dataTableManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/data-tables/{id}", id))
                .andExpect(status().isNotFound());

        verify(dataTableManager, times(1)).existsById(id);
        verify(dataTableManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/data-tables/by-room/{roomId} - Supprimer toutes les DataTables d'une salle")
    void deleteByRoomId_ShouldDeleteAllDataTablesInRoom() throws Exception {
        // Given
        Integer roomId = 1;
        doNothing().when(dataTableManager).deleteByRoomId(roomId);

        // When & Then
        mockMvc.perform(delete("/api/v1/data-tables/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les DataTables de la salle ont été supprimées"));

        verify(dataTableManager, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/data-tables/by-custom-name - Supprimer toutes les DataTables avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllDataTablesWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(dataTableManager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/data-tables/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les DataTables avec ce nom ont été supprimées"));

        verify(dataTableManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/data-tables/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(dataTableManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/data-tables/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(dataTableManager, never()).deleteByCustomName(anyString());
    }
}