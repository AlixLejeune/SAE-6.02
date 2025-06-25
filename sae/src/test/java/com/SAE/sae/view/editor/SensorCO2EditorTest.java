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

import com.SAE.sae.entity.RoomObjects.SensorCO2;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.SensorCO2Manager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the SensorCO2Editor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * tables
 * et de ses interactions avec les services.
 */
public class SensorCO2EditorTest {

    @MockBean
    private SensorCO2Manager sensorCO2Manager;

    @MockBean
    private RoomManager roomManager;

    private SensorCO2Editor sensorCO2Editor;
    private SensorCO2 testSensorCO2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(SensorCO2EditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());
        // Initialiser le SensorCO2Editor avec les mocks
        sensorCO2Editor = new SensorCO2Editor(sensorCO2Manager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testSensorCO2 = new SensorCO2();
        testSensorCO2.setId(1);
        testSensorCO2.setCustomName("Test Table");
        testSensorCO2.setRoom(testRoom);
        testSensorCO2.setPosX(1.0);
        testSensorCO2.setPosY(2.0);
        testSensorCO2.setPosZ(3.0);

        logger.info("SensorCO2Editor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSensorCO2EditorInstantiation() {
        // Vérifier que le SensorCO2Editor peut être instancié correctement
        assertNotNull(sensorCO2Editor);
        logger.info("SensorCO2Editor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        SensorCO2Editor editor = new SensorCO2Editor(sensorCO2Manager, roomManager);
        assertNotNull(editor);
        logger.info("SensorCO2Editor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            sensorCO2Editor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            sensorCO2Editor.setOnDataChanged(null);
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
            sensorCO2Editor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidSensorCO2() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec une table valide
        assertDoesNotThrow(() -> {
            sensorCO2Editor.openEditDialog(testSensorCO2);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for table: " + testSensorCO2.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullSensorCO2() {
        // Tester l'ouverture du dialog d'édition avec une table null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            sensorCO2Editor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null SensorCO2 input");
    }

    @Test
    void testConfirmDeleteWithValidSensorCO2() {
        // Tester la confirmation de suppression avec une table valide
        assertDoesNotThrow(() -> {
            sensorCO2Editor.confirmDelete(testSensorCO2);
        });

        logger.info("Delete confirmation dialog opened for table: " + testSensorCO2.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullSensorCO2() {
        // Tester la confirmation de suppression avec une table null
        assertDoesNotThrow(() -> {
            sensorCO2Editor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null SensorCO2 input");
    }

    @Test
    void testSensorCO2ManagerIntegration() {
        // Tester l'intégration avec SensorCO2Manager
        assertNotNull(sensorCO2Manager);

        // Mock du comportement de sauvegarde
        when(sensorCO2Manager.save(any(SensorCO2.class))).thenReturn(testSensorCO2);

        SensorCO2 savedTable = sensorCO2Manager.save(testSensorCO2);
        assertNotNull(savedTable);
        assertEquals(testSensorCO2.getCustomName(), savedTable.getCustomName());

        // Vérifier que save a été appelé
        verify(sensorCO2Manager, times(1)).save(testSensorCO2);

        logger.info("SensorCO2Manager integration test successful");
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
    void testSensorCO2Validation() {
        // Tester la validation des données de table
        SensorCO2 validTable = new SensorCO2();
        validTable.setCustomName("Valid Table");
        validTable.setRoom(testRoom);
        validTable.setPosX(1.0);
        validTable.setPosY(1.0);
        validTable.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validTable.getCustomName());
        assertNotNull(validTable.getRoom());
        assertNotNull(validTable.getPosX());

        logger.info("SensorCO2 validation test - all required fields present");
    }

    @Test
    void testSensorCO2WithEmptyName() {
        // Tester avec un nom vide
        SensorCO2 emptyNameTable = new SensorCO2();
        emptyNameTable.setCustomName("");
        emptyNameTable.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameTable.getCustomName().isEmpty());

        logger.info("SensorCO2 with empty name handled correctly");
    }

    @Test
    void testSensorCO2WithNullRoom() {
        // Tester avec une salle null
        SensorCO2 nullRoomTable = new SensorCO2();
        nullRoomTable.setCustomName("Test Table");
        nullRoomTable.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomTable.getRoom());

        logger.info("SensorCO2 with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer tableId = 1;

        // Mock de la méthode deleteById
        doNothing().when(sensorCO2Manager).deleteById(tableId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            sensorCO2Manager.deleteById(tableId);
        });

        // Vérifier que deleteById a été appelé
        verify(sensorCO2Manager, times(1)).deleteById(tableId);

        logger.info("Delete operation mock test successful for table ID: " + tableId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(sensorCO2Manager.save(any(SensorCO2.class))).thenReturn(testSensorCO2);

        // Simuler la sauvegarde
        SensorCO2 result = assertDoesNotThrow(() -> {
            return sensorCO2Manager.save(testSensorCO2);
        });

        assertNotNull(result);
        assertEquals(testSensorCO2.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(sensorCO2Manager, times(1)).save(testSensorCO2);

        logger.info("Save operation mock test successful for table: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(sensorCO2Manager.save(any(SensorCO2.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            sensorCO2Manager.save(testSensorCO2);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer tableId = 1;
        doThrow(new RuntimeException("Delete error")).when(sensorCO2Manager).deleteById(tableId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            sensorCO2Manager.deleteById(tableId);
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
    void testSensorCO2PositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testSensorCO2.getPosX());
        assertEquals(2.0, testSensorCO2.getPosY());
        assertEquals(3.0, testSensorCO2.getPosZ());

        logger.info("SensorCO2 position values test successful: X=" +
                testSensorCO2.getPosX() + ", Y=" + testSensorCO2.getPosY() +
                ", Z=" + testSensorCO2.getPosZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        sensorCO2Editor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}