package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.HeaterController;
import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.repository.RoomObjects.HeaterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HeaterControllerTestMock {

    @Mock
    private HeaterRepository HeaterRepository;

    @InjectMocks
    private HeaterController HeaterController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Heater sampleHeater;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(HeaterController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Heater d'exemple
        sampleHeater = new Heater();
        sampleHeater.setId(1);
        sampleHeater.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité Heater
    }

    @Test
    @DisplayName("GET /api/v1/heaters - Récupérer toutes les Heaters")
    void getAllHeaters_ShouldReturnAllHeaters() throws Exception {
        // Given
        List<Heater> Heaters = Arrays.asList(sampleHeater, new Heater());
        when(HeaterRepository.findAll()).thenReturn(Heaters);

        // When & Then
        mockMvc.perform(get("/api/v1/heaters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(HeaterRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/heaters - Retourner liste vide quand aucune Heater")
    void getAllHeaters_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(HeaterRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/heaters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(HeaterRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/heaters/{id} - Récupérer une Heater par ID existant")
    void getHeaterById_WhenExists_ShouldReturnHeater() throws Exception {
        // Given
        Integer id = 1;
        when(HeaterRepository.findById(id)).thenReturn(Optional.of(sampleHeater));

        // When & Then
        mockMvc.perform(get("/api/v1/heaters/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(HeaterRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/{id} - Retourner 404 pour ID inexistant")
    void getHeaterById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(HeaterRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/heaters/{id}", id))
                .andExpect(status().isNotFound());

        verify(HeaterRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-room/{roomId} - Récupérer Heaters par ID de salle")
    void getByRoomId_ShouldReturnHeatersForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Heater> roomHeaters = Arrays.asList(sampleHeater);
        when(HeaterRepository.findByRoom_Id(roomId)).thenReturn(roomHeaters);

        // When & Then
        mockMvc.perform(get("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(HeaterRepository, times(1)).findByRoom_Id(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-room/{roomId} - Retourner liste vide pour salle sans Heaters")
    void getByRoomId_WhenNoHeaters_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(HeaterRepository.findByRoom_Id(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(HeaterRepository, times(1)).findByRoom_Id(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/heaters/by-custom-name - Récupérer Heaters par nom personnalisé")
    void getByCustomName_ShouldReturnHeatersWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Heater> namedHeaters = Arrays.asList(sampleHeater);
        when(HeaterRepository.findByCustomName(customName)).thenReturn(namedHeaters);

        // When & Then
        mockMvc.perform(get("/api/v1/heaters/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(HeaterRepository, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/heaters - Créer une nouvelle Heater")
    void createHeater_ShouldCreateAndReturnHeater() throws Exception {
        // Given
        Heater newHeater = new Heater();
        newHeater.setCustomName("Nouvelle Table");
        
        when(HeaterRepository.save(any(Heater.class))).thenReturn(sampleHeater);

        // When & Then
        mockMvc.perform(post("/api/v1/heaters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newHeater)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(HeaterRepository, times(1)).save(any(Heater.class));
    }

    @Test
    @DisplayName("PUT /api/v1/heaters - Mettre à jour une Heater existante")
    void updateHeater_ShouldUpdateAndReturnHeater() throws Exception {
        // Given
        Heater updatedHeater = new Heater();
        updatedHeater.setId(1);
        updatedHeater.setCustomName("Table Modifiée");
        
        when(HeaterRepository.save(any(Heater.class))).thenReturn(updatedHeater);

        // When & Then
        mockMvc.perform(put("/api/v1/heaters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedHeater)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(HeaterRepository, times(1)).save(any(Heater.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/{id} - Supprimer une Heater existante")
    void deleteHeater_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(HeaterRepository.existsById(id)).thenReturn(true);
        doNothing().when(HeaterRepository).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/heaters/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Heater supprimée avec succès"));

        verify(HeaterRepository, times(1)).existsById(id);
        verify(HeaterRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/{id} - Retourner 404 pour ID inexistant")
    void deleteHeater_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(HeaterRepository.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/heaters/{id}", id))
                .andExpect(status().isNotFound());

        verify(HeaterRepository, times(1)).existsById(id);
        verify(HeaterRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/by-room/{roomId} - Supprimer toutes les Heaters d'une salle")
    void deleteByRoomId_ShouldDeleteAllHeatersInRoom() throws Exception {
        // Given
        Integer roomId = 1;
        doNothing().when(HeaterRepository).deleteByRoomId(roomId);

        // When & Then
        mockMvc.perform(delete("/api/v1/heaters/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Heaters de la salle ont été supprimées"));

        verify(HeaterRepository, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/heaters/by-custom-name - Supprimer toutes les Heaters avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllHeatersWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(HeaterRepository).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/heaters/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Heaters avec ce nom ont été supprimées"));

        verify(HeaterRepository, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/heaters/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(HeaterRepository, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/heaters/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(HeaterRepository, never()).deleteByCustomName(anyString());
    }

    @Test
    @DisplayName("Test d'intégration - Scénario complet CRUD")
    void fullCrudScenario_ShouldWorkCorrectly() throws Exception {
        // Cette méthode pourrait tester un scénario complet :
        // 1. Créer une Heater
        // 2. La récupérer
        // 3. La modifier
        // 4. La supprimer
        
        // Ceci est plus adapté pour des tests d'intégration
        // mais peut être utile pour valider le comportement global
    }
}