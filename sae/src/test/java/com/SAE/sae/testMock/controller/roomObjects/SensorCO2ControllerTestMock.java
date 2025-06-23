package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.SensorCO2Controller;
import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.service.RoomObjects.SensorCO2Manager;
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
public class SensorCO2ControllerTestMock {

    @Mock
    private SensorCO2Manager sensorCO2Manager;

    @InjectMocks
    private SensorCO2Controller sensorCO2Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SensorCO2 sampleSensorCO2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sensorCO2Controller).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet SensorCO2 d'exemple
        sampleSensorCO2 = new SensorCO2();
        sampleSensorCO2.setId(1);
        sampleSensorCO2.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité SensorCO2
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s - Récupérer toutes les SensorCO2s")
    void getAllSensorCO2s_ShouldReturnAllSensorCO2s() throws Exception {
        // Given
        List<SensorCO2> SensorCO2s = Arrays.asList(sampleSensorCO2, new SensorCO2());
        when(sensorCO2Manager.findAll()).thenReturn(SensorCO2s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(sensorCO2Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s - Retourner liste vide quand aucune SensorCO2")
    void getAllSensorCO2s_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(sensorCO2Manager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensorCO2Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s/{id} - Récupérer une SensorCO2 par ID existant")
    void getSensorCO2ById_WhenExists_ShouldReturnSensorCO2() throws Exception {
        // Given
        Integer id = 1;
        when(sensorCO2Manager.findById(id)).thenReturn(sampleSensorCO2);

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(sensorCO2Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s/{id} - Retourner 404 pour ID inexistant")
    void getSensorCO2ById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensorCO2Manager.findById(id)).thenThrow(new IllegalArgumentException("SensorCo2 not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensorCO2Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s/by-room/{roomId} - Récupérer SensorCO2s par ID de salle")
    void getByRoomId_ShouldReturnSensorCO2sForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<SensorCO2> roomSensorCO2s = Arrays.asList(sampleSensorCO2);
        when(sensorCO2Manager.findByRoomId(roomId)).thenReturn(roomSensorCO2s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensorCO2Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s/by-room/{roomId} - Retourner liste vide pour salle sans SensorCO2s")
    void getByRoomId_WhenNoSensorCO2s_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(sensorCO2Manager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensorCO2Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensorco2s/by-custom-name - Récupérer SensorCO2s par nom personnalisé")
    void getByCustomName_ShouldReturnSensorCO2sWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<SensorCO2> namedSensorCO2s = Arrays.asList(sampleSensorCO2);
        when(sensorCO2Manager.findByCustomName(customName)).thenReturn(namedSensorCO2s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensorCO2Manager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/sensorco2s - Créer une nouvelle SensorCO2")
    void createSensorCO2_ShouldCreateAndReturnSensorCO2() throws Exception {
        // Given
        SensorCO2 newSensorCO2 = new SensorCO2();
        newSensorCO2.setCustomName("Nouvelle Table");
        
        when(sensorCO2Manager.save(any(SensorCO2.class))).thenReturn(sampleSensorCO2);

        // When & Then
        mockMvc.perform(post("/api/v1/sensorco2s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSensorCO2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(sensorCO2Manager, times(1)).save(any(SensorCO2.class));
    }

    @Test
    @DisplayName("PUT /api/v1/sensorco2s - Mettre à jour une SensorCO2 existante")
    void updateSensorCO2_ShouldUpdateAndReturnSensorCO2() throws Exception {
        // Given
        SensorCO2 updatedSensorCO2 = new SensorCO2();
        updatedSensorCO2.setId(1);
        updatedSensorCO2.setCustomName("Table Modifiée");
        
        when(sensorCO2Manager.save(any(SensorCO2.class))).thenReturn(updatedSensorCO2);

        // When & Then
        mockMvc.perform(put("/api/v1/sensorco2s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSensorCO2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(sensorCO2Manager, times(1)).save(any(SensorCO2.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/sensorco2s/{id} - Supprimer une SensorCO2 existante")
    void deleteSensorCO2_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(sensorCO2Manager.existsById(id)).thenReturn(true);
        doNothing().when(sensorCO2Manager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensorco2s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("SensorCO2 supprimée avec succès"));

        verify(sensorCO2Manager, times(1)).existsById(id);
        verify(sensorCO2Manager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensorco2s/{id} - Retourner 404 pour ID inexistant")
    void deleteSensorCO2_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensorCO2Manager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensorco2s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensorCO2Manager, times(1)).existsById(id);
        verify(sensorCO2Manager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensorco2s/by-custom-name - Supprimer toutes les SensorCO2s avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllSensorCO2sWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(sensorCO2Manager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensorco2s/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les SensorCO2s avec ce nom ont été supprimées"));

        verify(sensorCO2Manager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/sensorco2s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensorCO2Manager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/sensorco2s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensorCO2Manager, never()).deleteByCustomName(anyString());
    }
}