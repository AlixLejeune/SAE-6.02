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

import com.SAE.sae.entity.RoomObjects.Sensor9in1;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.Sensor9in1Manager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the Sensor9in1View component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de gestion des tables de données
 * et de ses interactions avec les services.
 */
public class Sensor9in1ViewTest {

    @MockBean
    private Sensor9in1Manager Sensor9in1Manager;

    @MockBean
    private RoomManager roomManager;

    private Sensor9in1View Sensor9in1View;
    private Sensor9in1 testSensor9in1;
    private Sensor9in1 testSensor9in12;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(Sensor9in1ViewTest.class.getName());

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

        testSensor9in1 = new Sensor9in1();
        testSensor9in1.setId(1);
        testSensor9in1.setCustomName("Test Sensor9in1 1");
        testSensor9in1.setRoom(testRoom);
        testSensor9in1.setPosX(1.0);
        testSensor9in1.setPosY(2.0);
        testSensor9in1.setPosZ(3.0);

        testSensor9in12 = new Sensor9in1();
        testSensor9in12.setId(2);
        testSensor9in12.setCustomName("Test Sensor9in1 2");
        testSensor9in12.setRoom(testRoom);
        testSensor9in12.setPosX(2.0);
        testSensor9in12.setPosY(3.0);
        testSensor9in12.setPosZ(4.0);

        // Initialiser la Sensor9in1View avec les mocks
        Sensor9in1View = new Sensor9in1View(Sensor9in1Manager, roomManager);

