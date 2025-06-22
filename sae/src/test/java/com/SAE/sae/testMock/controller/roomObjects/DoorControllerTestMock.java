package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.DoorController;
import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.service.RoomObjects.DoorManager;
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
public class DoorControllerTestMock {

    @Mock
    private DoorManager doorManager;

    @InjectMocks
    private DoorController doorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Door sampleDoor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(doorController).build();
        objectMapper = new ObjectMapper();

        // Création d'un objet Door d'exemple
        sampleDoor = new Door();
        sampleDoor.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/doors - Récupérer toutes les Doors")
    void getAllDoors_ShouldReturnAllDoors() throws Exception {
        // Given
        List<Door> Doors = Arrays.asList(sampleDoor, new Door());
        when(doorManager.findAll()).thenReturn(Doors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(doorManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/doors - Retourner liste vide quand aucune Door")
    void getAllDoors_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(doorManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/doors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(doorManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/doors/{id} - Récupérer une Door par ID existant")
    void getDoorById_WhenExists_ShouldReturnDoor() throws Exception {
        // Given
        Integer id = 1;
        when(doorManager.findById(id)).thenReturn(sampleDoor);

        // When & Then
        mockMvc.perform(get("/api/v1/doors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(doorManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/doors/{id} - Retourner 404 pour ID inexistant")
    void getDoorById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(doorManager.findById(id)).thenThrow(new IllegalArgumentException("Door not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/doors/{id}", id))
                .andExpect(status().isNotFound());

        verify(doorManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-room/{roomId} - Récupérer Doors par ID de salle")
    void getByRoomId_ShouldReturnDoorsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Door> roomDoors = Arrays.asList(sampleDoor);
        when(doorManager.findByRoomId(roomId)).thenReturn(roomDoors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(doorManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-room/{roomId} - Retourner liste vide pour salle sans Doors")
    void getByRoomId_WhenNoDoors_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(doorManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(doorManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-custom-name - Récupérer Doors par nom personnalisé")
    void getByCustomName_ShouldReturnDoorsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Door> namedDoors = Arrays.asList(sampleDoor);
        when(doorManager.findByCustomName(customName)).thenReturn(namedDoors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(doorManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/doors - Créer une nouvelle Door")
    void createDoor_ShouldCreateAndReturnDoor() throws Exception {
        // Given
        Door newDoor = new Door();
        newDoor.setCustomName("Nouvelle Table");

        when(doorManager.save(any(Door.class))).thenReturn(sampleDoor);

        // When & Then
        mockMvc.perform(post("/api/v1/doors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(doorManager, times(1)).save(any(Door.class));
    }

    @Test
    @DisplayName("PUT /api/v1/doors - Mettre à jour une Door existante")
    void updateDoor_ShouldUpdateAndReturnDoor() throws Exception {
        // Given
        Door updatedDoor = new Door();
        updatedDoor.setId(1);
        updatedDoor.setCustomName("Table Modifiée");

        when(doorManager.save(any(Door.class))).thenReturn(updatedDoor);

        // When & Then
        mockMvc.perform(put("/api/v1/doors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDoor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(doorManager, times(1)).save(any(Door.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/{id} - Supprimer une Door existante")
    void deleteDoor_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(doorManager.existsById(id)).thenReturn(true);
        doNothing().when(doorManager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Door supprimée avec succès"));

        verify(doorManager, times(1)).existsById(id);
        verify(doorManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/{id} - Retourner 404 pour ID inexistant")
    void deleteDoor_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(doorManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/{id}", id))
                .andExpect(status().isNotFound());

        verify(doorManager, times(1)).existsById(id);
        verify(doorManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/by-room/{roomId} - Supprimer toutes les Doors d'une salle")
    void deleteByRoomId_ShouldDeleteAllDoorsInRoom() throws Exception {
        // Given
        Integer roomId = 1;
        doNothing().when(doorManager).deleteByRoomId(roomId);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Doors de la salle ont été supprimées"));

        verify(doorManager, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/by-custom-name - Supprimer toutes les Doors avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllDoorsWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(doorManager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Doors avec ce nom ont été supprimées"));

        verify(doorManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(doorManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(doorManager, never()).deleteByCustomName(anyString());
    }
}