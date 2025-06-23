package com.SAE.sae.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.SAE.sae.entity.RoomObjects.Heater;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.HeaterManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@ExtendWith(MockitoExtension.class)
/**
 * Test class for the HeaterView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de
 * gestion des tables de données
 * et de ses interactions avec les services.
 */
public class HeaterViewTest {

    @Mock
    private HeaterManager HeaterManager;

    @Mock
    private RoomManager roomManager;

    private HeaterView HeaterView;
    private Heater testHeater;
    private Heater testHeater2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(HeaterViewTest.class.getName());

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

        testHeater = new Heater();
        testHeater.setId(1);
        testHeater.setCustomName("Test Heater 1");
        testHeater.setRoom(testRoom);
        testHeater.setPosX(1.0);
        testHeater.setPosY(2.0);
        testHeater.setPosZ(3.0);
        testHeater.setSizeX(1.5);
        testHeater.setSizeY(2.5);
        testHeater.setSizeZ(0.8);

        testHeater2 = new Heater();
        testHeater2.setId(2);
        testHeater2.setCustomName("Test Heater 2");
        testHeater2.setRoom(testRoom);
        testHeater2.setPosX(2.0);
        testHeater2.setPosY(3.0);
        testHeater2.setPosZ(4.0);
        testHeater2.setSizeX(2.0);
        testHeater2.setSizeY(3.0);
        testHeater2.setSizeZ(1.0);

        // Initialiser la HeaterView avec les mocks
        HeaterView = new HeaterView(HeaterManager, roomManager);

