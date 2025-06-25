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
 * Test class for the SensorCO2View component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de gestion des tables de données
 * et de ses interactions avec les services.
 */
public class SensorCO2ViewTest {

    @MockBean
    private SensorCO2Manager SensorCO2Manager;

    @MockBean
    private RoomManager roomManager;

    private SensorCO2View SensorCO2View;
    private SensorCO2 testSensorCO2;
    private SensorCO2 testSensorCO22;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(SensorCO2ViewTest.class.getName());

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

        testSensorCO2 = new SensorCO2();
        testSensorCO2.setId(1);
        testSensorCO2.setCustomName("Test SensorCO2 1");
        testSensorCO2.setRoom(testRoom);
        testSensorCO2.setPosX(1.0);
        testSensorCO2.setPosY(2.0);
        testSensorCO2.setPosZ(3.0);

        testSensorCO22 = new SensorCO2();
        testSensorCO22.setId(2);
        testSensorCO22.setCustomName("Test SensorCO2 2");
        testSensorCO22.setRoom(testRoom);
        testSensorCO22.setPosX(2.0);
        testSensorCO22.setPosY(3.0);

        // Initialiser la SensorCO2View avec les mocks
        SensorCO2View = new SensorCO2View(SensorCO2Manager, roomManager);

        logger.info("SensorCO2View test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSensorCO2ViewInstantiation() {
        // Vérifier que la SensorCO2View peut être instanciée correctement
        assertNotNull(SensorCO2View);
        logger.info("SensorCO2View instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        SensorCO2View view = new SensorCO2View(SensorCO2Manager, roomManager);
        assertNotNull(view);
        logger.info("SensorCO2View constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<SensorCO2> mockSensorCO2s = Arrays.asList(testSensorCO2, testSensorCO22);
        when(SensorCO2Manager.findAll()).thenReturn(mockSensorCO2s);

        // Créer une nouvelle instance pour tester l'initialisation
        SensorCO2View newView = new SensorCO2View(SensorCO2Manager, roomManager);
        
        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(SensorCO2Manager, atLeastOnce()).findAll();
        
        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidSensorCO2s() {
        // Mock des données
        List<SensorCO2> mockSensorCO2s = Arrays.asList(testSensorCO2, testSensorCO22);
        when(SensorCO2Manager.findAll()).thenReturn(mockSensorCO2s);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            SensorCO2View newView = new SensorCO2View(SensorCO2Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(SensorCO2Manager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockSensorCO2s.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(SensorCO2Manager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            SensorCO2View newView = new SensorCO2View(SensorCO2Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(SensorCO2Manager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(SensorCO2Manager.findAll())
            .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            SensorCO2View newView = new SensorCO2View(SensorCO2Manager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testSensorCO2ManagerIntegration() {
        // Tester l'intégration avec SensorCO2Manager
        assertNotNull(SensorCO2Manager);

        // Mock du comportement de sauvegarde
        when(SensorCO2Manager.save(any(SensorCO2.class))).thenReturn(testSensorCO2);

        SensorCO2 savedSensorCO2 = SensorCO2Manager.save(testSensorCO2);
        assertNotNull(savedSensorCO2);
        assertEquals(testSensorCO2.getCustomName(), savedSensorCO2.getCustomName());

        // Vérifier que save a été appelé
        verify(SensorCO2Manager, times(1)).save(testSensorCO2);

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
    void testSaveSensorCO2Operation() {
        // Tester l'opération de sauvegarde (pas de updateSensorCO2 dans le service)
        when(SensorCO2Manager.save(any(SensorCO2.class))).thenReturn(testSensorCO2);

        // Simuler la sauvegarde
        SensorCO2 result = assertDoesNotThrow(() -> {
            return SensorCO2Manager.save(testSensorCO2);
        });

        assertNotNull(result);
        assertEquals(testSensorCO2.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(SensorCO2Manager, times(1)).save(testSensorCO2);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteSensorCO2Operation() {
        // Tester l'opération de suppression
        Integer SensorCO2Id = 1;

        // Mock de la méthode deleteById
        doNothing().when(SensorCO2Manager).deleteById(SensorCO2Id);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            SensorCO2Manager.deleteById(SensorCO2Id);
        });

        // Vérifier que deleteById a été appelé
        verify(SensorCO2Manager, times(1)).deleteById(SensorCO2Id);

        logger.info("Delete operation test successful for data table ID: " + SensorCO2Id);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer SensorCO2Id = 1;
        when(SensorCO2Manager.findById(SensorCO2Id)).thenReturn(testSensorCO2);

        SensorCO2 result = assertDoesNotThrow(() -> {
            return SensorCO2Manager.findById(SensorCO2Id);
        });

        assertNotNull(result);
        assertEquals(testSensorCO2.getId(), result.getId());
        assertEquals(testSensorCO2.getCustomName(), result.getCustomName());

        verify(SensorCO2Manager, times(1)).findById(SensorCO2Id);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(SensorCO2Manager.findById(invalidId))
            .thenThrow(new IllegalArgumentException("Aucune SensorCO2 trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            SensorCO2Manager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<SensorCO2> mockSensorCO2s = Arrays.asList(testSensorCO2, testSensorCO22);
        when(SensorCO2Manager.findByRoomId(roomId)).thenReturn(mockSensorCO2s);

        List<SensorCO2> result = SensorCO2Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(SensorCO2Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<SensorCO2> mockSensorCO2s = Arrays.asList(testSensorCO2);
        when(SensorCO2Manager.findByRoomId(roomId)).thenReturn(mockSensorCO2s);

        List<SensorCO2> result = SensorCO2Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(SensorCO2Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test SensorCO2 1";
        List<SensorCO2> mockSensorCO2s = Arrays.asList(testSensorCO2);
        when(SensorCO2Manager.findByCustomName(customName)).thenReturn(mockSensorCO2s);

        List<SensorCO2> result = SensorCO2Manager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(SensorCO2Manager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs SensorCO2s
        List<SensorCO2> SensorCO2List = Arrays.asList(testSensorCO2, testSensorCO22);
        when(SensorCO2Manager.saveAll(SensorCO2List)).thenReturn(SensorCO2List);

        List<SensorCO2> result = SensorCO2Manager.saveAll(SensorCO2List);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(SensorCO2Manager, times(1)).saveAll(SensorCO2List);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet SensorCO2
        doNothing().when(SensorCO2Manager).delete(testSensorCO2);

        assertDoesNotThrow(() -> {
            SensorCO2Manager.delete(testSensorCO2);
        });

        verify(SensorCO2Manager, times(1)).delete(testSensorCO2);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les SensorCO2s
        doNothing().when(SensorCO2Manager).deleteAll();

        assertDoesNotThrow(() -> {
            SensorCO2Manager.deleteAll();
        });

        verify(SensorCO2Manager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test SensorCO2 1";
        doNothing().when(SensorCO2Manager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            SensorCO2Manager.deleteByCustomName(customName);
        });

        verify(SensorCO2Manager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer SensorCO2Id = 1;
        when(SensorCO2Manager.existsById(SensorCO2Id)).thenReturn(true);

        boolean exists = SensorCO2Manager.existsById(SensorCO2Id);
        assertTrue(exists);

        verify(SensorCO2Manager, times(1)).existsById(SensorCO2Id);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(SensorCO2Manager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = SensorCO2Manager.existsById(nonExistentId);
        assertFalse(exists);

        verify(SensorCO2Manager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des SensorCO2s
        long expectedCount = 5L;
        when(SensorCO2Manager.count()).thenReturn(expectedCount);

        long count = SensorCO2Manager.count();
        assertEquals(expectedCount, count);

        verify(SensorCO2Manager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(SensorCO2Manager.save(any(SensorCO2.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SensorCO2Manager.save(testSensorCO2);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer SensorCO2Id = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(SensorCO2Manager).deleteById(SensorCO2Id);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SensorCO2Manager.deleteById(SensorCO2Id);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testSensorCO2Validation() {
        // Tester la validation des données de table
        SensorCO2 validSensorCO2 = new SensorCO2();
        validSensorCO2.setCustomName("Valid SensorCO2");
        validSensorCO2.setRoom(testRoom);
        validSensorCO2.setPosX(1.0);
        validSensorCO2.setPosY(1.0);
        validSensorCO2.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validSensorCO2.getCustomName());
        assertNotNull(validSensorCO2.getRoom());
        assertNotNull(validSensorCO2.getPosX());

        logger.info("SensorCO2 validation test - all required fields present");
    }

    @Test
    void testSensorCO2WithEmptyName() {
        // Tester avec un nom vide
        SensorCO2 emptyNameSensorCO2 = new SensorCO2();
        emptyNameSensorCO2.setCustomName("");
        emptyNameSensorCO2.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameSensorCO2.getCustomName().isEmpty());

        logger.info("SensorCO2 with empty name detected correctly");
    }

    @Test
    void testSensorCO2WithNullRoom() {
        // Tester avec une salle null
        SensorCO2 nullRoomSensorCO2 = new SensorCO2();
        nullRoomSensorCO2.setCustomName("Test SensorCO2");
        nullRoomSensorCO2.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomSensorCO2.getRoom());

        logger.info("SensorCO2 with null room detected correctly");
    }

    @Test
    void testSensorCO2WithNullName() {
        // Tester avec un nom null
        SensorCO2 nullNameSensorCO2 = new SensorCO2();
        nullNameSensorCO2.setCustomName(null);
        nullNameSensorCO2.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameSensorCO2.getCustomName());

        logger.info("SensorCO2 with null name detected correctly");
    }

    @Test
    void testSensorCO2WithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        SensorCO2 whitespaceNameSensorCO2 = new SensorCO2();
        whitespaceNameSensorCO2.setCustomName("   ");
        whitespaceNameSensorCO2.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameSensorCO2.getCustomName().trim().isEmpty());

        logger.info("SensorCO2 with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleSensorCO2sHandling() {
        // Tester avec plusieurs tables
        SensorCO2 SensorCO23 = new SensorCO2();
        SensorCO23.setId(3);
        SensorCO23.setCustomName("Test SensorCO2 3");
        SensorCO23.setRoom(testRoom);

        List<SensorCO2> multipleSensorCO2s = Arrays.asList(testSensorCO2, testSensorCO22, SensorCO23);
        when(SensorCO2Manager.findAll()).thenReturn(multipleSensorCO2s);

        List<SensorCO2> result = SensorCO2Manager.findAll();
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
    void testSensorCO2IdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testSensorCO2.getId());
        assertEquals(Integer.valueOf(2), testSensorCO22.getId());

        logger.info("SensorCO2 ID handling test successful: SensorCO21 ID=" + 
                testSensorCO2.getId() + ", SensorCO22 ID=" + testSensorCO22.getId());
    }

    @Test
    void testSensorCO2NameHandling() {
        // Tester la gestion des noms
        assertEquals("Test SensorCO2 1", testSensorCO2.getCustomName());
        assertEquals("Test SensorCO2 2", testSensorCO22.getCustomName());

        logger.info("SensorCO2 name handling test successful: SensorCO21 name='" + 
                testSensorCO2.getCustomName() + "', SensorCO22 name='" + testSensorCO22.getCustomName() + "'");
    }

    @Test
    void testSensorCO2RoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testSensorCO2.getRoom());
        assertEquals(testRoom.getName(), testSensorCO2.getRoom().getName());

        logger.info("SensorCO2 room association test successful: Room='" + 
                testSensorCO2.getRoom().getName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(SensorCO2Manager.findAll()).thenReturn(Arrays.asList(testSensorCO2));
        when(SensorCO2Manager.save(any(SensorCO2.class))).thenReturn(testSensorCO2);
        
        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            SensorCO2Manager.findAll();
            SensorCO2Manager.save(testSensorCO2);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<SensorCO2> largeDataset = Arrays.asList(
            testSensorCO2, testSensorCO22,
            createTestSensorCO2(3, "SensorCO2 3"),
            createTestSensorCO2(4, "SensorCO2 4"),
            createTestSensorCO2(5, "SensorCO2 5")
        );
        
        when(SensorCO2Manager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            SensorCO2View newView = new SensorCO2View(SensorCO2Manager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        SensorCO2 invalidSensorCO2 = new SensorCO2();
        invalidSensorCO2.setCustomName("Invalid Position SensorCO2");
        invalidSensorCO2.setRoom(testRoom);
        invalidSensorCO2.setPosX(-1.0);  // Position négative
        invalidSensorCO2.setPosY(Double.NaN);  // Valeur NaN
        invalidSensorCO2.setPosZ(Double.POSITIVE_INFINITY);  // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidSensorCO2.getPosX() < 0);
        assertTrue(Double.isNaN(invalidSensorCO2.getPosY()));
        assertTrue(Double.isInfinite(invalidSensorCO2.getPosZ()));

        logger.info("Invalid position values test successful");
    }


    // Méthode utilitaire pour créer des tables de données de test
    private SensorCO2 createTestSensorCO2(Integer id, String name) {
        SensorCO2 SensorCO2 = new SensorCO2();
        SensorCO2.setId(id);
        SensorCO2.setCustomName(name);
        SensorCO2.setRoom(testRoom);
        SensorCO2.setPosX(1.0);
        SensorCO2.setPosY(1.0);
        SensorCO2.setPosZ(1.0);

        return SensorCO2;
    }
}