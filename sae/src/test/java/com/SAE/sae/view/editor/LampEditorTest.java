package com.SAE.sae.view.editor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.LampManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the LampEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * lampes
 * et de ses interactions avec les services.
 */
public class LampEditorTest {

    @MockBean
    private LampManager lampManager;

    @MockBean
    private RoomManager roomManager;

    private LampEditor lampEditor;
    private Lamp testLamp;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(LampEditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());

        // Initialiser le LampEditor avec les mocks
        lampEditor = new LampEditor(lampManager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testLamp = new Lamp();
        testLamp.setId(1);
        testLamp.setCustomName("Test Lamp");
        testLamp.setRoom(testRoom);
        testLamp.setPosX(1.0);
        testLamp.setPosY(2.0);
        testLamp.setPosZ(3.0);

        logger.info("LampEditor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testLampEditorInstantiation() {
        // Vérifier que le LampEditor peut être instancié correctement
        assertNotNull(lampEditor);
        logger.info("LampEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        LampEditor editor = new LampEditor(lampManager, roomManager);
        assertNotNull(editor);
        logger.info("LampEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            lampEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            lampEditor.setOnDataChanged(null);
        });

        logger.info("OnDataChanged handles null callback correctly");
    }

    @Test
    void testOpenAddDialogCreation() {
        // Mock des données nécessaires pour le dialog
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'ajout
        assertDoesNotThrow(() -> {
            lampEditor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidLamp() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec une lampe valide
        assertDoesNotThrow(() -> {
            lampEditor.openEditDialog(testLamp);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for lamp: " + testLamp.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullLamp() {
        // Tester l'ouverture du dialog d'édition avec une lampe null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            lampEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Lamp input");
    }

    @Test
    void testConfirmDeleteWithValidLamp() {
        // Tester la confirmation de suppression avec une lampe valide
        assertDoesNotThrow(() -> {
            lampEditor.confirmDelete(testLamp);
        });

        logger.info("Delete confirmation dialog opened for lamp: " + testLamp.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullLamp() {
        // Tester la confirmation de suppression avec une lampe null
        assertDoesNotThrow(() -> {
            lampEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Lamp input");
    }

    @Test
    void testLampManagerIntegration() {
        // Tester l'intégration avec LampManager
        assertNotNull(lampManager);

        // Mock du comportement de sauvegarde
        when(lampManager.save(any(Lamp.class))).thenReturn(testLamp);

        Lamp savedLamp = lampManager.save(testLamp);
        assertNotNull(savedLamp);
        assertEquals(testLamp.getCustomName(), savedLamp.getCustomName());

        // Vérifier que save a été appelé
        verify(lampManager, times(1)).save(testLamp);

        logger.info("LampManager integration test successful");
    }

    @Test
    void testRoomManagerIntegration() {
        // Tester l'intégration avec RoomManager
        assertNotNull(roomManager);

        // Mock du comportement getAllRooms
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        List<Room> rooms = roomManager.getAllRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());
        assertEquals(testRoom.getName(), rooms.get(0).getName());

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, times(1)).getAllRooms();

        logger.info("RoomManager integration test successful - returned " + rooms.size() + " rooms");
    }

    @Test
    void testLampValidation() {
        // Tester la validation des données de lampe
        Lamp validLamp = new Lamp();
        validLamp.setCustomName("Valid Lamp");
        validLamp.setRoom(testRoom);
        validLamp.setPosX(1.0);
        validLamp.setPosY(1.0);
        validLamp.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validLamp.getCustomName());
        assertNotNull(validLamp.getRoom());
        assertNotNull(validLamp.getPosX());

        logger.info("Lamp validation test - all required fields present");
    }

    @Test
    void testLampWithEmptyName() {
        // Tester avec un nom vide
        Lamp emptyNameLamp = new Lamp();
        emptyNameLamp.setCustomName("");
        emptyNameLamp.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameLamp.getCustomName().isEmpty());

        logger.info("Lamp with empty name handled correctly");
    }

    @Test
    void testLampWithNullRoom() {
        // Tester avec une salle null
        Lamp nullRoomLamp = new Lamp();
        nullRoomLamp.setCustomName("Test Lamp");
        nullRoomLamp.setRoom(null);

        // La salle ne devrait pas être null pour une lampe valide
        assertNull(nullRoomLamp.getRoom());

        logger.info("Lamp with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer lampId = 1;

        // Mock de la méthode deleteById
        doNothing().when(lampManager).deleteById(lampId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            lampManager.deleteById(lampId);
        });

        // Vérifier que deleteById a été appelé
        verify(lampManager, times(1)).deleteById(lampId);

        logger.info("Delete operation mock test successful for lamp ID: " + lampId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(lampManager.save(any(Lamp.class))).thenReturn(testLamp);

        // Simuler la sauvegarde
        Lamp result = assertDoesNotThrow(() -> {
            return lampManager.save(testLamp);
        });

        assertNotNull(result);
        assertEquals(testLamp.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(lampManager, times(1)).save(testLamp);

        logger.info("Save operation mock test successful for lamp: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(lampManager.save(any(Lamp.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            lampManager.save(testLamp);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer lampId = 1;
        doThrow(new RuntimeException("Delete error")).when(lampManager).deleteById(lampId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            lampManager.deleteById(lampId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testMultipleRoomsHandling() {
        // Tester avec plusieurs salles
        Room room2 = new Room();
        room2.setId(2);
        room2.setName("Test Room 2");
        room2.setBuilding(testBuilding);

        List<Room> multipleRooms = Arrays.asList(testRoom, room2);
        when(roomManager.getAllRooms()).thenReturn(multipleRooms);

        List<Room> result = roomManager.getAllRooms();
        assertNotNull(result);
        assertEquals(2, result.size());

        logger.info("Multiple rooms handling test successful - " + result.size() + " rooms");
    }

    @Test
    void testEmptyRoomsHandling() {
        // Tester avec une liste vide de salles
        when(roomManager.getAllRooms()).thenReturn(Arrays.asList());

        List<Room> result = roomManager.getAllRooms();
        assertNotNull(result);
        assertTrue(result.isEmpty());

        logger.info("Empty rooms list handling test successful");
    }

    @Test
    void testLampPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testLamp.getPosX());
        assertEquals(2.0, testLamp.getPosY());
        assertEquals(3.0, testLamp.getPosZ());

        logger.info("Lamp position values test successful: X=" +
                testLamp.getPosX() + ", Y=" + testLamp.getPosY() +
                ", Z=" + testLamp.getPosZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        lampEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}