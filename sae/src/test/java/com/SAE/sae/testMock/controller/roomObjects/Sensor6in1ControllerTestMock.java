package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.Sensor6in1Controller;
import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.service.RoomObjects.Sensor6in1Manager;
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
public class Sensor6in1ControllerTestMock {

    @Mock
    private Sensor6in1Manager sensor6in1Manager;

    @InjectMocks
    private Sensor6in1Controller sensor6in1Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Sensor6in1 sampleSensor6in1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sensor6in1Controller).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Sensor6in1 d'exemple
        sampleSensor6in1 = new Sensor6in1();
        sampleSensor6in1.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s - Récupérer toutes les Sensor6in1s")
    void getAllSensor6in1s_ShouldReturnAllSensor6in1s() throws Exception {
        // Given
        List<Sensor6in1> Sensor6in1s = Arrays.asList(sampleSensor6in1, new Sensor6in1());
        when(sensor6in1Manager.findAll()).thenReturn(Sensor6in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(sensor6in1Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s - Retourner liste vide quand aucune Sensor6in1")
    void getAllSensor6in1s_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(sensor6in1Manager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensor6in1Manager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s/{id} - Récupérer une Sensor6in1 par ID existant")
    void getSensor6in1ById_WhenExists_ShouldReturnSensor6in1() throws Exception {
        // Given
        Integer id = 1;
        when(sensor6in1Manager.findById(id)).thenReturn(sampleSensor6in1);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(sensor6in1Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s/{id} - Retourner 404 pour ID inexistant")
    void getSensor6in1ById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensor6in1Manager.findById(id)).thenThrow(new IllegalArgumentException("Sensor6in1 Object not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensor6in1Manager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s/by-room/{roomId} - Récupérer Sensor6in1s par ID de salle")
    void getByRoomId_ShouldReturnSensor6in1sForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Sensor6in1> roomSensor6in1s = Arrays.asList(sampleSensor6in1);
        when(sensor6in1Manager.findByRoomId(roomId)).thenReturn(roomSensor6in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensor6in1Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s/by-room/{roomId} - Retourner liste vide pour salle sans Sensor6in1s")
    void getByRoomId_WhenNoSensor6in1s_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(sensor6in1Manager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sensor6in1Manager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/sensor6in1s/by-custom-name - Récupérer Sensor6in1s par nom personnalisé")
    void getByCustomName_ShouldReturnSensor6in1sWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Sensor6in1> namedSensor6in1s = Arrays.asList(sampleSensor6in1);
        when(sensor6in1Manager.findByCustomName(customName)).thenReturn(namedSensor6in1s);

        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sensor6in1Manager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/sensor6in1s - Créer une nouvelle Sensor6in1")
    void createSensor6in1_ShouldCreateAndReturnSensor6in1() throws Exception {
        // Given
        Sensor6in1 newSensor6in1 = new Sensor6in1();
        newSensor6in1.setCustomName("Nouvelle Table");
        
        when(sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(sampleSensor6in1);

        // When & Then
        mockMvc.perform(post("/api/v1/sensor6in1s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSensor6in1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(sensor6in1Manager, times(1)).save(any(Sensor6in1.class));
    }

    @Test
    @DisplayName("PUT /api/v1/sensor6in1s - Mettre à jour une Sensor6in1 existante")
    void updateSensor6in1_ShouldUpdateAndReturnSensor6in1() throws Exception {
        // Given
        Sensor6in1 updatedSensor6in1 = new Sensor6in1();
        updatedSensor6in1.setId(1);
        updatedSensor6in1.setCustomName("Table Modifiée");
        
        when(sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(updatedSensor6in1);

        // When & Then
        mockMvc.perform(put("/api/v1/sensor6in1s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSensor6in1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(sensor6in1Manager, times(1)).save(any(Sensor6in1.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor6in1s/{id} - Supprimer une Sensor6in1 existante")
    void deleteSensor6in1_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(sensor6in1Manager.existsById(id)).thenReturn(true);
        doNothing().when(sensor6in1Manager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor6in1s/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Sensor6in1 supprimée avec succès"));

        verify(sensor6in1Manager, times(1)).existsById(id);
        verify(sensor6in1Manager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor6in1s/{id} - Retourner 404 pour ID inexistant")
    void deleteSensor6in1_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(sensor6in1Manager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor6in1s/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensor6in1Manager, times(1)).existsById(id);
        verify(sensor6in1Manager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/sensor6in1s/by-custom-name - Supprimer toutes les Sensor6in1s avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllSensor6in1sWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(sensor6in1Manager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/sensor6in1s/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Sensor6in1s avec ce nom ont été supprimées"));

        verify(sensor6in1Manager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/sensor6in1s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensor6in1Manager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/sensor6in1s/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(sensor6in1Manager, never()).deleteByCustomName(anyString());
    }
}