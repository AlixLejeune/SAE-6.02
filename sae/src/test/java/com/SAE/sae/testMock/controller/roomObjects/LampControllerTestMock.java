package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.LampController;
import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.service.RoomObjects.LampManager;
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
public class LampControllerTestMock {

    @Mock
    private LampManager lampManager;

    @InjectMocks
    private LampController lampController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Lamp sampleLamp;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lampController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Lamp d'exemple
        sampleLamp = new Lamp();
        sampleLamp.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/lamps - Récupérer toutes les Lamps")
    void getAllLamps_ShouldReturnAllLamps() throws Exception {
        // Given
        List<Lamp> Lamps = Arrays.asList(sampleLamp, new Lamp());
        when(lampManager.findAll()).thenReturn(Lamps);

        // When & Then
        mockMvc.perform(get("/api/v1/lamps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(lampManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/lamps - Retourner liste vide quand aucune Lamp")
    void getAllLamps_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(lampManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/lamps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(lampManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/lamps/{id} - Récupérer une Lamp par ID existant")
    void getLampById_WhenExists_ShouldReturnLamp() throws Exception {
        // Given
        Integer id = 1;
        when(lampManager.findById(id)).thenReturn(sampleLamp);

        // When & Then
        mockMvc.perform(get("/api/v1/lamps/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(lampManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/lamps/{id} - Retourner 404 pour ID inexistant")
    void getLampById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(lampManager.findById(id)).thenThrow(new IllegalArgumentException("Lamp not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/lamps/{id}", id))
                .andExpect(status().isNotFound());

        verify(lampManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/lamps/by-room/{roomId} - Récupérer Lamps par ID de salle")
    void getByRoomId_ShouldReturnLampsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Lamp> roomLamps = Arrays.asList(sampleLamp);
        when(lampManager.findByRoomId(roomId)).thenReturn(roomLamps);

        // When & Then
        mockMvc.perform(get("/api/v1/lamps/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(lampManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/lamps/by-room/{roomId} - Retourner liste vide pour salle sans Lamps")
    void getByRoomId_WhenNoLamps_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(lampManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/lamps/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(lampManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/lamps/by-custom-name - Récupérer Lamps par nom personnalisé")
    void getByCustomName_ShouldReturnLampsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Lamp> namedLamps = Arrays.asList(sampleLamp);
        when(lampManager.findByCustomName(customName)).thenReturn(namedLamps);

        // When & Then
        mockMvc.perform(get("/api/v1/lamps/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(lampManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/lamps - Créer une nouvelle Lamp")
    void createLamp_ShouldCreateAndReturnLamp() throws Exception {
        // Given
        Lamp newLamp = new Lamp();
        newLamp.setCustomName("Nouvelle Table");
        
        when(lampManager.save(any(Lamp.class))).thenReturn(sampleLamp);

        // When & Then
        mockMvc.perform(post("/api/v1/lamps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLamp)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(lampManager, times(1)).save(any(Lamp.class));
    }

    @Test
    @DisplayName("PUT /api/v1/lamps - Mettre à jour une Lamp existante")
    void updateLamp_ShouldUpdateAndReturnLamp() throws Exception {
        // Given
        Lamp updatedLamp = new Lamp();
        updatedLamp.setId(1);
        updatedLamp.setCustomName("Table Modifiée");
        
        when(lampManager.save(any(Lamp.class))).thenReturn(updatedLamp);

        // When & Then
        mockMvc.perform(put("/api/v1/lamps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedLamp)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(lampManager, times(1)).save(any(Lamp.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/lamps/{id} - Supprimer une Lamp existante")
    void deleteLamp_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(lampManager.existsById(id)).thenReturn(true);
        doNothing().when(lampManager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/lamps/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Lamp supprimée avec succès"));

        verify(lampManager, times(1)).existsById(id);
        verify(lampManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/lamps/{id} - Retourner 404 pour ID inexistant")
    void deleteLamp_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(lampManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/lamps/{id}", id))
                .andExpect(status().isNotFound());

        verify(lampManager, times(1)).existsById(id);
        verify(lampManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/lamps/by-custom-name - Supprimer toutes les Lamps avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllLampsWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(lampManager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/lamps/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Lamps avec ce nom ont été supprimées"));

        verify(lampManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/lamps/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(lampManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/lamps/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(lampManager, never()).deleteByCustomName(anyString());
    }
}