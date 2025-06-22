package com.SAE.sae.testMock.controller;

import com.SAE.sae.controller.RoomTypeController;
import com.SAE.sae.entity.RoomType;
import com.SAE.sae.service.RoomTypeManager;
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
public class RoomTypeControllerTestMock {

    @Mock
    private RoomTypeManager roomTypeManager;

    @InjectMocks
    private RoomTypeController roomTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RoomType sampleRoomType;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomTypeController).build();
        objectMapper = new ObjectMapper();

        // Création d'un objet RoomType d'exemple
        sampleRoomType = new RoomType();
        sampleRoomType.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/RoomType - Récupérer toutes les RoomTypes")
    void getAllRoomTypes_ShouldReturnAllRoomTypes() throws Exception {
        // Given
        List<RoomType> RoomTypes = Arrays.asList(sampleRoomType, new RoomType());
        when(roomTypeManager.getAllRoomTypes()).thenReturn(RoomTypes);

        // When & Then
        mockMvc.perform(get("/api/v1/RoomType"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(roomTypeManager, times(1)).getAllRoomTypes();
    }

    @Test
    @DisplayName("GET /api/v1/RoomType - Retourner liste vide quand aucune RoomType")
    void getAllRoomTypes_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(roomTypeManager.getAllRoomTypes()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/RoomType"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomTypeManager, times(1)).getAllRoomTypes();
    }

    @Test
    @DisplayName("GET /api/v1/RoomType/{id} - Récupérer une RoomType par ID existant")
    void getRoomTypeById_WhenExists_ShouldReturnRoomType() throws Exception {
        // Given
        Integer id = 1;
        when(roomTypeManager.getRoomTypeById(id)).thenReturn(sampleRoomType);

        // When & Then
        mockMvc.perform(get("/api/v1/RoomType/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomTypeManager, times(1)).getRoomTypeById(id);
    }

    @Test
    @DisplayName("GET /api/v1/RoomType/{id} - Retourner 404 pour ID inexistant")
    void getRoomTypeById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(roomTypeManager.getRoomTypeById(id)).thenThrow(new IllegalArgumentException("RoomType non trouvée"));

        // When & Then
        mockMvc.perform(get("/api/v1/RoomType/{id}", id))
                .andExpect(status().isNotFound());

        verify(roomTypeManager, times(1)).getRoomTypeById(id);
    }

    @Test
    @DisplayName("POST /api/v1/RoomType - Créer une nouvelle RoomType")
    void createRoomType_ShouldCreateAndReturnRoomType() throws Exception {
        // Given
        RoomType newRoomType = new RoomType();
        newRoomType.setName("RoomType 1");

        when(roomTypeManager.saveRoomType(any(RoomType.class))).thenReturn(sampleRoomType);

        // When & Then
        mockMvc.perform(post("/api/v1/RoomType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoomType)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomTypeManager, times(1)).saveRoomType(any(RoomType.class));
    }

    @Test
    @DisplayName("PUT /api/v1/RoomType - Mettre à jour une RoomType existante")
    void updateRoomType_ShouldUpdateAndReturnRoomType() throws Exception {
        // Given
        RoomType updatedRoomType = new RoomType();
        updatedRoomType.setId(1);
        updatedRoomType.setName("RoomType 1");

        when(roomTypeManager.saveRoomType(any(RoomType.class))).thenReturn(updatedRoomType);

        // When & Then
        mockMvc.perform(put("/api/v1/RoomType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRoomType)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(roomTypeManager, times(1)).saveRoomType(any(RoomType.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/RoomType/{id} - Retourner 404 pour ID inexistant")
    void deleteRoomType_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;

        // When & Then
        mockMvc.perform(delete("/api/v1/RoomType/{id}", id))
                .andExpect(status().isNotFound());

        verify(roomTypeManager, never()).deleteRoomTypeById(id);
    }
}