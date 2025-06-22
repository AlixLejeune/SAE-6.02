package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.HeaterController;
import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.service.RoomObjects.HeaterManager;
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
public class HeaterControllerTestMock {

    @Mock
    private HeaterManager heaterManager;

    @InjectMocks
    private HeaterController heaterController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Heater sampleHeater;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(heaterController).build();
        objectMapper = new ObjectMapper();

        sampleHeater = new Heater();
        sampleHeater.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/heaters - Récupérer toutes les Heaters")
    void getAllHeaters_ShouldReturnAllHeaters() throws Exception {
        List<Heater> heaters = Arrays.asList(sampleHeater, new Heater());
        when(heaterManager.findAll()).thenReturn(heaters);

        mockMvc.perform(get("/api/v1/heaters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(heaterManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/heaters - Retourner liste vide quand aucune Heater")
    void getAllHeaters_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        when(heaterManager.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/heaters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(heaterManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/heaters/{id} - Récupérer une Heater par ID existant")
    void getHeaterById_WhenExists_ShouldReturnHeater() throws Exception {
        Integer id = 1;
        when(heaterManager.findById(id)).thenReturn(sampleHeater);

        mockMvc.perform(get("/api/v1/heaters/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(heaterManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/{id} - Retourner 404 pour ID inexistant")
    void getHeaterById_WhenNotExists_ShouldReturn404() throws Exception {
        Integer id = 999;
        when(heaterManager.findById(id)).thenThrow(new IllegalArgumentException("Heater not found"));

        mockMvc.perform(get("/api/v1/heaters/{id}", id))
                .andExpect(status().isNotFound());

        verify(heaterManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-room/{roomId} - Récupérer Heaters par ID de salle")
    void getByRoomId_ShouldReturnHeatersForRoom() throws Exception {
        Long roomId = 1L;
        List<Heater> roomHeaters = Arrays.asList(sampleHeater);
        when(heaterManager.findByRoomId(roomId)).thenReturn(roomHeaters);

        mockMvc.perform(get("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(heaterManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-room/{roomId} - Retourner liste vide pour salle sans Heaters")
    void getByRoomId_WhenNoHeaters_ShouldReturnEmptyList() throws Exception {
        Long roomId = 999L;
        when(heaterManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(heaterManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-custom-name - Récupérer Heaters par nom personnalisé")
    void getByCustomName_ShouldReturnHeatersWithCustomName() throws Exception {
        String customName = "Table Test";
        List<Heater> namedHeaters = Arrays.asList(sampleHeater);
        when(heaterManager.findByCustomName(customName)).thenReturn(namedHeaters);

        mockMvc.perform(get("/api/v1/heaters/by-custom-name")
                        .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(heaterManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/heaters - Créer une nouvelle Heater")
    void createHeater_ShouldCreateAndReturnHeater() throws Exception {
        Heater newHeater = new Heater();
        newHeater.setCustomName("Nouvelle Table");

        when(heaterManager.save(any(Heater.class))).thenReturn(sampleHeater);

        mockMvc.perform(post("/api/v1/heaters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHeater)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(heaterManager, times(1)).save(any(Heater.class));
    }

    @Test
    @DisplayName("PUT /api/v1/heaters - Mettre à jour une Heater existante")
    void updateHeater_ShouldUpdateAndReturnHeater() throws Exception {
        Heater updatedHeater = new Heater();
        updatedHeater.setId(1);
        updatedHeater.setCustomName("Table Modifiée");

        when(heaterManager.save(any(Heater.class))).thenReturn(updatedHeater);

        mockMvc.perform(put("/api/v1/heaters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedHeater)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(heaterManager, times(1)).save(any(Heater.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/{id} - Supprimer une Heater existante")
    void deleteHeater_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        Integer id = 1;
        when(heaterManager.existsById(id)).thenReturn(true);
        doNothing().when(heaterManager).deleteById(id);

        mockMvc.perform(delete("/api/v1/heaters/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Heater supprimée avec succès"));

        verify(heaterManager, times(1)).existsById(id);
        verify(heaterManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/{id} - Retourner 404 pour ID inexistant")
    void deleteHeater_WhenNotExists_ShouldReturn404() throws Exception {
        Integer id = 999;
        when(heaterManager.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/heaters/{id}", id))
                .andExpect(status().isNotFound());

        verify(heaterManager, times(1)).existsById(id);
        verify(heaterManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/by-room/{roomId} - Supprimer toutes les Heaters d'une salle")
    void deleteByRoomId_ShouldDeleteAllHeatersInRoom() throws Exception {
        Integer roomId = 1;
        doNothing().when(heaterManager).deleteByRoomId(roomId);

        mockMvc.perform(delete("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Heaters de la salle ont été supprimées"));

        verify(heaterManager, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/by-custom-name - Supprimer toutes les Heaters avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllHeatersWithCustomName() throws Exception {
        String customName = "Table à supprimer";
        doNothing().when(heaterManager).deleteByCustomName(customName);

        mockMvc.perform(delete("/api/v1/heaters/by-custom-name")
                        .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Heaters avec ce nom ont été supprimées"));

        verify(heaterManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/heaters/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(heaterManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/heaters/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(heaterManager, never()).deleteByCustomName(anyString());
    }
}
