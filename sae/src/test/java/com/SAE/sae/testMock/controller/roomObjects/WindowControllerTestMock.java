package com.SAE.sae.testMock.controller.roomObjects;

import com.SAE.sae.controller.RoomObjects.WindowController;
import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.service.RoomObjects.WindowManager;
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
public class WindowControllerTestMock {

    @Mock
    private WindowManager windowManager;

    @InjectMocks
    private WindowController windowController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Window sampleWindow;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(windowController).build();
        objectMapper = new ObjectMapper();
        
        // Création d'un objet Window d'exemple
        sampleWindow = new Window();
        sampleWindow.setId(1);
    }

    @Test
    @DisplayName("GET /api/v1/windows - Récupérer toutes les Windows")
    void getAllWindows_ShouldReturnAllWindows() throws Exception {
        // Given
        List<Window> Windows = Arrays.asList(sampleWindow, new Window());
        when(windowManager.findAll()).thenReturn(Windows);

        // When & Then
        mockMvc.perform(get("/api/v1/windows"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(windowManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/windows - Retourner liste vide quand aucune Window")
    void getAllWindows_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(windowManager.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/windows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(windowManager, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/windows/{id} - Récupérer une Window par ID existant")
    void getWindowById_WhenExists_ShouldReturnWindow() throws Exception {
        // Given
        Integer id = 1;
        when(windowManager.findById(id)).thenReturn(sampleWindow);

        // When & Then
        mockMvc.perform(get("/api/v1/windows/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(windowManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/windows/{id} - Retourner 404 pour ID inexistant")
    void getWindowById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(windowManager.findById(id)).thenThrow(new IllegalArgumentException("Window not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/windows/{id}", id))
                .andExpect(status().isNotFound());

        verify(windowManager, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/windows/by-room/{roomId} - Récupérer Windows par ID de salle")
    void getByRoomId_ShouldReturnWindowsForRoom() throws Exception {
        // Given
        Long roomId = 1L;
        List<Window> roomWindows = Arrays.asList(sampleWindow);
        when(windowManager.findByRoomId(roomId)).thenReturn(roomWindows);

        // When & Then
        mockMvc.perform(get("/api/v1/windows/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(windowManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/windows/by-room/{roomId} - Retourner liste vide pour salle sans Windows")
    void getByRoomId_WhenNoWindows_ShouldReturnEmptyList() throws Exception {
        // Given
        Long roomId = 999L;
        when(windowManager.findByRoomId(roomId)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/windows/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(windowManager, times(1)).findByRoomId(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/windows/by-custom-name - Récupérer Windows par nom personnalisé")
    void getByCustomName_ShouldReturnWindowsWithCustomName() throws Exception {
        // Given
        String customName = "Table Test";
        List<Window> namedWindows = Arrays.asList(sampleWindow);
        when(windowManager.findByCustomName(customName)).thenReturn(namedWindows);

        // When & Then
        mockMvc.perform(get("/api/v1/windows/by-custom-name")
                .param("name", customName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(windowManager, times(1)).findByCustomName(customName);
    }

    @Test
    @DisplayName("POST /api/v1/windows - Créer une nouvelle Window")
    void createWindow_ShouldCreateAndReturnWindow() throws Exception {
        // Given
        Window newWindow = new Window();
        newWindow.setCustomName("Nouvelle Table");
        
        when(windowManager.save(any(Window.class))).thenReturn(sampleWindow);

        // When & Then
        mockMvc.perform(post("/api/v1/windows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWindow)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(windowManager, times(1)).save(any(Window.class));
    }

    @Test
    @DisplayName("PUT /api/v1/windows - Mettre à jour une Window existante")
    void updateWindow_ShouldUpdateAndReturnWindow() throws Exception {
        // Given
        Window updatedWindow = new Window();
        updatedWindow.setId(1);
        updatedWindow.setCustomName("Table Modifiée");
        
        when(windowManager.save(any(Window.class))).thenReturn(updatedWindow);

        // When & Then
        mockMvc.perform(put("/api/v1/windows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWindow)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customName").value("Table Modifiée"));

        verify(windowManager, times(1)).save(any(Window.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/windows/{id} - Supprimer une Window existante")
    void deleteWindow_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
        // Given
        Integer id = 1;
        when(windowManager.existsById(id)).thenReturn(true);
        doNothing().when(windowManager).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/windows/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Window supprimée avec succès"));

        verify(windowManager, times(1)).existsById(id);
        verify(windowManager, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/windows/{id} - Retourner 404 pour ID inexistant")
    void deleteWindow_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        Integer id = 999;
        when(windowManager.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/windows/{id}", id))
                .andExpect(status().isNotFound());

        verify(windowManager, times(1)).existsById(id);
        verify(windowManager, never()).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/windows/by-room/{roomId} - Supprimer toutes les Windows d'une salle")
    void deleteByRoomId_ShouldDeleteAllWindowsInRoom() throws Exception {
        // Given
        Integer roomId = 1;
        doNothing().when(windowManager).deleteByRoomId(roomId);

        // When & Then
        mockMvc.perform(delete("/api/v1/windows/by-room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Windows de la salle ont été supprimées"));

        verify(windowManager, times(1)).deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("DELETE /api/v1/windows/by-custom-name - Supprimer toutes les Windows avec un nom spécifique")
    void deleteByCustomName_ShouldDeleteAllWindowsWithCustomName() throws Exception {
        // Given
        String customName = "Table à supprimer";
        doNothing().when(windowManager).deleteByCustomName(customName);

        // When & Then
        mockMvc.perform(delete("/api/v1/windows/by-custom-name")
                .param("customName", customName))
                .andExpect(status().isOk())
                .andExpect(content().string("Toutes les Windows avec ce nom ont été supprimées"));

        verify(windowManager, times(1)).deleteByCustomName(customName);
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour by-custom-name")
    void getByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/windows/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(windowManager, never()).findByCustomName(anyString());
    }

    @Test
    @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
    void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/windows/by-custom-name"))
                .andExpect(status().isBadRequest());

        verify(windowManager, never()).deleteByCustomName(anyString());
    }
}