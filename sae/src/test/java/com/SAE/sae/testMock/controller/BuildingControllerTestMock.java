package com.SAE.sae.testMock.controller;

import com.SAE.sae.controller.BuildingController;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.BuildingManager;
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
public class BuildingControllerTestMock {

    @Mock
    private BuildingManager buildingManager;

    @InjectMocks
    private BuildingController buildingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Building sampleBuilding;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(buildingController).build();
        objectMapper = new ObjectMapper();

        // Création d'un objet Building d'exemple
        sampleBuilding = new Building();
        sampleBuilding.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/building - Récupérer toutes les Buildings")
    void getAllBuildings_ShouldReturnAllBuildings() throws Exception {
        // Given
        List<Building> Buildings = Arrays.asList(sampleBuilding, new Building());
        when(buildingManager.getAllBuildings()).thenReturn(Buildings);

        // When & Then
        mockMvc.perform(get("/api/v1/building"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(buildingManager, times(1)).getAllBuildings();
    }

    @Test
    @DisplayName("GET /api/v1/building - Retourner liste vide quand aucune Building")
    void getAllBuildings_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(buildingManager.getAllBuildings()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/building"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(buildingManager, times(1)).getAllBuildings();
    }

    @Test
    @DisplayName("GET /api/v1/building/{id} - Récupérer une Building par ID existant")
    void getBuildingById_WhenExists_ShouldReturnBuilding() throws Exception {
        // Given
        Integer id = 1;
        when(buildingManager.getBuildingById(id)).thenReturn(sampleBuilding);

        // When & Then
        mockMvc.perform(get("/api/v1/building/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(buildingManager, times(1)).getBuildingById(id);
    }

    @Test
    @DisplayName("GET /api/v1/building/{id} - Retourner 404 pour ID inexistant")
    void getBuildingById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(buildingManager.getBuildingById(id)).thenThrow(new IllegalArgumentException("Building non trouvée"));

        // When & Then
        mockMvc.perform(get("/api/v1/building/{id}", id))
                .andExpect(status().isNotFound());

        verify(buildingManager, times(1)).getBuildingById(id);
    }

    @Test
    @DisplayName("POST /api/v1/building - Créer une nouvelle Building")
    void createBuilding_ShouldCreateAndReturnBuilding() throws Exception {
        // Given
        Building newBuilding = new Building();
        newBuilding.setName("Building 1");

        when(buildingManager.saveBuilding(any(Building.class))).thenReturn(sampleBuilding);

        // When & Then
        mockMvc.perform(post("/api/v1/building")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBuilding)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(buildingManager, times(1)).saveBuilding(any(Building.class));
    }

    @Test
    @DisplayName("PUT /api/v1/building - Mettre à jour une Building existante")
    void updateBuilding_ShouldUpdateAndReturnBuilding() throws Exception {
        // Given
        Building updatedBuilding = new Building();
        updatedBuilding.setId(1);
        updatedBuilding.setName("Building 1");

        when(buildingManager.saveBuilding(any(Building.class))).thenReturn(updatedBuilding);

        // When & Then
        mockMvc.perform(put("/api/v1/building")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBuilding)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(buildingManager, times(1)).saveBuilding(any(Building.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/building/{id} - Retourner 404 pour ID inexistant")
    void deleteBuilding_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;

        // When & Then
        mockMvc.perform(delete("/api/v1/building/{id}", id))
                .andExpect(status().isNotFound());

        verify(buildingManager, never()).deleteBuildingById(id);
    }
}