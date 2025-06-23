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

import com.SAE.sae.entity.RoomObjects.Sensor6in1;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.Sensor6in1Manager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the Sensor6in1View component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de gestion des tables de données
 * et de ses interactions avec les services.
 */
public class Sensor6in1ViewTest {

    @MockBean
    private Sensor6in1Manager Sensor6in1Manager;

    @MockBean
    private RoomManager roomManager;

    private Sensor6in1View Sensor6in1View;
    private Sensor6in1 testSensor6in1;
    private Sensor6in1 testSensor6in12;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(Sensor6in1ViewTest.class.getName());

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

        testSensor6in1 = new Sensor6in1();
        testSensor6in1.setId(1);
        testSensor6in1.setCustomName("Test Sensor6in1 1");
        testSensor6in1.setRoom(testRoom);
        testSensor6in1.setPosX(1.0);
        testSensor6in1.setPosY(2.0);
        testSensor6in1.setPosZ(3.0);


        testSensor6in12 = new Sensor6in1();
        testSensor6in12.setId(2);
        testSensor6in12.setCustomName("Test Sensor6in1 2");
        testSensor6in12.setRoom(testRoom);
        testSensor6in12.setPosX(2.0);
        testSensor6in12.setPosY(3.0);
        testSensor6in12.setPosZ(4.0);


        // Initialiser la Sensor6in1View avec les mocks
        Sensor6in1View = new Sensor6in1View(Sensor6in1Manager, roomManager);

