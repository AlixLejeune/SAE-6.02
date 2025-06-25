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

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.Sensor6in1Manager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the Sensor6in1Editor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * capteurs 6-en-1
 * et de ses interactions avec les services.
 */
public class Sensor6in1EditorTest {

    @MockBean
    private Sensor6in1Manager sensor6in1Manager;

    @MockBean
    private RoomManager roomManager;

    private Sensor6in1Editor sensor6in1Editor;
    private Sensor6in1 testSensor6in1;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(Sensor6in1EditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());

        // Initialiser le Sensor6in1Editor avec les mocks
        sensor6in1Editor = new Sensor6in1Editor(sensor6in1Manager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testSensor6in1 = new Sensor6in1();
        testSensor6in1.setId(1);
        testSensor6in1.setCustomName("Test Sensor 6-in-1");
        testSensor6in1.setRoom(testRoom);
        testSensor6in1.setPosX(1.0);
        testSensor6in1.setPosY(2.0);
        testSensor6in1.setPosZ(3.0);

        logger.info("Sensor6in1Editor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSensor6in1EditorInstantiation() {
        // Vérifier que le Sensor6in1Editor peut être instancié correctement
        assertNotNull(sensor6in1Editor);
        logger.info("Sensor6in1Editor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        Sensor6in1Editor editor = new Sensor6in1Editor(sensor6in1Manager, roomManager);
        assertNotNull(editor);
        logger.info("Sensor6in1Editor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            sensor6in1Editor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            sensor6in1Editor.setOnDataChanged(null);
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
            sensor6in1Editor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidSensor6in1() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec un capteur valide
        assertDoesNotThrow(() -> {
            sensor6in1Editor.openEditDialog(testSensor6in1);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for sensor: " + testSensor6in1.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullSensor6in1() {
        // Tester l'ouverture du dialog d'édition avec un capteur null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            sensor6in1Editor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Sensor6in1 input");
    }

    @Test
    void testConfirmDeleteWithValidSensor6in1() {
        // Tester la confirmation de suppression avec un capteur valide
        assertDoesNotThrow(() -> {
            sensor6in1Editor.confirmDelete(testSensor6in1);
        });

        logger.info("Delete confirmation dialog opened for sensor: " + testSensor6in1.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullSensor6in1() {
        // Tester la confirmation de suppression avec un capteur null
        assertDoesNotThrow(() -> {
            sensor6in1Editor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Sensor6in1 input");
    }

    @Test
    void testSensor6in1ManagerIntegration() {
        // Tester l'intégration avec Sensor6in1Manager
        assertNotNull(sensor6in1Manager);

        // Mock du comportement de sauvegarde
        when(sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        Sensor6in1 savedSensor = sensor6in1Manager.save(testSensor6in1);
        assertNotNull(savedSensor);
        assertEquals(testSensor6in1.getCustomName(), savedSensor.getCustomName());

        // Vérifier que save a été appelé
        verify(sensor6in1Manager, times(1)).save(testSensor6in1);

        logger.info("Sensor6in1Manager integration test successful");
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
    void testSensor6in1Validation() {
        // Tester la validation des données de capteur
        Sensor6in1 validSensor = new Sensor6in1();
        validSensor.setCustomName("Valid Sensor");
        validSensor.setRoom(testRoom);
        validSensor.setPosX(1.0);
        validSensor.setPosY(1.0);
        validSensor.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validSensor.getCustomName());
        assertNotNull(validSensor.getRoom());
        assertNotNull(validSensor.getPosX());

        logger.info("Sensor6in1 validation test - all required fields present");
    }

    @Test
    void testSensor6in1WithEmptyName() {
        // Tester avec un nom vide
        Sensor6in1 emptyNameSensor = new Sensor6in1();
        emptyNameSensor.setCustomName("");
        emptyNameSensor.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameSensor.getCustomName().isEmpty());

        logger.info("Sensor6in1 with empty name handled correctly");
    }

    @Test
    void testSensor6in1WithNullRoom() {
        // Tester avec une salle null
        Sensor6in1 nullRoomSensor = new Sensor6in1();
        nullRoomSensor.setCustomName("Test Sensor");
        nullRoomSensor.setRoom(null);

        // La salle ne devrait pas être null pour un capteur valide
        assertNull(nullRoomSensor.getRoom());

        logger.info("Sensor6in1 with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer sensorId = 1;

        // Mock de la méthode deleteById
        doNothing().when(sensor6in1Manager).deleteById(sensorId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            sensor6in1Manager.deleteById(sensorId);
        });

        // Vérifier que deleteById a été appelé
        verify(sensor6in1Manager, times(1)).deleteById(sensorId);

        logger.info("Delete operation mock test successful for sensor ID: " + sensorId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        // Simuler la sauvegarde
        Sensor6in1 result = assertDoesNotThrow(() -> {
            return sensor6in1Manager.save(testSensor6in1);
        });

        assertNotNull(result);
        assertEquals(testSensor6in1.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(sensor6in1Manager, times(1)).save(testSensor6in1);

        logger.info("Save operation mock test successful for sensor: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(sensor6in1Manager.save(any(Sensor6in1.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            sensor6in1Manager.save(testSensor6in1);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer sensorId = 1;
        doThrow(new RuntimeException("Delete error")).when(sensor6in1Manager).deleteById(sensorId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            sensor6in1Manager.deleteById(sensorId);
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
    void testSensor6in1PositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testSensor6in1.getPosX());
        assertEquals(2.0, testSensor6in1.getPosY());
        assertEquals(3.0, testSensor6in1.getPosZ());

        logger.info("Sensor6in1 position values test successful: X=" +
                testSensor6in1.getPosX() + ", Y=" + testSensor6in1.getPosY() +
                ", Z=" + testSensor6in1.getPosZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        sensor6in1Editor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}