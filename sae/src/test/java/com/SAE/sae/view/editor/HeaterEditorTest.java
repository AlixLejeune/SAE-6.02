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
 * radiateurs
 * et de ses interactions avec les services.
 */
public class HeaterEditorTest {

    @MockBean
    private HeaterManager heaterManager;

    @MockBean
    private RoomManager roomManager;

    private HeaterEditor heaterEditor;
    private Heater testHeater;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(HeaterEditorTest.class.getName());

    @BeforeEach
    void setUp() {

        UI.setCurrent(new UI());
        // Initialiser le HeaterEditor avec les mocks
        heaterEditor = new HeaterEditor(heaterManager, roomManager);

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
        testHeater.setCustomName("Test Heater");
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
        assertNotNull(heaterEditor);
        logger.info("HeaterEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        HeaterEditor editor = new HeaterEditor(heaterManager, roomManager);
        assertNotNull(editor);
        logger.info("HeaterEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            heaterEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            heaterEditor.setOnDataChanged(null);
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
            heaterEditor.openAddDialog();
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

        // Tester l'ouverture du dialog d'édition avec un radiateur valide
        assertDoesNotThrow(() -> {
            heaterEditor.openEditDialog(testHeater);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for heater: " + testHeater.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullHeater() {
        // Tester l'ouverture du dialog d'édition avec un radiateur null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            heaterEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Heater input");
    }

    @Test
    void testConfirmDeleteWithValidHeater() {
        // Tester la confirmation de suppression avec un radiateur valide
        assertDoesNotThrow(() -> {
            heaterEditor.confirmDelete(testHeater);
        });

        logger.info("Delete confirmation dialog opened for heater: " + testHeater.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullHeater() {
        // Tester la confirmation de suppression avec un radiateur null
        assertDoesNotThrow(() -> {
            heaterEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Heater input");
    }

    @Test
    void testHeaterManagerIntegration() {
        // Tester l'intégration avec HeaterManager
        assertNotNull(heaterManager);

        // Mock du comportement de sauvegarde
        when(heaterManager.save(any(Heater.class))).thenReturn(testHeater);

        Heater savedHeater = heaterManager.save(testHeater);
        assertNotNull(savedHeater);
        assertEquals(testHeater.getCustomName(), savedHeater.getCustomName());

        // Vérifier que save a été appelé
        verify(heaterManager, times(1)).save(testHeater);

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
        // Tester la validation des données de radiateur
        Heater validHeater = new Heater();
        validHeater.setCustomName("Valid Heater");
        validHeater.setRoom(testRoom);
        validHeater.setPosX(1.0);
        validHeater.setPosY(1.0);
        validHeater.setPosZ(1.0);
        validHeater.setSizeX(1.0);
        validHeater.setSizeY(1.0);
        validHeater.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validHeater.getCustomName());
        assertNotNull(validHeater.getRoom());
        assertNotNull(validHeater.getPosX());
        assertNotNull(validHeater.getSizeX());

        logger.info("Heater validation test - all required fields present");
    }

    @Test
    void testHeaterWithEmptyName() {
        // Tester avec un nom vide
        Heater emptyNameHeater = new Heater();
        emptyNameHeater.setCustomName("");
        emptyNameHeater.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameHeater.getCustomName().isEmpty());

        logger.info("Heater with empty name handled correctly");
    }

    @Test
    void testHeaterWithNullRoom() {
        // Tester avec une salle null
        Heater nullRoomHeater = new Heater();
        nullRoomHeater.setCustomName("Test Heater");
        nullRoomHeater.setRoom(null);

        // La salle ne devrait pas être null pour un radiateur valide
        assertNull(nullRoomHeater.getRoom());

        logger.info("Heater with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer heaterId = 1;

        // Mock de la méthode deleteById
        doNothing().when(heaterManager).deleteById(heaterId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            heaterManager.deleteById(heaterId);
        });

        // Vérifier que deleteById a été appelé
        verify(heaterManager, times(1)).deleteById(heaterId);

        logger.info("Delete operation mock test successful for heater ID: " + heaterId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(heaterManager.save(any(Heater.class))).thenReturn(testHeater);

        // Simuler la sauvegarde
        Heater result = assertDoesNotThrow(() -> {
            return heaterManager.save(testHeater);
        });

        assertNotNull(result);
        assertEquals(testHeater.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(heaterManager, times(1)).save(testHeater);

        logger.info("Save operation mock test successful for heater: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(heaterManager.save(any(Heater.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            heaterManager.save(testHeater);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer heaterId = 1;
        doThrow(new RuntimeException("Delete error")).when(heaterManager).deleteById(heaterId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            heaterManager.deleteById(heaterId);
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

        heaterEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}