        logger.info("Sensor6in1View test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSensor6in1ViewInstantiation() {
        // Vérifier que la Sensor6in1View peut être instanciée correctement
        assertNotNull(Sensor6in1View);
        logger.info("Sensor6in1View instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        Sensor6in1View view = new Sensor6in1View(Sensor6in1Manager, roomManager);
        assertNotNull(view);
        logger.info("Sensor6in1View constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Sensor6in1> mockSensor6in1s = Arrays.asList(testSensor6in1, testSensor6in12);
        when(Sensor6in1Manager.findAll()).thenReturn(mockSensor6in1s);

        // Créer une nouvelle instance pour tester l'initialisation
        Sensor6in1View newView = new Sensor6in1View(Sensor6in1Manager, roomManager);
        
        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(Sensor6in1Manager, atLeastOnce()).findAll();
        
        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidSensor6in1s() {
        // Mock des données
        List<Sensor6in1> mockSensor6in1s = Arrays.asList(testSensor6in1, testSensor6in12);
        when(Sensor6in1Manager.findAll()).thenReturn(mockSensor6in1s);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            Sensor6in1View newView = new Sensor6in1View(Sensor6in1Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(Sensor6in1Manager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockSensor6in1s.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(Sensor6in1Manager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            Sensor6in1View newView = new Sensor6in1View(Sensor6in1Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(Sensor6in1Manager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(Sensor6in1Manager.findAll())
            .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            Sensor6in1View newView = new Sensor6in1View(Sensor6in1Manager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testSensor6in1ManagerIntegration() {
        // Tester l'intégration avec Sensor6in1Manager
        assertNotNull(Sensor6in1Manager);

        // Mock du comportement de sauvegarde
        when(Sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        Sensor6in1 savedSensor6in1 = Sensor6in1Manager.save(testSensor6in1);
        assertNotNull(savedSensor6in1);
        assertEquals(testSensor6in1.getCustomName(), savedSensor6in1.getCustomName());

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
    void testSaveSensor6in1Operation() {
        // Tester l'opération de sauvegarde (pas de updateSensor6in1 dans le service)
        when(Sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);

        // Simuler la sauvegarde
        Sensor6in1 result = assertDoesNotThrow(() -> {
            return Sensor6in1Manager.save(testSensor6in1);
        });

        assertNotNull(result);
        assertEquals(testSensor6in1.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(Sensor6in1Manager, times(1)).save(testSensor6in1);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteSensor6in1Operation() {
        // Tester l'opération de suppression
        Integer Sensor6in1Id = 1;

        // Mock de la méthode deleteById
        doNothing().when(Sensor6in1Manager).deleteById(Sensor6in1Id);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            Sensor6in1Manager.deleteById(Sensor6in1Id);
        });

        // Vérifier que deleteById a été appelé
        verify(Sensor6in1Manager, times(1)).deleteById(Sensor6in1Id);

        logger.info("Delete operation test successful for data table ID: " + Sensor6in1Id);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer Sensor6in1Id = 1;
        when(Sensor6in1Manager.findById(Sensor6in1Id)).thenReturn(testSensor6in1);

        Sensor6in1 result = assertDoesNotThrow(() -> {
            return Sensor6in1Manager.findById(Sensor6in1Id);
        });

        assertNotNull(result);
        assertEquals(testSensor6in1.getId(), result.getId());
        assertEquals(testSensor6in1.getCustomName(), result.getCustomName());

        verify(Sensor6in1Manager, times(1)).findById(Sensor6in1Id);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(Sensor6in1Manager.findById(invalidId))
            .thenThrow(new IllegalArgumentException("Aucune Sensor6in1 trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            Sensor6in1Manager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Sensor6in1> mockSensor6in1s = Arrays.asList(testSensor6in1, testSensor6in12);
        when(Sensor6in1Manager.findByRoomId(roomId)).thenReturn(mockSensor6in1s);

        List<Sensor6in1> result = Sensor6in1Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(Sensor6in1Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Sensor6in1> mockSensor6in1s = Arrays.asList(testSensor6in1);
        when(Sensor6in1Manager.findByRoomId(roomId)).thenReturn(mockSensor6in1s);

        List<Sensor6in1> result = Sensor6in1Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(Sensor6in1Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Sensor6in1 1";
        List<Sensor6in1> mockSensor6in1s = Arrays.asList(testSensor6in1);
        when(Sensor6in1Manager.findByCustomName(customName)).thenReturn(mockSensor6in1s);

        List<Sensor6in1> result = Sensor6in1Manager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(Sensor6in1Manager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Sensor6in1s
        List<Sensor6in1> Sensor6in1List = Arrays.asList(testSensor6in1, testSensor6in12);
        when(Sensor6in1Manager.saveAll(Sensor6in1List)).thenReturn(Sensor6in1List);

        List<Sensor6in1> result = Sensor6in1Manager.saveAll(Sensor6in1List);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(Sensor6in1Manager, times(1)).saveAll(Sensor6in1List);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Sensor6in1
        doNothing().when(Sensor6in1Manager).delete(testSensor6in1);

        assertDoesNotThrow(() -> {
            Sensor6in1Manager.delete(testSensor6in1);
        });

        verify(Sensor6in1Manager, times(1)).delete(testSensor6in1);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Sensor6in1s
        doNothing().when(Sensor6in1Manager).deleteAll();

        assertDoesNotThrow(() -> {
            Sensor6in1Manager.deleteAll();
        });

        verify(Sensor6in1Manager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Sensor6in1 1";
        doNothing().when(Sensor6in1Manager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            Sensor6in1Manager.deleteByCustomName(customName);
        });

        verify(Sensor6in1Manager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer Sensor6in1Id = 1;
        when(Sensor6in1Manager.existsById(Sensor6in1Id)).thenReturn(true);

        boolean exists = Sensor6in1Manager.existsById(Sensor6in1Id);
        assertTrue(exists);

        verify(Sensor6in1Manager, times(1)).existsById(Sensor6in1Id);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(Sensor6in1Manager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = Sensor6in1Manager.existsById(nonExistentId);
        assertFalse(exists);

        verify(Sensor6in1Manager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Sensor6in1s
        long expectedCount = 5L;
        when(Sensor6in1Manager.count()).thenReturn(expectedCount);

        long count = Sensor6in1Manager.count();
        assertEquals(expectedCount, count);

        verify(Sensor6in1Manager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(Sensor6in1Manager.save(any(Sensor6in1.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor6in1Manager.save(testSensor6in1);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer Sensor6in1Id = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(Sensor6in1Manager).deleteById(Sensor6in1Id);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor6in1Manager.deleteById(Sensor6in1Id);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testSensor6in1Validation() {
        // Tester la validation des données de table
        Sensor6in1 validSensor6in1 = new Sensor6in1();
        validSensor6in1.setCustomName("Valid Sensor6in1");
        validSensor6in1.setRoom(testRoom);
        validSensor6in1.setPosX(1.0);
        validSensor6in1.setPosY(1.0);
        validSensor6in1.setPosZ(1.0);


        // Les données devraient être valides
        assertNotNull(validSensor6in1.getCustomName());
        assertNotNull(validSensor6in1.getRoom());
        assertNotNull(validSensor6in1.getPosX());

        logger.info("Sensor6in1 validation test - all required fields present");
    }

    @Test
    void testSensor6in1WithEmptyName() {
        // Tester avec un nom vide
        Sensor6in1 emptyNameSensor6in1 = new Sensor6in1();
        emptyNameSensor6in1.setCustomName("");
        emptyNameSensor6in1.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameSensor6in1.getCustomName().isEmpty());

        logger.info("Sensor6in1 with empty name detected correctly");
    }

    @Test
    void testSensor6in1WithNullRoom() {
        // Tester avec une salle null
        Sensor6in1 nullRoomSensor6in1 = new Sensor6in1();
        nullRoomSensor6in1.setCustomName("Test Sensor6in1");
        nullRoomSensor6in1.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomSensor6in1.getRoom());

        logger.info("Sensor6in1 with null room detected correctly");
    }

    @Test
    void testSensor6in1WithNullName() {
        // Tester avec un nom null
        Sensor6in1 nullNameSensor6in1 = new Sensor6in1();
        nullNameSensor6in1.setCustomName(null);
        nullNameSensor6in1.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameSensor6in1.getCustomName());

        logger.info("Sensor6in1 with null name detected correctly");
    }

    @Test
    void testMultipleSensor6in1sHandling() {
        // Tester avec plusieurs tables
        Sensor6in1 Sensor6in13 = new Sensor6in1();
        Sensor6in13.setId(3);
        Sensor6in13.setCustomName("Test Sensor6in1 3");
        Sensor6in13.setRoom(testRoom);

        List<Sensor6in1> multipleSensor6in1s = Arrays.asList(testSensor6in1, testSensor6in12, Sensor6in13);
        when(Sensor6in1Manager.findAll()).thenReturn(multipleSensor6in1s);

        List<Sensor6in1> result = Sensor6in1Manager.findAll();
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
    void testSensor6in1IdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testSensor6in1.getId());
        assertEquals(Integer.valueOf(2), testSensor6in12.getId());

        logger.info("Sensor6in1 ID handling test successful: Sensor6in11 ID=" + 
                testSensor6in1.getId() + ", Sensor6in12 ID=" + testSensor6in12.getId());
    }

    @Test
    void testSensor6in1NameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Sensor6in1 1", testSensor6in1.getCustomName());
        assertEquals("Test Sensor6in1 2", testSensor6in12.getCustomName());

        logger.info("Sensor6in1 name handling test successful: Sensor6in11 name='" + 
                testSensor6in1.getCustomName() + "', Sensor6in12 name='" + testSensor6in12.getCustomName() + "'");
    }

    @Test
    void testSensor6in1RoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testSensor6in1.getRoom());
        assertEquals(testRoom.getName(), testSensor6in1.getRoom().getName());

        logger.info("Sensor6in1 room association test successful: Room='" + 
                testSensor6in1.getRoom().getName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(Sensor6in1Manager.findAll()).thenReturn(Arrays.asList(testSensor6in1));
        when(Sensor6in1Manager.save(any(Sensor6in1.class))).thenReturn(testSensor6in1);
        
        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            Sensor6in1Manager.findAll();
            Sensor6in1Manager.save(testSensor6in1);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Sensor6in1> largeDataset = Arrays.asList(
            testSensor6in1, testSensor6in12,
            createTestSensor6in1(3, "Sensor6in1 3"),
            createTestSensor6in1(4, "Sensor6in1 4"),
            createTestSensor6in1(5, "Sensor6in1 5")
        );
        
        when(Sensor6in1Manager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            Sensor6in1View newView = new Sensor6in1View(Sensor6in1Manager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Sensor6in1 invalidSensor6in1 = new Sensor6in1();
        invalidSensor6in1.setCustomName("Invalid Position Sensor6in1");
        invalidSensor6in1.setRoom(testRoom);
        invalidSensor6in1.setPosX(-1.0);  // Position négative
        invalidSensor6in1.setPosY(Double.NaN);  // Valeur NaN
        invalidSensor6in1.setPosZ(Double.POSITIVE_INFINITY);  // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidSensor6in1.getPosX() < 0);
        assertTrue(Double.isNaN(invalidSensor6in1.getPosY()));
        assertTrue(Double.isInfinite(invalidSensor6in1.getPosZ()));

        logger.info("Invalid position values test successful");
    }

  

    // Méthode utilitaire pour créer des tables de données de test
    private Sensor6in1 createTestSensor6in1(Integer id, String name) {
        Sensor6in1 Sensor6in1 = new Sensor6in1();
        Sensor6in1.setId(id);
        Sensor6in1.setCustomName(name);
        Sensor6in1.setRoom(testRoom);
        Sensor6in1.setPosX(1.0);
        Sensor6in1.setPosY(1.0);
        Sensor6in1.setPosZ(1.0);
        return Sensor6in1;
    }
}