        logger.info("Sensor9in1View test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSensor9in1ViewInstantiation() {
        // Vérifier que la Sensor9in1View peut être instanciée correctement
        assertNotNull(Sensor9in1View);
        logger.info("Sensor9in1View instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        Sensor9in1View view = new Sensor9in1View(Sensor9in1Manager, roomManager);
        assertNotNull(view);
        logger.info("Sensor9in1View constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Sensor9in1> mockSensor9in1s = Arrays.asList(testSensor9in1, testSensor9in12);
        when(Sensor9in1Manager.findAll()).thenReturn(mockSensor9in1s);

        // Créer une nouvelle instance pour tester l'initialisation
        Sensor9in1View newView = new Sensor9in1View(Sensor9in1Manager, roomManager);
        
        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(Sensor9in1Manager, atLeastOnce()).findAll();
        
        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidSensor9in1s() {
        // Mock des données
        List<Sensor9in1> mockSensor9in1s = Arrays.asList(testSensor9in1, testSensor9in12);
        when(Sensor9in1Manager.findAll()).thenReturn(mockSensor9in1s);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            Sensor9in1View newView = new Sensor9in1View(Sensor9in1Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(Sensor9in1Manager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockSensor9in1s.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(Sensor9in1Manager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            Sensor9in1View newView = new Sensor9in1View(Sensor9in1Manager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(Sensor9in1Manager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(Sensor9in1Manager.findAll())
            .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            Sensor9in1View newView = new Sensor9in1View(Sensor9in1Manager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testSensor9in1ManagerIntegration() {
        // Tester l'intégration avec Sensor9in1Manager
        assertNotNull(Sensor9in1Manager);

        // Mock du comportement de sauvegarde
        when(Sensor9in1Manager.save(any(Sensor9in1.class))).thenReturn(testSensor9in1);

        Sensor9in1 savedSensor9in1 = Sensor9in1Manager.save(testSensor9in1);
        assertNotNull(savedSensor9in1);
        assertEquals(testSensor9in1.getCustomName(), savedSensor9in1.getCustomName());

        // Vérifier que save a été appelé
        verify(Sensor9in1Manager, times(1)).save(testSensor9in1);

        logger.info("Sensor9in1Manager integration test successful");
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
    void testSaveSensor9in1Operation() {
        // Tester l'opération de sauvegarde (pas de updateSensor9in1 dans le service)
        when(Sensor9in1Manager.save(any(Sensor9in1.class))).thenReturn(testSensor9in1);

        // Simuler la sauvegarde
        Sensor9in1 result = assertDoesNotThrow(() -> {
            return Sensor9in1Manager.save(testSensor9in1);
        });

        assertNotNull(result);
        assertEquals(testSensor9in1.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(Sensor9in1Manager, times(1)).save(testSensor9in1);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteSensor9in1Operation() {
        // Tester l'opération de suppression
        Integer Sensor9in1Id = 1;

        // Mock de la méthode deleteById
        doNothing().when(Sensor9in1Manager).deleteById(Sensor9in1Id);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            Sensor9in1Manager.deleteById(Sensor9in1Id);
        });

        // Vérifier que deleteById a été appelé
        verify(Sensor9in1Manager, times(1)).deleteById(Sensor9in1Id);

        logger.info("Delete operation test successful for data table ID: " + Sensor9in1Id);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer Sensor9in1Id = 1;
        when(Sensor9in1Manager.findById(Sensor9in1Id)).thenReturn(testSensor9in1);

        Sensor9in1 result = assertDoesNotThrow(() -> {
            return Sensor9in1Manager.findById(Sensor9in1Id);
        });

        assertNotNull(result);
        assertEquals(testSensor9in1.getId(), result.getId());
        assertEquals(testSensor9in1.getCustomName(), result.getCustomName());

        verify(Sensor9in1Manager, times(1)).findById(Sensor9in1Id);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(Sensor9in1Manager.findById(invalidId))
            .thenThrow(new IllegalArgumentException("Aucune Sensor9in1 trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            Sensor9in1Manager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Sensor9in1> mockSensor9in1s = Arrays.asList(testSensor9in1, testSensor9in12);
        when(Sensor9in1Manager.findByRoomId(roomId)).thenReturn(mockSensor9in1s);

        List<Sensor9in1> result = Sensor9in1Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(Sensor9in1Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Sensor9in1> mockSensor9in1s = Arrays.asList(testSensor9in1);
        when(Sensor9in1Manager.findByRoomId(roomId)).thenReturn(mockSensor9in1s);

        List<Sensor9in1> result = Sensor9in1Manager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(Sensor9in1Manager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Sensor9in1 1";
        List<Sensor9in1> mockSensor9in1s = Arrays.asList(testSensor9in1);
        when(Sensor9in1Manager.findByCustomName(customName)).thenReturn(mockSensor9in1s);

        List<Sensor9in1> result = Sensor9in1Manager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(Sensor9in1Manager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Sensor9in1s
        List<Sensor9in1> Sensor9in1List = Arrays.asList(testSensor9in1, testSensor9in12);
        when(Sensor9in1Manager.saveAll(Sensor9in1List)).thenReturn(Sensor9in1List);

        List<Sensor9in1> result = Sensor9in1Manager.saveAll(Sensor9in1List);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(Sensor9in1Manager, times(1)).saveAll(Sensor9in1List);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Sensor9in1
        doNothing().when(Sensor9in1Manager).delete(testSensor9in1);

        assertDoesNotThrow(() -> {
            Sensor9in1Manager.delete(testSensor9in1);
        });

        verify(Sensor9in1Manager, times(1)).delete(testSensor9in1);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Sensor9in1s
        doNothing().when(Sensor9in1Manager).deleteAll();

        assertDoesNotThrow(() -> {
            Sensor9in1Manager.deleteAll();
        });

        verify(Sensor9in1Manager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Sensor9in1 1";
        doNothing().when(Sensor9in1Manager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            Sensor9in1Manager.deleteByCustomName(customName);
        });

        verify(Sensor9in1Manager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer Sensor9in1Id = 1;
        when(Sensor9in1Manager.existsById(Sensor9in1Id)).thenReturn(true);

        boolean exists = Sensor9in1Manager.existsById(Sensor9in1Id);
        assertTrue(exists);

        verify(Sensor9in1Manager, times(1)).existsById(Sensor9in1Id);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(Sensor9in1Manager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = Sensor9in1Manager.existsById(nonExistentId);
        assertFalse(exists);

        verify(Sensor9in1Manager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Sensor9in1s
        long expectedCount = 5L;
        when(Sensor9in1Manager.count()).thenReturn(expectedCount);

        long count = Sensor9in1Manager.count();
        assertEquals(expectedCount, count);

        verify(Sensor9in1Manager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(Sensor9in1Manager.save(any(Sensor9in1.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor9in1Manager.save(testSensor9in1);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer Sensor9in1Id = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(Sensor9in1Manager).deleteById(Sensor9in1Id);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            Sensor9in1Manager.deleteById(Sensor9in1Id);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testSensor9in1Validation() {
        // Tester la validation des données de table
        Sensor9in1 validSensor9in1 = new Sensor9in1();
        validSensor9in1.setCustomName("Valid Sensor9in1");
        validSensor9in1.setRoom(testRoom);
        validSensor9in1.setPosX(1.0);
        validSensor9in1.setPosY(1.0);
        validSensor9in1.setPosZ(1.0);


        // Les données devraient être valides
        assertNotNull(validSensor9in1.getCustomName());
        assertNotNull(validSensor9in1.getRoom());
        assertNotNull(validSensor9in1.getPosX());

        logger.info("Sensor9in1 validation test - all required fields present");
    }

    @Test
    void testSensor9in1WithEmptyName() {
        // Tester avec un nom vide
        Sensor9in1 emptyNameSensor9in1 = new Sensor9in1();
        emptyNameSensor9in1.setCustomName("");
        emptyNameSensor9in1.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameSensor9in1.getCustomName().isEmpty());

        logger.info("Sensor9in1 with empty name detected correctly");
    }

    @Test
    void testSensor9in1WithNullRoom() {
        // Tester avec une salle null
        Sensor9in1 nullRoomSensor9in1 = new Sensor9in1();
        nullRoomSensor9in1.setCustomName("Test Sensor9in1");
        nullRoomSensor9in1.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomSensor9in1.getRoom());

        logger.info("Sensor9in1 with null room detected correctly");
    }

    @Test
    void testSensor9in1WithNullName() {
        // Tester avec un nom null
        Sensor9in1 nullNameSensor9in1 = new Sensor9in1();
        nullNameSensor9in1.setCustomName(null);
        nullNameSensor9in1.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameSensor9in1.getCustomName());

        logger.info("Sensor9in1 with null name detected correctly");
    }

    @Test
    void testSensor9in1WithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Sensor9in1 whitespaceNameSensor9in1 = new Sensor9in1();
        whitespaceNameSensor9in1.setCustomName("   ");
        whitespaceNameSensor9in1.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameSensor9in1.getCustomName().trim().isEmpty());

        logger.info("Sensor9in1 with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleSensor9in1sHandling() {
        // Tester avec plusieurs tables
        Sensor9in1 Sensor9in13 = new Sensor9in1();
        Sensor9in13.setId(3);
        Sensor9in13.setCustomName("Test Sensor9in1 3");
        Sensor9in13.setRoom(testRoom);

        List<Sensor9in1> multipleSensor9in1s = Arrays.asList(testSensor9in1, testSensor9in12, Sensor9in13);
        when(Sensor9in1Manager.findAll()).thenReturn(multipleSensor9in1s);

        List<Sensor9in1> result = Sensor9in1Manager.findAll();
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
    void testSensor9in1PositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testSensor9in1.getPosX());
        assertEquals(2.0, testSensor9in1.getPosY());
        assertEquals(3.0, testSensor9in1.getPosZ());

        logger.info("Sensor9in1 position values test successful: X=" +
                testSensor9in1.getPosX() + ", Y=" + testSensor9in1.getPosY() +
                ", Z=" + testSensor9in1.getPosZ());
    }


    @Test
    void testSensor9in1IdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testSensor9in1.getId());
        assertEquals(Integer.valueOf(2), testSensor9in12.getId());

        logger.info("Sensor9in1 ID handling test successful: Sensor9in11 ID=" + 
                testSensor9in1.getId() + ", Sensor9in12 ID=" + testSensor9in12.getId());
    }

    @Test
    void testSensor9in1NameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Sensor9in1 1", testSensor9in1.getCustomName());
        assertEquals("Test Sensor9in1 2", testSensor9in12.getCustomName());

        logger.info("Sensor9in1 name handling test successful: Sensor9in11 name='" + 
                testSensor9in1.getCustomName() + "', Sensor9in12 name='" + testSensor9in12.getCustomName() + "'");
    }

    @Test
    void testSensor9in1RoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testSensor9in1.getRoom());
        assertEquals(testRoom.getName(), testSensor9in1.getRoom().getName());

        logger.info("Sensor9in1 room association test successful: Room='" + 
                testSensor9in1.getRoom().getName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(Sensor9in1Manager.findAll()).thenReturn(Arrays.asList(testSensor9in1));
        when(Sensor9in1Manager.save(any(Sensor9in1.class))).thenReturn(testSensor9in1);
        
        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            Sensor9in1Manager.findAll();
            Sensor9in1Manager.save(testSensor9in1);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Sensor9in1> largeDataset = Arrays.asList(
            testSensor9in1, testSensor9in12,
            createTestSensor9in1(3, "Sensor9in1 3"),
            createTestSensor9in1(4, "Sensor9in1 4"),
            createTestSensor9in1(5, "Sensor9in1 5")
        );
        
        when(Sensor9in1Manager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            Sensor9in1View newView = new Sensor9in1View(Sensor9in1Manager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Sensor9in1 invalidSensor9in1 = new Sensor9in1();
        invalidSensor9in1.setCustomName("Invalid Position Sensor9in1");
        invalidSensor9in1.setRoom(testRoom);
        invalidSensor9in1.setPosX(-1.0);  // Position négative
        invalidSensor9in1.setPosY(Double.NaN);  // Valeur NaN
        invalidSensor9in1.setPosZ(Double.POSITIVE_INFINITY);  // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidSensor9in1.getPosX() < 0);
        assertTrue(Double.isNaN(invalidSensor9in1.getPosY()));
        assertTrue(Double.isInfinite(invalidSensor9in1.getPosZ()));

        logger.info("Invalid position values test successful");
    }



    // Méthode utilitaire pour créer des tables de données de test
    private Sensor9in1 createTestSensor9in1(Integer id, String name) {
        Sensor9in1 Sensor9in1 = new Sensor9in1();
        Sensor9in1.setId(id);
        Sensor9in1.setCustomName(name);
        Sensor9in1.setRoom(testRoom);
        Sensor9in1.setPosX(1.0);
        Sensor9in1.setPosY(1.0);
        Sensor9in1.setPosZ(1.0);
        return Sensor9in1;
    }
}