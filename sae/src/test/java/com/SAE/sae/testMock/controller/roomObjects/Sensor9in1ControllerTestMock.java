package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.Sensor9in1Controller;
import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.service.RoomObjects.Sensor9in1Manager;
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
public class Sensor9in1ControllerTestMock {

    @Mock
    private Sensor9in1Manager sensor9in1Manager;

    @InjectMocks
    private Sensor9in1Controller sensor9in1Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Sensor9in1 sampleSensor9in1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sensor9in1Controller).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Sensor9in1 d'exemple
        sampleSensor9in1 = new Sensor9in1();
        sampleSensor9in1.setId(1);
        sampleSensor9in1.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité Sensor9in1
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s - Récupérer toutes les Sensor9in1s")
    void getAllSensor9in1s_ShouldReturnAllSensor9in1s() throws Exception {
        // Given
        List<Sensor9in1> Sensor9in1s = Arrays.asList(sampleSensor9in1, new Sensor9in1());
        when(sensor9in1Manager.findAll()).thenReturn(Sensor9in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(sensor9in1Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s - Retourner liste vide quand aucune Sensor9in1")
    void getAllSensor9in1s_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(sensor9in1Manager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensor9in1Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s/{id} - Récupérer une Sensor9in1 par ID existant")
    void getSensor9in1ById_WhenExists_ShouldReturnSensor9in1() throws Exception {
        // Given
        Integer id = 1;
        when(sensor9in1Manager.findById(id)).thenReturn(sampleSensor9in1);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(sensor9in1Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s/{id} - Retourner 404 pour ID inexistant")
    void getSensor9in1ById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensor9in1Manager.findById(id)).thenThrow(new IllegalArgumentException("Sensor9in1 not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensor9in1Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s/by-room/{roomId} - Récupérer Sensor9in1s par ID de salle")
    void getByRoomId_ShouldReturnSensor9in1sForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Sensor9in1> roomSensor9in1s = Arrays.asList(sampleSensor9in1);
        when(sensor9in1Manager.findByRoomId(roomId)).thenReturn(roomSensor9in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensor9in1Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s/by-room/{roomId} - Retourner liste vide pour salle sans Sensor9in1s")
    void getByRoomId_WhenNoSensor9in1s_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(sensor9in1Manager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensor9in1Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensor9in1s/by-custom-name - Récupérer Sensor9in1s par nom personnalisé")
    void getByCustomName_ShouldReturnSensor9in1sWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Sensor9in1> namedSensor9in1s = Arrays.asList(sampleSensor9in1);
        when(sensor9in1Manager.findByCustomName(customName)).thenReturn(namedSensor9in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensor9in1Manager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/sensor9in1s - Créer une nouvelle Sensor9in1")
    void createSensor9in1_ShouldCreateAndReturnSensor9in1() throws Exception {
        // Given
        Sensor9in1 newSensor9in1 = new Sensor9in1();
        newSensor9in1.setCustomName("Nouvelle Table");
        
        when(sensor9in1Manager.save(any(Sensor9in1.class))).thenReturn(sampleSensor9in1);

        // When & Then
        mockMvc.perform(post("/api/v1/sensor9in1s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSensor9in1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(sensor9in1Manager, times(1)).save(any(Sensor9in1.class));
    }

    @Test
    @DisplayName("PUT /api/v1/sensor9in1s - Mettre à jour une Sensor9in1 existante")
    void updateSensor9in1_ShouldUpdateAndReturnSensor9in1() throws Exception {
        // Given
        Sensor9in1 updatedSensor9in1 = new Sensor9in1();
        updatedSensor9in1.setId(1);
        updatedSensor9in1.setCustomName("Table Modifiée");
        
        when(sensor9in1Manager.save(any(Sensor9in1.class))).thenReturn(updatedSensor9in1);

        // When & Then
        mockMvc.perform(put("/api/v1/sensor9in1s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSensor9in1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(sensor9in1Manager, times(1)).save(any(Sensor9in1.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor9in1s/{id} - Supprimer une Sensor9in1 existante")
    void deleteSensor9in1_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(sensor9in1Manager.existsById(id)).thenReturn(true);
        doNothing().when(sensor9in1Manager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor9in1s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Sensor9in1 supprimée avec succès"));

        verify(sensor9in1Manager, times(1)).existsById(id);
        verify(sensor9in1Manager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor9in1s/{id} - Retourner 404 pour ID inexistant")
    void deleteSensor9in1_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensor9in1Manager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor9in1s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensor9in1Manager, times(1)).existsById(id);
        verify(sensor9in1Manager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor9in1s/by-custom-name - Supprimer toutes les Sensor9in1s avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllSensor9in1sWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(sensor9in1Manager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor9in1s/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Sensor9in1s avec ce nom ont été supprimées"));

        verify(sensor9in1Manager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/sensor9in1s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensor9in1Manager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/sensor9in1s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensor9in1Manager, never()).deleteByCustomName(anyString());
    }
}