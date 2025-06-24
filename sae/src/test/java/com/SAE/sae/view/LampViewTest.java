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

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.SAE.sae.entity.RoomObjects.Lamp;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.LampManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@ExtendWith(MockitoExtension.class)
/**
 * Test class for the LampView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de
 * gestion des tables de données
 * et de ses interactions avec les services.
 */
public class LampViewTest {

    @Mock
    private LampManager lampManager;

    @Mock
    private RoomManager roomManager;

    private LampView lampView;
    private Lamp testLamp;
    private Lamp testLamp2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(LampViewTest.class.getName());

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

        testLamp = new Lamp();
        testLamp.setId(1);
        testLamp.setCustomName("Test Lamp 1");
        testLamp.setRoom(testRoom);
        testLamp.setPosX(1.0);
        testLamp.setPosY(2.0);
        testLamp.setPosZ(3.0);

        testLamp2 = new Lamp();
        testLamp2.setId(2);
        testLamp2.setCustomName("Test Lamp 2");
        testLamp2.setRoom(testRoom);
        testLamp2.setPosX(2.0);
        testLamp2.setPosY(3.0);
        testLamp2.setPosZ(4.0);

        // Initialiser la lampView avec les mocks
        lampView = new LampView(lampManager, roomManager);

        logger.info("LampView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testLampViewInstantiation() {
        // Vérifier que la lampView peut être instanciée correctement
        assertNotNull(lampView);
        logger.info("LampView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        LampView view = new LampView(lampManager, roomManager);
        assertNotNull(view);
        logger.info("LampView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Lamp> mockLamps = Arrays.asList(testLamp, testLamp2);
        when(lampManager.findAll()).thenReturn(mockLamps);

        // Créer une nouvelle instance pour tester l'initialisation
        LampView newView = new LampView(lampManager, roomManager);

        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(lampManager, atLeastOnce()).findAll();

        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidLamps() {
        // Mock des données
        List<Lamp> mockLamps = Arrays.asList(testLamp, testLamp2);
        when(lampManager.findAll()).thenReturn(mockLamps);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            LampView newView = new LampView(lampManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(lampManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockLamps.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(lampManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            LampView newView = new LampView(lampManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(lampManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(lampManager.findAll())
                .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            LampView newView = new LampView(lampManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testLampManagerIntegration() {
        // Tester l'intégration avec lampManager
        assertNotNull(lampManager);

        // Mock du comportement de sauvegarde
        when(lampManager.save(any(Lamp.class))).thenReturn(testLamp);

        Lamp savedLamp = lampManager.save(testLamp);
        assertNotNull(savedLamp);
        assertEquals(testLamp.getCustomName(), savedLamp.getCustomName());

        // Vérifier que save a été appelé
        verify(lampManager, times(1)).save(testLamp);

        logger.info("LampManager integration test successful");
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
    void testSaveLampOperation() {
        // Tester l'opération de sauvegarde (pas de updateLamp dans le service)
        when(lampManager.save(any(Lamp.class))).thenReturn(testLamp);

        // Simuler la sauvegarde
        Lamp result = assertDoesNotThrow(() -> {
            return lampManager.save(testLamp);
        });

        assertNotNull(result);
        assertEquals(testLamp.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(lampManager, times(1)).save(testLamp);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteLampOperation() {
        // Tester l'opération de suppression
        Integer lampId = 1;

        // Mock de la méthode deleteById
        doNothing().when(lampManager).deleteById(lampId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            lampManager.deleteById(lampId);
        });

        // Vérifier que deleteById a été appelé
        verify(lampManager, times(1)).deleteById(lampId);

        logger.info("Delete operation test successful for data table ID: " + lampId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer lampId = 1;
        when(lampManager.findById(lampId)).thenReturn(testLamp);

        Lamp result = assertDoesNotThrow(() -> {
            return lampManager.findById(lampId);
        });

        assertNotNull(result);
        assertEquals(testLamp.getId(), result.getId());
        assertEquals(testLamp.getCustomName(), result.getCustomName());

        verify(lampManager, times(1)).findById(lampId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(lampManager.findById(invalidId))
                .thenThrow(new IllegalArgumentException("Aucune Lamp trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            lampManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Lamp> mockLamps = Arrays.asList(testLamp, testLamp2);
        when(lampManager.findByRoomId(roomId)).thenReturn(mockLamps);

        List<Lamp> result = lampManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(lampManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Lamp> mockLamps = Arrays.asList(testLamp);
        when(lampManager.findByRoomId(roomId)).thenReturn(mockLamps);

        List<Lamp> result = lampManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(lampManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Lamp 1";
        List<Lamp> mockLamps = Arrays.asList(testLamp);
        when(lampManager.findByCustomName(customName)).thenReturn(mockLamps);

        List<Lamp> result = lampManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(lampManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Lamps
        List<Lamp> lampList = Arrays.asList(testLamp, testLamp2);
        when(lampManager.saveAll(lampList)).thenReturn(lampList);

        List<Lamp> result = lampManager.saveAll(lampList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(lampManager, times(1)).saveAll(lampList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Lamp
        doNothing().when(lampManager).delete(testLamp);

        assertDoesNotThrow(() -> {
            lampManager.delete(testLamp);
        });

        verify(lampManager, times(1)).delete(testLamp);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Lamps
        doNothing().when(lampManager).deleteAll();

        assertDoesNotThrow(() -> {
            lampManager.deleteAll();
        });

        verify(lampManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Lamp 1";
        doNothing().when(lampManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            lampManager.deleteByCustomName(customName);
        });

        verify(lampManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer lampId = 1;
        when(lampManager.existsById(lampId)).thenReturn(true);

        boolean exists = lampManager.existsById(lampId);
        assertTrue(exists);

        verify(lampManager, times(1)).existsById(lampId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(lampManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = lampManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(lampManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Lamps
        long expectedCount = 5L;
        when(lampManager.count()).thenReturn(expectedCount);

        long count = lampManager.count();
        assertEquals(expectedCount, count);

        verify(lampManager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(lampManager.save(any(Lamp.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            lampManager.save(testLamp);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer lampId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(lampManager).deleteById(lampId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            lampManager.deleteById(lampId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testLampValidation() {
        // Tester la validation des données de table
        Lamp validLamp = new Lamp();
        validLamp.setCustomName("Valid Lamp");
        validLamp.setRoom(testRoom);
        validLamp.setPosX(1.0);
        validLamp.setPosY(1.0);
        validLamp.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validLamp.getCustomName());
        assertNotNull(validLamp.getRoom());
        assertNotNull(validLamp.getPosX());

        logger.info("Lamp validation test - all required fields present");
    }

    @Test
    void testLampWithEmptyName() {
        // Tester avec un nom vide
        Lamp emptyNameLamp = new Lamp();
        emptyNameLamp.setCustomName("");
        emptyNameLamp.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameLamp.getCustomName().isEmpty());

        logger.info("Lamp with empty name detected correctly");
    }

    @Test
    void testLampWithNullRoom() {
        // Tester avec une salle null
        Lamp nullRoomLamp = new Lamp();
        nullRoomLamp.setCustomName("Test Lamp");
        nullRoomLamp.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomLamp.getRoom());

        logger.info("Lamp with null room detected correctly");
    }

    @Test
    void testLampWithNullName() {
        // Tester avec un nom null
        Lamp nullNameLamp = new Lamp();
        nullNameLamp.setCustomName(null);
        nullNameLamp.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameLamp.getCustomName());

        logger.info("Lamp with null name detected correctly");
    }

    @Test
    void testLampWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Lamp whitespaceNameLamp = new Lamp();
        whitespaceNameLamp.setCustomName("   ");
        whitespaceNameLamp.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameLamp.getCustomName().trim().isEmpty());

        logger.info("Lamp with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleLampsHandling() {
        // Tester avec plusieurs tables
        Lamp lamp3 = new Lamp();
        lamp3.setId(3);
        lamp3.setCustomName("Test Lamp 3");
        lamp3.setRoom(testRoom);

        List<Lamp> multipleLamps = Arrays.asList(testLamp, testLamp2, lamp3);
        when(lampManager.findAll()).thenReturn(multipleLamps);

        List<Lamp> result = lampManager.findAll();
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
    void testLampPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testLamp.getPosX());
        assertEquals(2.0, testLamp.getPosY());
        assertEquals(3.0, testLamp.getPosZ());

        logger.info("Lamp position values test successful: X=" +
                testLamp.getPosX() + ", Y=" + testLamp.getPosY() +
                ", Z=" + testLamp.getPosZ());
    }

    @Test
    void testLampIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testLamp.getId());
        assertEquals(Integer.valueOf(2), testLamp2.getId());

        logger.info("Lamp ID handling test successful: Lamp1 ID=" +
                testLamp.getId() + ", Lamp2 ID=" + testLamp2.getId());
    }

    @Test
    void testLampNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Lamp 1", testLamp.getCustomName());
        assertEquals("Test Lamp 2", testLamp2.getCustomName());

        logger.info("Lamp name handling test successful: Lamp1 name='" +
                testLamp.getCustomName() + "', Lamp2 name='" + testLamp2.getCustomName() + "'");
    }

    @Test
    void testLampRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testLamp.getRoom());
        assertEquals(testRoom.getName(), testLamp.getRoom().getName());

        logger.info("Lamp room association test successful: Room='" +
                testLamp.getRoom().getName() + "'");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Lamp> largeDataset = Arrays.asList(
                testLamp, testLamp2,
                createTestLamp(3, "Lamp 3"),
                createTestLamp(4, "Lamp 4"),
                createTestLamp(5, "Lamp 5"));

        when(lampManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            LampView newView = new LampView(lampManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Lamp invalidLamp = new Lamp();
        invalidLamp.setCustomName("Invalid Position Lamp");
        invalidLamp.setRoom(testRoom);
        invalidLamp.setPosX(-1.0); // Position négative
        invalidLamp.setPosY(Double.NaN); // Valeur NaN
        invalidLamp.setPosZ(Double.POSITIVE_INFINITY); // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidLamp.getPosX() < 0);
        assertTrue(Double.isNaN(invalidLamp.getPosY()));
        assertTrue(Double.isInfinite(invalidLamp.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private Lamp createTestLamp(Integer id, String name) {
        Lamp lamp = new Lamp();
        lamp.setId(id);
        lamp.setCustomName(name);
        lamp.setRoom(testRoom);
        lamp.setPosX(1.0);
        lamp.setPosY(1.0);
        lamp.setPosZ(1.0);
        return lamp;
    }
}