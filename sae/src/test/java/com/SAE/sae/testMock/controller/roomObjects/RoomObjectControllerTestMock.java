package com.SAE.sae.testMock.controller.roomObjects;
// package com.SAE.sae.controller.RoomObjects;

// import com.SAE.sae.entity.RoomObjects.RoomObject;
// import com.SAE.sae.repository.RoomObjects.RoomObjectRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @ExtendWith(MockitoExtension.class)
// @DisplayName("Tests pour RoomObjectController")
// class RoomObjectControllerTest {

//     @Mock
//     private RoomObjectRepository roomObjectRepository;

//     @InjectMocks
//     private RoomObjectController roomObjectController;

//     private MockMvc mockMvc;
//     private ObjectMapper objectMapper;
//     private RoomObject sampleRoomObject;

//     @BeforeEach
//     void setUp() {
//         mockMvc = MockMvcBuilders.standaloneSetup(roomObjectController).build();
//         objectMapper = new ObjectMapper();
        
//         // Création d'un objet RoomObject d'exemple
//         // Note: RoomObject étant abstract, nous devons créer une implémentation concrète pour les tests
//         sampleRoomObject = new ConcreteRoomObject();
//         sampleRoomObject.setId(1);
//         sampleRoomObject.setCustomName("Objet Test");
//         // La relation Room est annotée @JsonIgnore, donc pas besoin de la mocker pour les tests JSON
//     }
    
//     // Classe concrète pour les tests (RoomObject étant abstract)
//     private static class ConcreteRoomObject extends RoomObject {
//         // Implémentation concrète minimale pour les tests
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects - Récupérer tous les RoomObjects")
//     void getAllRoomObjects_ShouldReturnAllRoomObjects() throws Exception {
//         // Given
//         List<RoomObject> roomObjects = Arrays.asList(sampleRoomObject, new ConcreteRoomObject());
//         when(roomObjectRepository.findAll()).thenReturn(roomObjects);

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.length()").value(2));

//         verify(roomObjectRepository, times(1)).findAll();
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects - Retourner liste vide quand aucun RoomObject")
//     void getAllRoomObjects_WhenEmpty_ShouldReturnEmptyList() throws Exception {
//         // Given
//         when(roomObjectRepository.findAll()).thenReturn(Arrays.asList());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.length()").value(0));

//         verify(roomObjectRepository, times(1)).findAll();
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/{id} - Récupérer un RoomObject par ID existant")
//     void getRoomObjectById_WhenExists_ShouldReturnRoomObject() throws Exception {
//         // Given
//         Integer id = 1;
//         when(roomObjectRepository.findById(id)).thenReturn(Optional.of(sampleRoomObject));

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/{id}", id))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.customName").value("Objet Test"));

//         verify(roomObjectRepository, times(1)).findById(id);
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/{id} - Retourner 404 pour ID inexistant")
//     void getRoomObjectById_WhenNotExists_ShouldReturn404() throws Exception {
//         // Given
//         Integer id = 999;
//         when(roomObjectRepository.findById(id)).thenReturn(Optional.empty());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/{id}", id))
//                 .andExpect(status().isNotFound());

//         verify(roomObjectRepository, times(1)).findById(id);
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/by-room/{roomId} - Récupérer RoomObjects par ID de salle")
//     void getObjectsByRoomId_ShouldReturnRoomObjectsForRoom() throws Exception {
//         // Given
//         Long roomId = 1L;
//         List<RoomObject> roomObjects = Arrays.asList(sampleRoomObject);
//         when(roomObjectRepository.findByRoom_Id(roomId)).thenReturn(roomObjects);

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-room/{roomId}", roomId))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.length()").value(1))
//                 .andExpect(jsonPath("$[0].customName").value("Objet Test"));

//         verify(roomObjectRepository, times(1)).findByRoom_Id(roomId);
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/by-room/{roomId} - Retourner liste vide pour salle sans objets")
//     void getObjectsByRoomId_WhenNoObjects_ShouldReturnEmptyList() throws Exception {
//         // Given
//         Long roomId = 999L;
//         when(roomObjectRepository.findByRoom_Id(roomId)).thenReturn(Arrays.asList());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-room/{roomId}", roomId))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.length()").value(0));

