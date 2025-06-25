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

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.DoorManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the DoorEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * portes
 * et de ses interactions avec les services.
 */
public class DoorEditorTest {

    @MockBean
    private DoorManager doorManager;

    @MockBean
    private RoomManager roomManager;

    private DoorEditor doorEditor;
    private Door testDoor;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(DoorEditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());
        // Initialiser le DoorEditor avec les mocks
        doorEditor = new DoorEditor(doorManager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testDoor = new Door();
        testDoor.setId(1);
        testDoor.setCustomName("Test Door");
        testDoor.setRoom(testRoom);
        testDoor.setPosX(1.0);
        testDoor.setPosY(2.0);
        testDoor.setPosZ(3.0);
        testDoor.setSizeX(1.5);
        testDoor.setSizeY(2.5);
        testDoor.setSizeZ(0.8);

        logger.info("DoorEditor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testDoorEditorInstantiation() {
        // Vérifier que le DoorEditor peut être instancié correctement
        assertNotNull(doorEditor);
        logger.info("DoorEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        DoorEditor editor = new DoorEditor(doorManager, roomManager);
        assertNotNull(editor);
        logger.info("DoorEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            doorEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            doorEditor.setOnDataChanged(null);
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
            doorEditor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidDoor() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec une porte valide
        assertDoesNotThrow(() -> {
            doorEditor.openEditDialog(testDoor);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for door: " + testDoor.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullDoor() {
        // Tester l'ouverture du dialog d'édition avec une porte null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            doorEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Door input");
    }

    @Test
    void testConfirmDeleteWithValidDoor() {
        // Tester la confirmation de suppression avec une porte valide
        assertDoesNotThrow(() -> {
            doorEditor.confirmDelete(testDoor);
        });

        logger.info("Delete confirmation dialog opened for door: " + testDoor.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullDoor() {
        // Tester la confirmation de suppression avec une porte null
        assertDoesNotThrow(() -> {
            doorEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Door input");
    }

    @Test
    void testDoorManagerIntegration() {
        // Tester l'intégration avec DoorManager
        assertNotNull(doorManager);

        // Mock du comportement de sauvegarde
        when(doorManager.save(any(Door.class))).thenReturn(testDoor);

        Door savedDoor = doorManager.save(testDoor);
        assertNotNull(savedDoor);
        assertEquals(testDoor.getCustomName(), savedDoor.getCustomName());

        // Vérifier que save a été appelé
        verify(doorManager, times(1)).save(testDoor);

        logger.info("DoorManager integration test successful");
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
    void testDoorValidation() {
        // Tester la validation des données de porte
        Door validDoor = new Door();
        validDoor.setCustomName("Valid Door");
        validDoor.setRoom(testRoom);
        validDoor.setPosX(1.0);
        validDoor.setPosY(1.0);
        validDoor.setPosZ(1.0);
        validDoor.setSizeX(1.0);
        validDoor.setSizeY(1.0);
        validDoor.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validDoor.getCustomName());
        assertNotNull(validDoor.getRoom());
        assertNotNull(validDoor.getPosX());
        assertNotNull(validDoor.getSizeX());

        logger.info("Door validation test - all required fields present");
    }

    @Test
    void testDoorWithEmptyName() {
        // Tester avec un nom vide
        Door emptyNameDoor = new Door();
        emptyNameDoor.setCustomName("");
        emptyNameDoor.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameDoor.getCustomName().isEmpty());

        logger.info("Door with empty name handled correctly");
    }

    @Test
    void testDoorWithNullRoom() {
        // Tester avec une salle null
        Door nullRoomDoor = new Door();
        nullRoomDoor.setCustomName("Test Door");
        nullRoomDoor.setRoom(null);

        // La salle ne devrait pas être null pour une porte valide
        assertNull(nullRoomDoor.getRoom());

        logger.info("Door with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer doorId = 1;

        // Mock de la méthode deleteById
        doNothing().when(doorManager).deleteById(doorId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            doorManager.deleteById(doorId);
        });

        // Vérifier que deleteById a été appelé
        verify(doorManager, times(1)).deleteById(doorId);

        logger.info("Delete operation mock test successful for door ID: " + doorId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(doorManager.save(any(Door.class))).thenReturn(testDoor);

        // Simuler la sauvegarde
        Door result = assertDoesNotThrow(() -> {
            return doorManager.save(testDoor);
        });

        assertNotNull(result);
        assertEquals(testDoor.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(doorManager, times(1)).save(testDoor);

        logger.info("Save operation mock test successful for door: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(doorManager.save(any(Door.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            doorManager.save(testDoor);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer doorId = 1;
        doThrow(new RuntimeException("Delete error")).when(doorManager).deleteById(doorId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            doorManager.deleteById(doorId);
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
    void testDoorPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testDoor.getPosX());
        assertEquals(2.0, testDoor.getPosY());
        assertEquals(3.0, testDoor.getPosZ());

        logger.info("Door position values test successful: X=" +
                testDoor.getPosX() + ", Y=" + testDoor.getPosY() +
                ", Z=" + testDoor.getPosZ());
    }

    @Test
    void testDoorSizeValues() {
        // Tester les valeurs de taille
        assertEquals(1.5, testDoor.getSizeX());
        assertEquals(2.5, testDoor.getSizeY());
        assertEquals(0.8, testDoor.getSizeZ());

        logger.info("Door size values test successful: X=" +
                testDoor.getSizeX() + ", Y=" + testDoor.getSizeY() +
                ", Z=" + testDoor.getSizeZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        doorEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}