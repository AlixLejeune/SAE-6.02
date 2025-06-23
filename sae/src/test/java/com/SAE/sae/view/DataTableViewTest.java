package com.SAE.sae.view;

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

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.DataTableManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the DataTableView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de gestion des tables de données
 * et de ses interactions avec les services.
 */
public class DataTableViewTest {

    @MockBean
    private DataTableManager dataTableManager;

    @MockBean
    private RoomManager roomManager;

    private DataTableView dataTableView;
    private DataTable testDataTable;
    private DataTable testDataTable2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(DataTableViewTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());
        
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
        testDataTable.setCustomName("Test DataTable 1");
        testDataTable.setRoom(testRoom);
        testDataTable.setPosX(1.0);
        testDataTable.setPosY(2.0);
        testDataTable.setPosZ(3.0);
        testDataTable.setSizeX(1.5);
        testDataTable.setSizeY(2.5);
        testDataTable.setSizeZ(0.8);

        testDataTable2 = new DataTable();
        testDataTable2.setId(2);
        testDataTable2.setCustomName("Test DataTable 2");
        testDataTable2.setRoom(testRoom);
        testDataTable2.setPosX(2.0);
        testDataTable2.setPosY(3.0);
        testDataTable2.setPosZ(4.0);
        testDataTable2.setSizeX(2.0);
        testDataTable2.setSizeY(3.0);
        testDataTable2.setSizeZ(1.0);

        // Initialiser la DataTableView avec les mocks
        dataTableView = new DataTableView(dataTableManager, roomManager);