//         verify(roomObjectRepository, times(1)).findByRoom_Id(roomId);
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/by-custom-name - Récupérer RoomObjects par nom personnalisé")
//     void getObjectsByCustomName_ShouldReturnRoomObjectsWithCustomName() throws Exception {
//         // Given
//         String customName = "Objet Test";
//         List<RoomObject> namedObjects = Arrays.asList(sampleRoomObject);
//         when(roomObjectRepository.findByCustomName(customName)).thenReturn(namedObjects);

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-custom-name")
//                 .param("name", customName))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.length()").value(1))
//                 .andExpect(jsonPath("$[0].customName").value(customName));

//         verify(roomObjectRepository, times(1)).findByCustomName(customName);
//     }

//     @Test
//     @DisplayName("GET /api/v1/room-objects/by-custom-name - Retourner liste vide pour nom inexistant")
//     void getObjectsByCustomName_WhenNoObjects_ShouldReturnEmptyList() throws Exception {
//         // Given
//         String customName = "Nom inexistant";
//         when(roomObjectRepository.findByCustomName(customName)).thenReturn(Arrays.asList());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-custom-name")
//                 .param("name", customName))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.length()").value(0));

//         verify(roomObjectRepository, times(1)).findByCustomName(customName);
//     }

//     @Test
//     @DisplayName("POST /api/v1/room-objects - Créer un nouveau RoomObject")
//     void createRoomObject_ShouldCreateAndReturnRoomObject() throws Exception {
//         // Given
//         RoomObject newRoomObject = new ConcreteRoomObject();
//         newRoomObject.setCustomName("Nouvel Objet");
        
//         when(roomObjectRepository.save(any(RoomObject.class))).thenReturn(sampleRoomObject);

//         // When & Then
//         mockMvc.perform(post("/api/v1/room-objects")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(newRoomObject)))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.customName").value("Objet Test"));

//         verify(roomObjectRepository, times(1)).save(any(RoomObject.class));
//     }

//     // @Test
//     // @DisplayName("PUT /api/v1/room-objects - Mettre à jour un RoomObject existant")
//     // void updateRoomObject_ShouldUpdateAndReturnRoomObject() throws Exception {
//     //     // Given
//     //     RoomObject updatedRoomObject = new RoomObject();
//     //     updatedRoomObject.setId(1);
//     //     updatedRoomObject.setCustomName("Objet Modifié");
        
//     //     when(roomObjectRepository.save(any(RoomObject.class))).thenReturn(updatedRoomObject);

//     //     // When & Then
//     //     mockMvc.perform(put("/api/v1/room-objects")
//     //             .contentType(MediaType.APPLICATION_JSON)
//     //             .content(objectMapper.writeValueAsString(updatedRoomObject)))
//     //             .andExpect(status().isOk())
//     //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//     //             .andExpect(jsonPath("$.id").value(1))
//     //             .andExpect(jsonPath("$.customName").value("Objet Modifié"));

//     //     verify(roomObjectRepository, times(1)).save(any(RoomObject.class));
//     // }

//     @Test
//     @DisplayName("DELETE /api/v1/room-objects/{id} - Supprimer un RoomObject existant")
//     void deleteRoomObject_WhenExists_ShouldDeleteAndReturnSuccess() throws Exception {
//         // Given
//         Integer id = 1;
//         when(roomObjectRepository.existsById(id)).thenReturn(true);
//         doNothing().when(roomObjectRepository).deleteById(id);

//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/{id}", id))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Objet supprimé avec succès"));

//         verify(roomObjectRepository, times(1)).existsById(id);
//         verify(roomObjectRepository, times(1)).deleteById(id);
//     }

//     @Test
//     @DisplayName("DELETE /api/v1/room-objects/{id} - Retourner 404 pour ID inexistant")
//     void deleteRoomObject_WhenNotExists_ShouldReturn404() throws Exception {
//         // Given
//         Integer id = 999;
//         when(roomObjectRepository.existsById(id)).thenReturn(false);

//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/{id}", id))
//                 .andExpect(status().isNotFound());

//         verify(roomObjectRepository, times(1)).existsById(id);
//         verify(roomObjectRepository, never()).deleteById(id);
//     }

