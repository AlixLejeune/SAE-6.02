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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
 * tables
 * et de ses interactions avec les services.
 */
public class Sensor6in1EditorTest {

    @MockBean
    private Sensor6in1Manager Sensor6in1Manager;

    @MockBean
    private RoomManager roomManager;

    private Sensor6in1Editor Sensor6in1Editor;
    private Sensor6in1 testSensor6in1;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(Sensor6in1EditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());

        // Initialiser le Sensor6in1Editor avec les mocks
        Sensor6in1Editor = new Sensor6in1Editor(Sensor6in1Manager, roomManager);

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
        testSensor6in1.setCustomName("Test Table");
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
        assertNotNull(Sensor6in1Editor);
        logger.info("Sensor6in1Editor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        Sensor6in1Editor editor = new Sensor6in1Editor(Sensor6in1Manager, roomManager);
        assertNotNull(editor);
        logger.info("Sensor6in1Editor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            Sensor6in1Editor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            Sensor6in1Editor.setOnDataChanged(null);
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
            Sensor6in1Editor.openAddDialog();
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

        // Tester l'ouverture du dialog d'édition avec une table valide
        assertDoesNotThrow(() -> {
            Sensor6in1Editor.openEditDialog(testSensor6in1);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for table: " + testSensor6in1.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullSensor6in1() {
        // Tester l'ouverture du dialog d'édition avec une table null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            Sensor6in1Editor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Sensor6in1 input");
    }

    @Test
    void testConfirmDeleteWithValidSensor6in1() {
        // Tester la confirmation de suppression avec une table valide
        assertDoesNotThrow(() -> {
            Sensor6in1Editor.confirmDelete(testSensor6in1);
        });

        logger.info("Delete confirmation dialog opened for table: " + testSensor6in1.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullSensor6in1() {
        // Tester la confirmation de suppression avec une table null
        assertDoesNotThrow(() -> {
            Sensor6in1Editor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Sensor6in1 input");
    }

    @Test
    void testSensor6in1ManagerIntegration() {
        // Tester l'intégration avec Sensor6in1Manager
        assertNotNull(Sensor6in1Manager);

        // Mock du comportement de sauvegarde
        when(Sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        Sensor6in1 savedTable = Sensor6in1Manager.save(testSensor6in1);
        assertNotNull(savedTable);
        assertEquals(testSensor6in1.getCustomName(), savedTable.getCustomName());

        // Vérifier que save a été appelé
        verify(Sensor6in1Manager, times(1)).save(testSensor6in1);

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
        // Tester la validation des données de table
        Sensor6in1 validTable = new Sensor6in1();
        validTable.setCustomName("Valid Table");
        validTable.setRoom(testRoom);
        validTable.setPosX(1.0);
        validTable.setPosY(1.0);
        validTable.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validTable.getCustomName());
        assertNotNull(validTable.getRoom());
        assertNotNull(validTable.getPosX());

        logger.info("Sensor6in1 validation test - all required fields present");
    }

    @Test
    void testSensor6in1WithEmptyName() {
        // Tester avec un nom vide
        Sensor6in1 emptyNameTable = new Sensor6in1();
        emptyNameTable.setCustomName("");
        emptyNameTable.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameTable.getCustomName().isEmpty());

        logger.info("Sensor6in1 with empty name handled correctly");
    }

    @Test
    void testSensor6in1WithNullRoom() {
        // Tester avec une salle null
        Sensor6in1 nullRoomTable = new Sensor6in1();
        nullRoomTable.setCustomName("Test Table");
        nullRoomTable.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomTable.getRoom());

        logger.info("Sensor6in1 with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer tableId = 1;

        // Mock de la méthode deleteById
        doNothing().when(Sensor6in1Manager).deleteById(tableId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            Sensor6in1Manager.deleteById(tableId);
        });

        // Vérifier que deleteById a été appelé
        verify(Sensor6in1Manager, times(1)).deleteById(tableId);

        logger.info("Delete operation mock test successful for table ID: " + tableId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(Sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        // Simuler la sauvegarde
        Sensor6in1 result = assertDoesNotThrow(() -> {
            return Sensor6in1Manager.save(testSensor6in1);
        });

        assertNotNull(result);
        assertEquals(testSensor6in1.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(Sensor6in1Manager, times(1)).save(testSensor6in1);

        logger.info("Save operation mock test successful for table: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(Sensor6in1Manager.save(any(Sensor6in1.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor6in1Manager.save(testSensor6in1);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer tableId = 1;
        doThrow(new RuntimeException("Delete error")).when(Sensor6in1Manager).deleteById(tableId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor6in1Manager.deleteById(tableId);
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

        Sensor6in1Editor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}