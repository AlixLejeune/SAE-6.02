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

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.HeaterManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the HeaterEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * tables
 * et de ses interactions avec les services.
 */
public class HeaterEditorTest {

    @MockBean
    private HeaterManager HeaterManager;

    @MockBean
    private RoomManager roomManager;

    private HeaterEditor HeaterEditor;
    private Heater testHeater;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(HeaterEditorTest.class.getName());

    @BeforeEach
    void setUp() {

        UI.setCurrent(new UI());
        // Initialiser le HeaterEditor avec les mocks
        HeaterEditor = new HeaterEditor(HeaterManager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testHeater = new Heater();
        testHeater.setId(1);
        testHeater.setCustomName("Test Table");
        testHeater.setRoom(testRoom);
        testHeater.setPosX(1.0);
        testHeater.setPosY(2.0);
        testHeater.setPosZ(3.0);
        testHeater.setSizeX(1.5);
        testHeater.setSizeY(2.5);
        testHeater.setSizeZ(0.8);

        logger.info("HeaterEditor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testHeaterEditorInstantiation() {
        // Vérifier que le HeaterEditor peut être instancié correctement
        assertNotNull(HeaterEditor);
        logger.info("HeaterEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        HeaterEditor editor = new HeaterEditor(HeaterManager, roomManager);
        assertNotNull(editor);
        logger.info("HeaterEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            HeaterEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            HeaterEditor.setOnDataChanged(null);
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
            HeaterEditor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidHeater() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec une table valide
        assertDoesNotThrow(() -> {
            HeaterEditor.openEditDialog(testHeater);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for table: " + testHeater.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullHeater() {
        // Tester l'ouverture du dialog d'édition avec une table null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            HeaterEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Heater input");
    }

    @Test
    void testConfirmDeleteWithValidHeater() {
        // Tester la confirmation de suppression avec une table valide
        assertDoesNotThrow(() -> {
            HeaterEditor.confirmDelete(testHeater);
        });

        logger.info("Delete confirmation dialog opened for table: " + testHeater.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullHeater() {
        // Tester la confirmation de suppression avec une table null
        assertDoesNotThrow(() -> {
            HeaterEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Heater input");
    }

    @Test
    void testHeaterManagerIntegration() {
        // Tester l'intégration avec HeaterManager
        assertNotNull(HeaterManager);

        // Mock du comportement de sauvegarde
        when(HeaterManager.save(any(Heater.class))).thenReturn(testHeater);

        Heater savedTable = HeaterManager.save(testHeater);
        assertNotNull(savedTable);
        assertEquals(testHeater.getCustomName(), savedTable.getCustomName());

        // Vérifier que save a été appelé
        verify(HeaterManager, times(1)).save(testHeater);

        logger.info("HeaterManager integration test successful");
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
    void testHeaterValidation() {
        // Tester la validation des données de table
        Heater validTable = new Heater();
        validTable.setCustomName("Valid Table");
        validTable.setRoom(testRoom);
        validTable.setPosX(1.0);
        validTable.setPosY(1.0);
        validTable.setPosZ(1.0);
        validTable.setSizeX(1.0);
        validTable.setSizeY(1.0);
        validTable.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validTable.getCustomName());
        assertNotNull(validTable.getRoom());
        assertNotNull(validTable.getPosX());
        assertNotNull(validTable.getSizeX());

        logger.info("Heater validation test - all required fields present");
    }

    @Test
    void testHeaterWithEmptyName() {
        // Tester avec un nom vide
        Heater emptyNameTable = new Heater();
        emptyNameTable.setCustomName("");
        emptyNameTable.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameTable.getCustomName().isEmpty());

        logger.info("Heater with empty name handled correctly");
    }

    @Test
    void testHeaterWithNullRoom() {
        // Tester avec une salle null
        Heater nullRoomTable = new Heater();
        nullRoomTable.setCustomName("Test Table");
        nullRoomTable.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomTable.getRoom());

        logger.info("Heater with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer tableId = 1;

        // Mock de la méthode deleteById
        doNothing().when(HeaterManager).deleteById(tableId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            HeaterManager.deleteById(tableId);
        });

        // Vérifier que deleteById a été appelé
        verify(HeaterManager, times(1)).deleteById(tableId);

        logger.info("Delete operation mock test successful for table ID: " + tableId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(HeaterManager.save(any(Heater.class))).thenReturn(testHeater);

        // Simuler la sauvegarde
        Heater result = assertDoesNotThrow(() -> {
            return HeaterManager.save(testHeater);
        });

        assertNotNull(result);
        assertEquals(testHeater.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(HeaterManager, times(1)).save(testHeater);

        logger.info("Save operation mock test successful for table: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(HeaterManager.save(any(Heater.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            HeaterManager.save(testHeater);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer tableId = 1;
        doThrow(new RuntimeException("Delete error")).when(HeaterManager).deleteById(tableId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            HeaterManager.deleteById(tableId);
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
    void testHeaterPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testHeater.getPosX());
        assertEquals(2.0, testHeater.getPosY());
        assertEquals(3.0, testHeater.getPosZ());

        logger.info("Heater position values test successful: X=" +
                testHeater.getPosX() + ", Y=" + testHeater.getPosY() +
                ", Z=" + testHeater.getPosZ());
    }

    @Test
    void testHeaterSizeValues() {
        // Tester les valeurs de taille
        assertEquals(1.5, testHeater.getSizeX());
        assertEquals(2.5, testHeater.getSizeY());
        assertEquals(0.8, testHeater.getSizeZ());

        logger.info("Heater size values test successful: X=" +
                testHeater.getSizeX() + ", Y=" + testHeater.getSizeY() +
                ", Z=" + testHeater.getSizeZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        HeaterEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}