//     @Test
//     @DisplayName("DELETE /api/v1/room-objects/by-room/{roomId} - Supprimer tous les RoomObjects d'une salle")
//     void deleteByRoomId_ShouldDeleteAllRoomObjectsInRoom() throws Exception {
//         // Given
//         Integer roomId = 1;
//         doNothing().when(roomObjectRepository).deleteByRoomId(roomId);

//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/by-room/{roomId}", roomId))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Toutes les RoomObjects de la salle ont été supprimées"));

//         verify(roomObjectRepository, times(1)).deleteByRoomId(roomId);
//     }

//     @Test
//     @DisplayName("DELETE /api/v1/room-objects/by-custom-name - Supprimer tous les RoomObjects avec un nom spécifique")
//     void deleteByCustomName_ShouldDeleteAllRoomObjectsWithCustomName() throws Exception {
//         // Given
//         String customName = "Objet à supprimer";
//         doNothing().when(roomObjectRepository).deleteByCustomName(customName);

//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/by-custom-name")
//                 .param("customName", customName))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Objets avec ce nom supprimés"));

//         verify(roomObjectRepository, times(1)).deleteByCustomName(customName);
//     }

//     @Test
//     @DisplayName("Test de validation - Paramètre manquant pour by-custom-name GET")
//     void getObjectsByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-custom-name"))
//                 .andExpect(status().isBadRequest());

//         verify(roomObjectRepository, never()).findByCustomName(anyString());
//     }

//     @Test
//     @DisplayName("Test de validation - Paramètre manquant pour delete by-custom-name")
//     void deleteByCustomName_WithoutNameParameter_ShouldReturnBadRequest() throws Exception {
//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/by-custom-name"))
//                 .andExpect(status().isBadRequest());

//         verify(roomObjectRepository, never()).deleteByCustomName(anyString());
//     }

//     @Test
//     @DisplayName("Test avec données null - Création d'un RoomObject avec champs null")
//     void createRoomObject_WithNullFields_ShouldHandleGracefully() throws Exception {
//         // Given
//         RoomObject nullFieldsObject = new ConcreteRoomObject();
//         // Pas de setCustomName - reste null
        
//         when(roomObjectRepository.save(any(RoomObject.class))).thenReturn(nullFieldsObject);

//         // When & Then
//         mockMvc.perform(post("/api/v1/room-objects")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(nullFieldsObject)))
//                 .andExpect(status().isOk());

//         verify(roomObjectRepository, times(1)).save(any(RoomObject.class));
//     }

//     @Test
//     @DisplayName("Test de performance - Recherche avec nom vide")
//     void getObjectsByCustomName_WithEmptyName_ShouldWork() throws Exception {
//         // Given
//         String emptyName = "";
//         when(roomObjectRepository.findByCustomName(emptyName)).thenReturn(Arrays.asList());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/by-custom-name")
//                 .param("name", emptyName))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.length()").value(0));

//         verify(roomObjectRepository, times(1)).findByCustomName(emptyName);
//     }

//     @Test
//     @DisplayName("Test avec ID négatif")
//     void getRoomObjectById_WithNegativeId_ShouldHandleGracefully() throws Exception {
//         // Given
//         Integer negativeId = -1;
//         when(roomObjectRepository.findById(negativeId)).thenReturn(Optional.empty());

//         // When & Then
//         mockMvc.perform(get("/api/v1/room-objects/{id}", negativeId))
//                 .andExpect(status().isNotFound());

//         verify(roomObjectRepository, times(1)).findById(negativeId);
//     }

//     @Test
//     @DisplayName("Test transactionnel - Suppression par roomId avec exception")
//     void deleteByRoomId_WhenRepositoryThrowsException_ShouldPropagateException() throws Exception {
//         // Given
//         Integer roomId = 1;
//         doThrow(new RuntimeException("Database error")).when(roomObjectRepository).deleteByRoomId(roomId);

//         // When & Then
//         mockMvc.perform(delete("/api/v1/room-objects/by-room/{roomId}", roomId))
//                 .andExpect(status().isInternalServerError());

//         verify(roomObjectRepository, times(1)).deleteByRoomId(roomId);
//     }
// }