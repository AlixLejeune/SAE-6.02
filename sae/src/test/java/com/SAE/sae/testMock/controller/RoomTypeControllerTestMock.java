package com.SAE.sae.testMock.controller;

import com.SAE.sae.controller.RoomController;
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
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

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
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RoomTypeController(roomTypeManager))
                .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver()) // ou rien
                .build();
        objectMapper = new ObjectMapper();

        // Création d'un objet RoomType d'exemple
        sampleRoomType = new RoomType();
        sampleRoomType.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/room_types - Récupérer toutes les RoomTypes")
    void getAllRoomTypes_ShouldReturnAllRoomTypes() throws Exception {
        // Given
        List<RoomType> RoomTypes = Arrays.asList(sampleRoomType, new RoomType());
        when(roomTypeManager.getAllRoomTypes()).thenReturn(RoomTypes);

        // When & Then
        mockMvc.perform(get("/api/v1/room_types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(roomTypeManager, times(1)).getAllRoomTypes();
    }

    @Test
    @DisplayName("GET /api/v1/room_types - Retourner liste vide quand aucune RoomType")
    void getAllRoomTypes_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(roomTypeManager.getAllRoomTypes()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/room_types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomTypeManager, times(1)).getAllRoomTypes();
    }

    @Test
    @DisplayName("GET /api/v1/room_types/{id} - Récupérer une RoomType par ID existant")
    void getRoomTypeById_WhenExists_ShouldReturnRoomType() throws Exception {
        // Given
        Integer id = 1;
        when(roomTypeManager.getRoomTypeById(id)).thenReturn(sampleRoomType);

        // When & Then
        mockMvc.perform(get("/api/v1/room_types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomTypeManager, times(1)).getRoomTypeById(id);
    }

    @Test
    @DisplayName("POST /api/v1/room_types - Créer une nouvelle RoomType")
    void createRoomType_ShouldCreateAndReturnRoomType() throws Exception {
        // Given
        RoomType newRoomType = new RoomType();
        newRoomType.setName("RoomType 1");

        when(roomTypeManager.saveRoomType(any(RoomType.class))).thenReturn(sampleRoomType);

        // When & Then
        mockMvc.perform(post("/api/v1/room_types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoomType)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomTypeManager, times(1)).saveRoomType(any(RoomType.class));
    }
}