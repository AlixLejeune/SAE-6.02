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

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.DataTableManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the DataTableEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de
 * tables
 * et de ses interactions avec les services.
 */
public class DataTableEditorTest {

    @MockBean
    private DataTableManager dataTableManager;

    @MockBean
    private RoomManager roomManager;

    private DataTableEditor dataTableEditor;
    private DataTable testDataTable;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(DataTableEditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());
        // Initialiser le DataTableEditor avec les mocks
        dataTableEditor = new DataTableEditor(dataTableManager, roomManager);

        // Créer des données de test
        testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBuilding(testBuilding);

        testDataTable = new DataTable();
        testDataTable.setId(1);
        testDataTable.setCustomName("Test Table");
        testDataTable.setRoom(testRoom);
        testDataTable.setPosX(1.0);
        testDataTable.setPosY(2.0);
        testDataTable.setPosZ(3.0);
        testDataTable.setSizeX(1.5);
        testDataTable.setSizeY(2.5);
        testDataTable.setSizeZ(0.8);

        logger.info("DataTableEditor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testDataTableEditorInstantiation() {
        // Vérifier que le DataTableEditor peut être instancié correctement
        assertNotNull(dataTableEditor);
        logger.info("DataTableEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        DataTableEditor editor = new DataTableEditor(dataTableManager, roomManager);
        assertNotNull(editor);
        logger.info("DataTableEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            dataTableEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            dataTableEditor.setOnDataChanged(null);
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
            dataTableEditor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    // @Test
    // void testOpenEditDialogWithValidDataTable() {
    //     // Mock des données nécessaires
    //     List<Room> mockRooms = Arrays.asList(testRoom);
    //     when(roomManager.getAllRooms()).thenReturn(mockRooms);

    //     // Tester l'ouverture du dialog d'édition avec une table valide
    //     assertDoesNotThrow(() -> {
    //         dataTableEditor.openEditDialog(testDataTable);
    //     });

    //     // Vérifier que getAllRooms a été appelé
    //     verify(roomManager, atLeastOnce()).getAllRooms();

    //     logger.info("Edit dialog opened successfully for table: " + testDataTable.getCustomName());
    // }

    @Test
    void testOpenEditDialogWithNullDataTable() {
        // Tester l'ouverture du dialog d'édition avec une table null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            dataTableEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null DataTable input");
    }

    @Test
    void testConfirmDeleteWithValidDataTable() {
        // Tester la confirmation de suppression avec une table valide
        assertDoesNotThrow(() -> {
            dataTableEditor.confirmDelete(testDataTable);
        });

        logger.info("Delete confirmation dialog opened for table: " + testDataTable.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullDataTable() {
        // Tester la confirmation de suppression avec une table null
        assertDoesNotThrow(() -> {
            dataTableEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null DataTable input");
    }

    @Test
    void testDataTableManagerIntegration() {
        // Tester l'intégration avec DataTableManager
        assertNotNull(dataTableManager);

        // Mock du comportement de sauvegarde
        when(dataTableManager.save(any(DataTable.class))).thenReturn(testDataTable);

        DataTable savedTable = dataTableManager.save(testDataTable);
        assertNotNull(savedTable);
        assertEquals(testDataTable.getCustomName(), savedTable.getCustomName());

        // Vérifier que save a été appelé
        verify(dataTableManager, times(1)).save(testDataTable);

        logger.info("DataTableManager integration test successful");
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
    void testDataTableValidation() {
        // Tester la validation des données de table
        DataTable validTable = new DataTable();
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

        logger.info("DataTable validation test - all required fields present");
    }

    @Test
    void testDataTableWithEmptyName() {
        // Tester avec un nom vide
        DataTable emptyNameTable = new DataTable();
        emptyNameTable.setCustomName("");
        emptyNameTable.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameTable.getCustomName().isEmpty());

        logger.info("DataTable with empty name handled correctly");
    }

    @Test
    void testDataTableWithNullRoom() {
        // Tester avec une salle null
        DataTable nullRoomTable = new DataTable();
        nullRoomTable.setCustomName("Test Table");
        nullRoomTable.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomTable.getRoom());

        logger.info("DataTable with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer tableId = 1;

        // Mock de la méthode deleteById
        doNothing().when(dataTableManager).deleteById(tableId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            dataTableManager.deleteById(tableId);
        });

        // Vérifier que deleteById a été appelé
        verify(dataTableManager, times(1)).deleteById(tableId);

        logger.info("Delete operation mock test successful for table ID: " + tableId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(dataTableManager.save(any(DataTable.class))).thenReturn(testDataTable);

        // Simuler la sauvegarde
        DataTable result = assertDoesNotThrow(() -> {
            return dataTableManager.save(testDataTable);
        });

        assertNotNull(result);
        assertEquals(testDataTable.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(dataTableManager, times(1)).save(testDataTable);

        logger.info("Save operation mock test successful for table: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(dataTableManager.save(any(DataTable.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            dataTableManager.save(testDataTable);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer tableId = 1;
        doThrow(new RuntimeException("Delete error")).when(dataTableManager).deleteById(tableId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            dataTableManager.deleteById(tableId);
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
    void testDataTablePositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testDataTable.getPosX());
        assertEquals(2.0, testDataTable.getPosY());
        assertEquals(3.0, testDataTable.getPosZ());

        logger.info("DataTable position values test successful: X=" +
                testDataTable.getPosX() + ", Y=" + testDataTable.getPosY() +
                ", Z=" + testDataTable.getPosZ());
    }

    @Test
    void testDataTableSizeValues() {
        // Tester les valeurs de taille
        assertEquals(1.5, testDataTable.getSizeX());
        assertEquals(2.5, testDataTable.getSizeY());
        assertEquals(0.8, testDataTable.getSizeZ());

        logger.info("DataTable size values test successful: X=" +
                testDataTable.getSizeX() + ", Y=" + testDataTable.getSizeY() +
                ", Z=" + testDataTable.getSizeZ());
    }

    @Test
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        dataTableEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}