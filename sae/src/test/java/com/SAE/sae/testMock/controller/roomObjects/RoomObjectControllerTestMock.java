package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.RoomObjectController;
import com.SAE.sae.entity.RoomObjects.RoomObject;
import com.SAE.sae.service.RoomObjects.RoomObjectManager;
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
public class RoomObjectControllerTestMock {

    @Mock
    private RoomObjectManager roomObjectManager;

    @InjectMocks
    private RoomObjectController roomObjectController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RoomObject sampleRoomObject;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomObjectController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet RoomObject d'exemple
        sampleRoomObject = new TestRoomObject();
        sampleRoomObject.setId(1);
        sampleRoomObject.setCustomName("Table Test");
        // Ajoutez d'autres propriétés selon votre entité RoomObject
    }

    @Test
    @DisplayName("GET /api/v1/room-objects - Retourner liste vide quand aucune RoomObject")
    void getAllroomObjects_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(roomObjectManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomObjectManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/room-objects/{id} - Récupérer une RoomObject par ID existant")
    void getRoomObjectById_WhenExists_ShouldReturnRoomObject() throws Exception {
        // Given
        Integer id = 1;
        when(roomObjectManager.findById(id)).thenReturn(sampleRoomObject);

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customName").value("Table Test"));

        verify(roomObjectManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/room-objects/{id} - Retourner 404 pour ID inexistant")
    void getRoomObjectById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(roomObjectManager.findById(id)).thenThrow(new IllegalArgumentException("RoomObject not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/{id}", id))
                .andExpect(status().isNotFound());

        verify(roomObjectManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/room-objects/by-room/{roomId} - Récupérer roomObjects par ID de salle")
    void getByRoomId_ShouldReturnroomObjectsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<RoomObject> roomroomObjects = Arrays.asList(sampleRoomObject);
        when(roomObjectManager.findByRoomId(roomId)).thenReturn(roomroomObjects);

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(roomObjectManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/room-objects/by-room/{roomId} - Retourner liste vide pour salle sans roomObjects")
    void getByRoomId_WhenNoroomObjects_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(roomObjectManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomObjectManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/room-objects/by-custom-name - Récupérer roomObjects par nom personnalisé")
    void getByCustomName_ShouldReturnroomObjectsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<RoomObject> namedroomObjects = Arrays.asList(sampleRoomObject);
        when(roomObjectManager.findByCustomName(customName)).thenReturn(namedroomObjects);

        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(roomObjectManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("DELETE /api/v1/room-objects/{id} - Retourner 404 pour ID inexistant")
    void deleteRoomObject_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(roomObjectManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/room-objects/{id}", id))
                .andExpect(status().isNotFound());

        verify(roomObjectManager, times(1)).existsById(id);
        verify(roomObjectManager, never()).deleteById(id);
    }

    
    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/room-objects/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(roomObjectManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/room-objects/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(roomObjectManager, never()).deleteByCustomName(anyString());
    }
}


class TestRoomObject extends RoomObject {
    public TestRoomObject() {
        super();
        // Ajouter d'éventuelles implémentations si nécessaire
    }
}