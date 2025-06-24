package com.SAE.sae.testMock.controller;

import com.SAE.sae.controller.BuildingController;
import com.SAE.sae.controller.RoomController;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.RoomManager;
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
public class RoomControllerTestMock {

    @Mock
    private RoomManager roomManager;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Room sampleRoom;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RoomController(roomManager))
                .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver()) // ou rien
                .build();
        objectMapper = new ObjectMapper();

        // Création d'un objet Room d'exemple
        sampleRoom = new Room();
        sampleRoom.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/rooms - Récupérer toutes les Rooms")
    void getAllRooms_ShouldReturnAllRooms() throws Exception {
        // Given
        List<Room> Rooms = Arrays.asList(sampleRoom, new Room());
        when(roomManager.getAllRooms()).thenReturn(Rooms);

        // When & Then
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(roomManager, times(1)).getAllRooms();
    }

    @Test
    @DisplayName("GET /api/v1/rooms - Retourner liste vide quand aucune Room")
    void getAllRooms_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(roomManager.getAllRooms()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomManager, times(1)).getAllRooms();
    }

    @Test
    @DisplayName("GET /api/v1/rooms/{id} - Récupérer une Room par ID existant")
    void getRoomById_WhenExists_ShouldReturnRoom() throws Exception {
        // Given
        Integer id = 1;
        when(roomManager.getRoomById(id)).thenReturn(sampleRoom);

        // When & Then
        mockMvc.perform(get("/api/v1/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomManager, times(1)).getRoomById(id);
    }

    @Test
    @DisplayName("POST /api/v1/rooms - Créer une nouvelle Room")
    void createRoom_ShouldCreateAndReturnRoom() throws Exception {
        // Given
        Room newRoom = new Room();
        newRoom.setName("Room 1");

        when(roomManager.saveRoom(any(Room.class))).thenReturn(sampleRoom);

        // When & Then
        mockMvc.perform(post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(roomManager, times(1)).saveRoom(any(Room.class));
    }

}