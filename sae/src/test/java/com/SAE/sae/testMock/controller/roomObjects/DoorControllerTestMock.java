package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.DoorController;
import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.RoomObjects.DoorRepository;
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
public class DoorControllerTestMock {

    @Mock
    private DoorRepository DoorRepository;

    @InjectMocks
    private DoorController DoorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Door sampleDoor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(DoorController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Door d'exemple
        sampleDoor = new Door();
        sampleDoor.setId(1);
        sampleDoor.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité Door
    }

    @Test
    @DisplayName("GET /api/v1/doors - Récupérer toutes les Doors")
    void getAllDoors_ShouldReturnAllDoors() throws Exception {
        // Given
        List<Door> Doors = Arrays.asList(sampleDoor, new Door());
        when(DoorRepository.findAll()).thenReturn(Doors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(DoorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/doors - Retourner liste vide quand aucune Door")
    void getAllDoors_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(DoorRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/doors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(DoorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/doors/{id} - Récupérer une Door par ID existant")
    void getDoorById_WhenExists_ShouldReturnDoor() throws Exception {
        // Given
        Integer id = 1;
        when(DoorRepository.findById(id)).thenReturn(Optional.of(sampleDoor));

        // When & Then
        mockMvc.perform(get("/api/v1/doors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(DoorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/doors/{id} - Retourner 404 pour ID inexistant")
    void getDoorById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(DoorRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/doors/{id}", id))
                .andExpect(status().isNotFound());

        verify(DoorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-room/{roomId} - Récupérer Doors par ID de salle")
    void getByRoomId_ShouldReturnDoorsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Door> roomDoors = Arrays.asList(sampleDoor);
        when(DoorRepository.findByRoom_Id(roomId)).thenReturn(roomDoors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(DoorRepository, times(1)).findByRoom_Id(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-room/{roomId} - Retourner liste vide pour salle sans Doors")
    void getByRoomId_WhenNoDoors_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(DoorRepository.findByRoom_Id(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(DoorRepository, times(1)).findByRoom_Id(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/doors/by-custom-name - Récupérer Doors par nom personnalisé")
    void getByCustomName_ShouldReturnDoorsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Door> namedDoors = Arrays.asList(sampleDoor);
        when(DoorRepository.findByCustomName(customName)).thenReturn(namedDoors);

        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(DoorRepository, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/doors - Créer une nouvelle Door")
    void createDoor_ShouldCreateAndReturnDoor() throws Exception {
        // Given
        Door newDoor = new Door();
        newDoor.setCustomName("Nouvelle Table");
        
        when(DoorRepository.save(any(Door.class))).thenReturn(sampleDoor);

        // When & Then
        mockMvc.perform(post("/api/v1/doors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(DoorRepository, times(1)).save(any(Door.class));
    }

    @Test
    @DisplayName("PUT /api/v1/doors - Mettre à jour une Door existante")
    void updateDoor_ShouldUpdateAndReturnDoor() throws Exception {
        // Given
        Door updatedDoor = new Door();
        updatedDoor.setId(1);
        updatedDoor.setCustomName("Table Modifiée");
        
        when(DoorRepository.save(any(Door.class))).thenReturn(updatedDoor);

        // When & Then
        mockMvc.perform(put("/api/v1/doors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDoor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(DoorRepository, times(1)).save(any(Door.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/{id} - Supprimer une Door existante")
    void deleteDoor_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(DoorRepository.existsById(id)).thenReturn(true);
        doNothing().when(DoorRepository).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Door supprimée avec succès"));

        verify(DoorRepository, times(1)).existsById(id);
        verify(DoorRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/{id} - Retourner 404 pour ID inexistant")
    void deleteDoor_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(DoorRepository.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/{id}", id))
                .andExpect(status().isNotFound());

        verify(DoorRepository, times(1)).existsById(id);
        verify(DoorRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/by-room/{roomId} - Supprimer toutes les Doors d'une salle")
    void deleteByRoomId_ShouldDeleteAllDoorsInRoom() throws Exception {
        // Given
        Integer roomId = 1;
        doNothing().when(DoorRepository).deleteByRoomId(roomId);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Doors de la salle ont été supprimées"));

        verify(DoorRepository, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/doors/by-custom-name - Supprimer toutes les Doors avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllDoorsWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(DoorRepository).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Doors avec ce nom ont été supprimées"));

        verify(DoorRepository, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/doors/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(DoorRepository, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/doors/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(DoorRepository, never()).deleteByCustomName(anyString());
    }

    @Test
    @DisplayName("Test d'intégration - Scénario complet CRUD")
    void fullCrudScenario_ShouldWorkCorrectly() throws Exception {
        // Cette méthode pourrait tester un scénario complet :
        // 1. Créer une Door
        // 2. La récupérer
        // 3. La modifier
        // 4. La supprimer
        
        // Ceci est plus adapté pour des tests d'intégration
        // mais peut être utile pour valider le comportement global
    }
}