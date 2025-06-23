package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.PlugController;
import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.service.RoomObjects.PlugManager;
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
public class PlugControllerTestMock {

    @Mock
    private PlugManager plugManager;

    @InjectMocks
    private PlugController plugController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Plug samplePlug;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(plugController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Plug d'exemple
        samplePlug = new Plug();
        samplePlug.setId(1);
        samplePlug.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité Plug
    }

    @Test
    @DisplayName("GET /api/v1/plugs - Récupérer toutes les Plugs")
    void getAllPlugs_ShouldReturnAllPlugs() throws Exception {
        // Given
        List<Plug> Plugs = Arrays.asList(samplePlug, new Plug());
        when(plugManager.findAll()).thenReturn(Plugs);

        // When & Then
        mockMvc.perform(get("/api/v1/plugs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(plugManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/plugs - Retourner liste vide quand aucune Plug")
    void getAllPlugs_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(plugManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/plugs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(plugManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/plugs/{id} - Récupérer une Plug par ID existant")
    void getPlugById_WhenExists_ShouldReturnPlug() throws Exception {
        // Given
        Integer id = 1;
        when(plugManager.findById(id)).thenReturn(samplePlug);

        // When & Then
        mockMvc.perform(get("/api/v1/plugs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(plugManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/plugs/{id} - Retourner 404 pour ID inexistant")
    void getPlugById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(plugManager.findById(id)).thenThrow(new IllegalArgumentException("Plug not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/plugs/{id}", id))
                .andExpect(status().isNotFound());

        verify(plugManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/plugs/by-room/{roomId} - Récupérer Plugs par ID de salle")
    void getByRoomId_ShouldReturnPlugsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Plug> roomPlugs = Arrays.asList(samplePlug);
        when(plugManager.findByRoomId(roomId)).thenReturn(roomPlugs);

        // When & Then
        mockMvc.perform(get("/api/v1/plugs/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(plugManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/plugs/by-room/{roomId} - Retourner liste vide pour salle sans Plugs")
    void getByRoomId_WhenNoPlugs_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(plugManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/plugs/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(plugManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/plugs/by-custom-name - Récupérer Plugs par nom personnalisé")
    void getByCustomName_ShouldReturnPlugsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Plug> namedPlugs = Arrays.asList(samplePlug);
        when(plugManager.findByCustomName(customName)).thenReturn(namedPlugs);

        // When & Then
        mockMvc.perform(get("/api/v1/plugs/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(plugManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/plugs - Créer une nouvelle Plug")
    void createPlug_ShouldCreateAndReturnPlug() throws Exception {
        // Given
        Plug newPlug = new Plug();
        newPlug.setCustomName("Nouvelle Table");
        
        when(plugManager.save(any(Plug.class))).thenReturn(samplePlug);

        // When & Then
        mockMvc.perform(post("/api/v1/plugs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPlug)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(plugManager, times(1)).save(any(Plug.class));
    }

    @Test
    @DisplayName("PUT /api/v1/plugs - Mettre à jour une Plug existante")
    void updatePlug_ShouldUpdateAndReturnPlug() throws Exception {
        // Given
        Plug updatedPlug = new Plug();
        updatedPlug.setId(1);
        updatedPlug.setCustomName("Table Modifiée");
        
        when(plugManager.save(any(Plug.class))).thenReturn(updatedPlug);

        // When & Then
        mockMvc.perform(put("/api/v1/plugs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPlug)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(plugManager, times(1)).save(any(Plug.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/plugs/{id} - Supprimer une Plug existante")
    void deletePlug_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(plugManager.existsById(id)).thenReturn(true);
        doNothing().when(plugManager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/plugs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Plug supprimée avec succès"));

        verify(plugManager, times(1)).existsById(id);
        verify(plugManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/plugs/{id} - Retourner 404 pour ID inexistant")
    void deletePlug_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(plugManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/plugs/{id}", id))
                .andExpect(status().isNotFound());

        verify(plugManager, times(1)).existsById(id);
        verify(plugManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/plugs/by-custom-name - Supprimer toutes les Plugs avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllPlugsWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(plugManager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/plugs/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Plugs avec ce nom ont été supprimées"));

        verify(plugManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/plugs/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(plugManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/plugs/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(plugManager, never()).deleteByCustomName(anyString());
    }
}