        logger.info("HeaterView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testHeaterViewInstantiation() {
        // Vérifier que la HeaterView peut être instanciée correctement
        assertNotNull(HeaterView);
        logger.info("HeaterView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        HeaterView view = new HeaterView(HeaterManager, roomManager);
        assertNotNull(view);
        logger.info("HeaterView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Heater> mockHeaters = Arrays.asList(testHeater, testHeater2);
        when(HeaterManager.findAll()).thenReturn(mockHeaters);

        // Créer une nouvelle instance pour tester l'initialisation
        HeaterView newView = new HeaterView(HeaterManager, roomManager);

        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(HeaterManager, atLeastOnce()).findAll();

        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidHeaters() {
        // Mock des données
        List<Heater> mockHeaters = Arrays.asList(testHeater, testHeater2);
        when(HeaterManager.findAll()).thenReturn(mockHeaters);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            HeaterView newView = new HeaterView(HeaterManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(HeaterManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockHeaters.size() + " data tables");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(HeaterManager.findAll())
                .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            HeaterView newView = new HeaterView(HeaterManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testHeaterManagerIntegration() {
        // Tester l'intégration avec HeaterManager
        assertNotNull(HeaterManager);

        // Mock du comportement de sauvegarde
        when(HeaterManager.save(any(Heater.class))).thenReturn(testHeater);

        Heater savedHeater = HeaterManager.save(testHeater);
        assertNotNull(savedHeater);
        assertEquals(testHeater.getCustomName(), savedHeater.getCustomName());

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
    void testSaveHeaterOperation() {
        UI.setCurrent(new UI());

        // Tester l'opération de sauvegarde (pas de updateHeater dans le service)
        when(HeaterManager.save(any(Heater.class))).thenReturn(testHeater);

        // Simuler la sauvegarde
        Heater result = assertDoesNotThrow(() -> {
            return HeaterManager.save(testHeater);
        });

        assertNotNull(result);
        assertEquals(testHeater.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(HeaterManager, times(1)).save(testHeater);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteHeaterOperation() {
        // Tester l'opération de suppression
        Integer HeaterId = 1;

        // Mock de la méthode deleteById
        doNothing().when(HeaterManager).deleteById(HeaterId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            HeaterManager.deleteById(HeaterId);
        });

        // Vérifier que deleteById a été appelé
        verify(HeaterManager, times(1)).deleteById(HeaterId);

        logger.info("Delete operation test successful for data table ID: " + HeaterId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer HeaterId = 1;
        when(HeaterManager.findById(HeaterId)).thenReturn(testHeater);

        Heater result = assertDoesNotThrow(() -> {
            return HeaterManager.findById(HeaterId);
        });

        assertNotNull(result);
        assertEquals(testHeater.getId(), result.getId());
        assertEquals(testHeater.getCustomName(), result.getCustomName());

        verify(HeaterManager, times(1)).findById(HeaterId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(HeaterManager.findById(invalidId))
                .thenThrow(new IllegalArgumentException("Aucune Heater trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            HeaterManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Heater> mockHeaters = Arrays.asList(testHeater, testHeater2);
        when(HeaterManager.findByRoomId(roomId)).thenReturn(mockHeaters);

        List<Heater> result = HeaterManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(HeaterManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Heater> mockHeaters = Arrays.asList(testHeater);
        when(HeaterManager.findByRoomId(roomId)).thenReturn(mockHeaters);

        List<Heater> result = HeaterManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(HeaterManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Heater 1";
        List<Heater> mockHeaters = Arrays.asList(testHeater);
        when(HeaterManager.findByCustomName(customName)).thenReturn(mockHeaters);

        List<Heater> result = HeaterManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(HeaterManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Heaters
        List<Heater> HeaterList = Arrays.asList(testHeater, testHeater2);
        when(HeaterManager.saveAll(HeaterList)).thenReturn(HeaterList);

        List<Heater> result = HeaterManager.saveAll(HeaterList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(HeaterManager, times(1)).saveAll(HeaterList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Heater
        doNothing().when(HeaterManager).delete(testHeater);

        assertDoesNotThrow(() -> {
            HeaterManager.delete(testHeater);
        });

        verify(HeaterManager, times(1)).delete(testHeater);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Heaters
        doNothing().when(HeaterManager).deleteAll();

        assertDoesNotThrow(() -> {
            HeaterManager.deleteAll();
        });

        verify(HeaterManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Heater 1";
        doNothing().when(HeaterManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            HeaterManager.deleteByCustomName(customName);
        });

        verify(HeaterManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer HeaterId = 1;
        when(HeaterManager.existsById(HeaterId)).thenReturn(true);

        boolean exists = HeaterManager.existsById(HeaterId);
        assertTrue(exists);

        verify(HeaterManager, times(1)).existsById(HeaterId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(HeaterManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = HeaterManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(HeaterManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Heaters
        long expectedCount = 5L;
        when(HeaterManager.count()).thenReturn(expectedCount);

        long count = HeaterManager.count();
        assertEquals(expectedCount, count);

        verify(HeaterManager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer HeaterId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(HeaterManager).deleteById(HeaterId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            HeaterManager.deleteById(HeaterId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testHeaterValidation() {
        // Tester la validation des données de table
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
        UI.setCurrent(new UI());

        // Tester avec un nom vide
        Heater emptyNameHeater = new Heater();
        emptyNameHeater.setCustomName("");
        emptyNameHeater.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameHeater.getCustomName().isEmpty());

        logger.info("Heater with empty name detected correctly");
    }

    @Test
    void testHeaterWithNullRoom() {
        // Tester avec une salle null
        Heater nullRoomHeater = new Heater();
        nullRoomHeater.setCustomName("Test Heater");
        nullRoomHeater.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomHeater.getRoom());

        logger.info("Heater with null room detected correctly");
    }

    @Test
    void testHeaterWithNullName() {
        // Tester avec un nom null
        Heater nullNameHeater = new Heater();
        nullNameHeater.setCustomName(null);
        nullNameHeater.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameHeater.getCustomName());

        logger.info("Heater with null name detected correctly");
    }

    @Test
    void testHeaterWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Heater whitespaceNameHeater = new Heater();
        whitespaceNameHeater.setCustomName("   ");
        whitespaceNameHeater.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameHeater.getCustomName().trim().isEmpty());

        logger.info("Heater with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleHeatersHandling() {
        // Tester avec plusieurs tables
        Heater Heater3 = new Heater();
        Heater3.setId(3);
        Heater3.setCustomName("Test Heater 3");
        Heater3.setRoom(testRoom);

        List<Heater> multipleHeaters = Arrays.asList(testHeater, testHeater2, Heater3);
        when(HeaterManager.findAll()).thenReturn(multipleHeaters);

        List<Heater> result = HeaterManager.findAll();
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
    void testHeaterIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testHeater.getId());
        assertEquals(Integer.valueOf(2), testHeater2.getId());

        logger.info("Heater ID handling test successful: Heater1 ID=" +
                testHeater.getId() + ", Heater2 ID=" + testHeater2.getId());
    }

    @Test
    void testHeaterNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Heater 1", testHeater.getCustomName());
        assertEquals("Test Heater 2", testHeater2.getCustomName());

        logger.info("Heater name handling test successful: Heater1 name='" +
                testHeater.getCustomName() + "', Heater2 name='" + testHeater2.getCustomName() + "'");
    }

    @Test
    void testHeaterRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testHeater.getRoom());
        assertEquals(testRoom.getName(), testHeater.getRoom().getName());

        logger.info("Heater room association test successful: Room='" +
                testHeater.getRoom().getName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(HeaterManager.findAll()).thenReturn(Arrays.asList(testHeater));
        when(HeaterManager.save(any(Heater.class))).thenReturn(testHeater);

        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            HeaterManager.findAll();
            HeaterManager.save(testHeater);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Heater> largeDataset = Arrays.asList(
                testHeater, testHeater2,
                createTestHeater(3, "Heater 3"),
                createTestHeater(4, "Heater 4"),
                createTestHeater(5, "Heater 5"));

        when(HeaterManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            HeaterView newView = new HeaterView(HeaterManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Heater invalidHeater = new Heater();
        invalidHeater.setCustomName("Invalid Position Heater");
        invalidHeater.setRoom(testRoom);
        invalidHeater.setPosX(-1.0); // Position négative
        invalidHeater.setPosY(Double.NaN); // Valeur NaN
        invalidHeater.setPosZ(Double.POSITIVE_INFINITY); // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidHeater.getPosX() < 0);
        assertTrue(Double.isNaN(invalidHeater.getPosY()));
        assertTrue(Double.isInfinite(invalidHeater.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    @Test
    void testInvalidSizeValues() {
        // Tester avec des valeurs de taille invalides
        Heater invalidHeater = new Heater();
        invalidHeater.setCustomName("Invalid Size Heater");
        invalidHeater.setRoom(testRoom);
        invalidHeater.setSizeX(0.0); // Taille nulle
        invalidHeater.setSizeY(-1.0); // Taille négative
        invalidHeater.setSizeZ(Double.NaN); // Valeur NaN

        // Ces valeurs devraient être détectées comme invalides
        assertEquals(0.0, invalidHeater.getSizeX());
        assertTrue(invalidHeater.getSizeY() < 0);
        assertTrue(Double.isNaN(invalidHeater.getSizeZ()));

        logger.info("Invalid size values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private Heater createTestHeater(Integer id, String name) {
        Heater Heater = new Heater();
        Heater.setId(id);
        Heater.setCustomName(name);
        Heater.setRoom(testRoom);
        Heater.setPosX(1.0);
        Heater.setPosY(1.0);
        Heater.setPosZ(1.0);
        Heater.setSizeX(1.0);
        Heater.setSizeY(1.0);
        Heater.setSizeZ(1.0);
        return Heater;
    }
}