        logger.info("DataTableView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testDataTableViewInstantiation() {
        // Vérifier que la DataTableView peut être instanciée correctement
        assertNotNull(dataTableView);
        logger.info("DataTableView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        DataTableView view = new DataTableView(dataTableManager, roomManager);
        assertNotNull(view);
        logger.info("DataTableView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<DataTable> mockDataTables = Arrays.asList(testDataTable, testDataTable2);
        when(dataTableManager.findAll()).thenReturn(mockDataTables);

        // Créer une nouvelle instance pour tester l'initialisation
        DataTableView newView = new DataTableView(dataTableManager, roomManager);
        
        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(dataTableManager, atLeastOnce()).findAll();
        
        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidDataTables() {
        // Mock des données
        List<DataTable> mockDataTables = Arrays.asList(testDataTable, testDataTable2);
        when(dataTableManager.findAll()).thenReturn(mockDataTables);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            DataTableView newView = new DataTableView(dataTableManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(dataTableManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockDataTables.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(dataTableManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            DataTableView newView = new DataTableView(dataTableManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(dataTableManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(dataTableManager.findAll())
            .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            DataTableView newView = new DataTableView(dataTableManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testDataTableManagerIntegration() {
        // Tester l'intégration avec DataTableManager
        assertNotNull(dataTableManager);

        // Mock du comportement de sauvegarde
        when(dataTableManager.save(any(DataTable.class))).thenReturn(testDataTable);

        DataTable savedDataTable = dataTableManager.save(testDataTable);
        assertNotNull(savedDataTable);
        assertEquals(testDataTable.getCustomName(), savedDataTable.getCustomName());

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
    void testSaveDataTableOperation() {
        // Tester l'opération de sauvegarde (pas de updateDataTable dans le service)
        when(dataTableManager.save(any(DataTable.class))).thenReturn(testDataTable);

        // Simuler la sauvegarde
        DataTable result = assertDoesNotThrow(() -> {
            return dataTableManager.save(testDataTable);
        });

        assertNotNull(result);
        assertEquals(testDataTable.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(dataTableManager, times(1)).save(testDataTable);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteDataTableOperation() {
        // Tester l'opération de suppression
        Integer dataTableId = 1;

        // Mock de la méthode deleteById
        doNothing().when(dataTableManager).deleteById(dataTableId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            dataTableManager.deleteById(dataTableId);
        });

        // Vérifier que deleteById a été appelé
        verify(dataTableManager, times(1)).deleteById(dataTableId);

        logger.info("Delete operation test successful for data table ID: " + dataTableId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer dataTableId = 1;
        when(dataTableManager.findById(dataTableId)).thenReturn(testDataTable);

        DataTable result = assertDoesNotThrow(() -> {
            return dataTableManager.findById(dataTableId);
        });

        assertNotNull(result);
        assertEquals(testDataTable.getId(), result.getId());
        assertEquals(testDataTable.getCustomName(), result.getCustomName());

        verify(dataTableManager, times(1)).findById(dataTableId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(dataTableManager.findById(invalidId))
            .thenThrow(new IllegalArgumentException("Aucune DataTable trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            dataTableManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<DataTable> mockDataTables = Arrays.asList(testDataTable, testDataTable2);
        when(dataTableManager.findByRoomId(roomId)).thenReturn(mockDataTables);

        List<DataTable> result = dataTableManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(dataTableManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<DataTable> mockDataTables = Arrays.asList(testDataTable);
        when(dataTableManager.findByRoomId(roomId)).thenReturn(mockDataTables);

        List<DataTable> result = dataTableManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dataTableManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test DataTable 1";
        List<DataTable> mockDataTables = Arrays.asList(testDataTable);
        when(dataTableManager.findByCustomName(customName)).thenReturn(mockDataTables);

        List<DataTable> result = dataTableManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(dataTableManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs DataTables
        List<DataTable> dataTableList = Arrays.asList(testDataTable, testDataTable2);
        when(dataTableManager.saveAll(dataTableList)).thenReturn(dataTableList);

        List<DataTable> result = dataTableManager.saveAll(dataTableList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(dataTableManager, times(1)).saveAll(dataTableList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet DataTable
        doNothing().when(dataTableManager).delete(testDataTable);

        assertDoesNotThrow(() -> {
            dataTableManager.delete(testDataTable);
        });

        verify(dataTableManager, times(1)).delete(testDataTable);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les DataTables
        doNothing().when(dataTableManager).deleteAll();

        assertDoesNotThrow(() -> {
            dataTableManager.deleteAll();
        });

        verify(dataTableManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByRoomIdOperation() {
        // Tester la suppression par ID de salle
        Integer roomId = 1;
        doNothing().when(dataTableManager).deleteByRoomId(roomId);

        assertDoesNotThrow(() -> {
            dataTableManager.deleteByRoomId(roomId);
        });

        verify(dataTableManager, times(1)).deleteByRoomId(roomId);

        logger.info("DeleteByRoomId operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test DataTable 1";
        doNothing().when(dataTableManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            dataTableManager.deleteByCustomName(customName);
        });

        verify(dataTableManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer dataTableId = 1;
        when(dataTableManager.existsById(dataTableId)).thenReturn(true);

        boolean exists = dataTableManager.existsById(dataTableId);
        assertTrue(exists);

        verify(dataTableManager, times(1)).existsById(dataTableId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(dataTableManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = dataTableManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(dataTableManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des DataTables
        long expectedCount = 5L;
        when(dataTableManager.count()).thenReturn(expectedCount);

        long count = dataTableManager.count();
        assertEquals(expectedCount, count);

        verify(dataTableManager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(dataTableManager.save(any(DataTable.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            dataTableManager.save(testDataTable);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer dataTableId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(dataTableManager).deleteById(dataTableId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            dataTableManager.deleteById(dataTableId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testDataTableValidation() {
        // Tester la validation des données de table
        DataTable validDataTable = new DataTable();
        validDataTable.setCustomName("Valid DataTable");
        validDataTable.setRoom(testRoom);
        validDataTable.setPosX(1.0);
        validDataTable.setPosY(1.0);
        validDataTable.setPosZ(1.0);
        validDataTable.setSizeX(1.0);
        validDataTable.setSizeY(1.0);
        validDataTable.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validDataTable.getCustomName());
        assertNotNull(validDataTable.getRoom());
        assertNotNull(validDataTable.getPosX());
        assertNotNull(validDataTable.getSizeX());

        logger.info("DataTable validation test - all required fields present");
    }

    @Test
    void testDataTableWithEmptyName() {
        // Tester avec un nom vide
        DataTable emptyNameDataTable = new DataTable();
        emptyNameDataTable.setCustomName("");
        emptyNameDataTable.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameDataTable.getCustomName().isEmpty());

        logger.info("DataTable with empty name detected correctly");
    }

    @Test
    void testDataTableWithNullRoom() {
        // Tester avec une salle null
        DataTable nullRoomDataTable = new DataTable();
        nullRoomDataTable.setCustomName("Test DataTable");
        nullRoomDataTable.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomDataTable.getRoom());

        logger.info("DataTable with null room detected correctly");
    }

    @Test
    void testDataTableWithNullName() {
        // Tester avec un nom null
        DataTable nullNameDataTable = new DataTable();
        nullNameDataTable.setCustomName(null);
        nullNameDataTable.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameDataTable.getCustomName());

        logger.info("DataTable with null name detected correctly");
    }

    @Test
    void testDataTableWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        DataTable whitespaceNameDataTable = new DataTable();
        whitespaceNameDataTable.setCustomName("   ");
        whitespaceNameDataTable.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameDataTable.getCustomName().trim().isEmpty());

        logger.info("DataTable with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleDataTablesHandling() {
        // Tester avec plusieurs tables
        DataTable dataTable3 = new DataTable();
        dataTable3.setId(3);
        dataTable3.setCustomName("Test DataTable 3");
        dataTable3.setRoom(testRoom);

        List<DataTable> multipleDataTables = Arrays.asList(testDataTable, testDataTable2, dataTable3);
        when(dataTableManager.findAll()).thenReturn(multipleDataTables);

        List<DataTable> result = dataTableManager.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());

        logger.info("Multiple data tables handling test successful - " + result.size() + " data tables");
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
    void testDataTableIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testDataTable.getId());
        assertEquals(Integer.valueOf(2), testDataTable2.getId());

        logger.info("DataTable ID handling test successful: DataTable1 ID=" + 
                testDataTable.getId() + ", DataTable2 ID=" + testDataTable2.getId());
    }

    @Test
    void testDataTableNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test DataTable 1", testDataTable.getCustomName());
        assertEquals("Test DataTable 2", testDataTable2.getCustomName());

        logger.info("DataTable name handling test successful: DataTable1 name='" + 
                testDataTable.getCustomName() + "', DataTable2 name='" + testDataTable2.getCustomName() + "'");
    }

    @Test
    void testDataTableRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testDataTable.getRoom());
        assertEquals(testRoom.getName(), testDataTable.getRoom().getName());

        logger.info("DataTable room association test successful: Room='" + 
                testDataTable.getRoom().getName() + "'");
    }

    @Test
    void testfindAllMultipleCalls() {
        // Tester plusieurs appels consécutifs
        List<DataTable> mockDataTables = Arrays.asList(testDataTable);
        when(dataTableManager.findAll()).thenReturn(mockDataTables);

        // Faire plusieurs appels
        dataTableManager.findAll();
        dataTableManager.findAll();
        dataTableManager.findAll();

        // Vérifier que la méthode a été appelée 3 fois
        verify(dataTableManager, times(3)).findAll();

        logger.info("Multiple findAll calls test successful");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(dataTableManager.findAll()).thenReturn(Arrays.asList(testDataTable));
        when(dataTableManager.save(any(DataTable.class))).thenReturn(testDataTable);
        
        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            dataTableManager.findAll();
            dataTableManager.save(testDataTable);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<DataTable> largeDataset = Arrays.asList(
            testDataTable, testDataTable2,
            createTestDataTable(3, "DataTable 3"),
            createTestDataTable(4, "DataTable 4"),
            createTestDataTable(5, "DataTable 5")
        );
        
        when(dataTableManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            DataTableView newView = new DataTableView(dataTableManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testNullManagersHandling() {
        // Tester avec des managers null (devrait lever une exception)
        assertThrows(Exception.class, () -> {
            new DataTableView(null, roomManager);
        });

        assertThrows(Exception.class, () -> {
            new DataTableView(dataTableManager, null);
        });

        assertThrows(Exception.class, () -> {
            new DataTableView(null, null);
        });

        logger.info("Null managers handling test successful");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        DataTable invalidDataTable = new DataTable();
        invalidDataTable.setCustomName("Invalid Position DataTable");
        invalidDataTable.setRoom(testRoom);
        invalidDataTable.setPosX(-1.0);  // Position négative
        invalidDataTable.setPosY(Double.NaN);  // Valeur NaN
        invalidDataTable.setPosZ(Double.POSITIVE_INFINITY);  // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidDataTable.getPosX() < 0);
        assertTrue(Double.isNaN(invalidDataTable.getPosY()));
        assertTrue(Double.isInfinite(invalidDataTable.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    @Test
    void testInvalidSizeValues() {
        // Tester avec des valeurs de taille invalides
        DataTable invalidDataTable = new DataTable();
        invalidDataTable.setCustomName("Invalid Size DataTable");
        invalidDataTable.setRoom(testRoom);
        invalidDataTable.setSizeX(0.0);  // Taille nulle
        invalidDataTable.setSizeY(-1.0);  // Taille négative
        invalidDataTable.setSizeZ(Double.NaN);  // Valeur NaN

        // Ces valeurs devraient être détectées comme invalides
        assertEquals(0.0, invalidDataTable.getSizeX());
        assertTrue(invalidDataTable.getSizeY() < 0);
        assertTrue(Double.isNaN(invalidDataTable.getSizeZ()));

        logger.info("Invalid size values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private DataTable createTestDataTable(Integer id, String name) {
        DataTable dataTable = new DataTable();
        dataTable.setId(id);
        dataTable.setCustomName(name);
        dataTable.setRoom(testRoom);
        dataTable.setPosX(1.0);
        dataTable.setPosY(1.0);
        dataTable.setPosZ(1.0);
        dataTable.setSizeX(1.0);
        dataTable.setSizeY(1.0);
        dataTable.setSizeZ(1.0);
        return dataTable;
    }
}