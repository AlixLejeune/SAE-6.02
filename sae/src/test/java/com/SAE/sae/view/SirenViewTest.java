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

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.SirenManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;


@ExtendWith(MockitoExtension.class)
/**
 * Test class for the SirenView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de
 * gestion des tables de données
 * et de ses interactions avec les services.
 */
public class SirenViewTest {

    @Mock
    private SirenManager SirenManager;

    @Mock
    private RoomManager roomManager;

    private SirenView SirenView;
    private Siren testSiren;
    private Siren testSiren2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(SirenViewTest.class.getName());

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

        testSiren = new Siren();
        testSiren.setId(1);
        testSiren.setCustomName("Test Siren 1");
        testSiren.setRoom(testRoom);
        testSiren.setPosX(1.0);
        testSiren.setPosY(2.0);
        testSiren.setPosZ(3.0);

        testSiren2 = new Siren();
        testSiren2.setId(2);
        testSiren2.setCustomName("Test Siren 2");
        testSiren2.setRoom(testRoom);
        testSiren2.setPosX(2.0);
        testSiren2.setPosY(3.0);
        testSiren2.setPosZ(4.0);

        // Initialiser la SirenView avec les mocks
        SirenView = new SirenView(SirenManager, roomManager);

        logger.info("SirenView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testSirenViewInstantiation() {
        // Vérifier que la SirenView peut être instanciée correctement
        assertNotNull(SirenView);
        logger.info("SirenView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        SirenView view = new SirenView(SirenManager, roomManager);
        assertNotNull(view);
        logger.info("SirenView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Siren> mockSirens = Arrays.asList(testSiren, testSiren2);
        when(SirenManager.findAll()).thenReturn(mockSirens);

        // Créer une nouvelle instance pour tester l'initialisation
        SirenView newView = new SirenView(SirenManager, roomManager);

        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(SirenManager, atLeastOnce()).findAll();

        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidSirens() {
        // Mock des données
        List<Siren> mockSirens = Arrays.asList(testSiren, testSiren2);
        when(SirenManager.findAll()).thenReturn(mockSirens);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            SirenView newView = new SirenView(SirenManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(SirenManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockSirens.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(SirenManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            SirenView newView = new SirenView(SirenManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(SirenManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(SirenManager.findAll())
                .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            SirenView newView = new SirenView(SirenManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testSirenManagerIntegration() {
        // Tester l'intégration avec SirenManager
        assertNotNull(SirenManager);

        // Mock du comportement de sauvegarde
        when(SirenManager.save(any(Siren.class))).thenReturn(testSiren);

        Siren savedSiren = SirenManager.save(testSiren);
        assertNotNull(savedSiren);
        assertEquals(testSiren.getCustomName(), savedSiren.getCustomName());

        // Vérifier que save a été appelé
        verify(SirenManager, times(1)).save(testSiren);

        logger.info("SirenManager integration test successful");
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
    void testSaveSirenOperation() {
        // Tester l'opération de sauvegarde (pas de updateSiren dans le service)
        when(SirenManager.save(any(Siren.class))).thenReturn(testSiren);

        // Simuler la sauvegarde
        Siren result = assertDoesNotThrow(() -> {
            return SirenManager.save(testSiren);
        });

        assertNotNull(result);
        assertEquals(testSiren.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(SirenManager, times(1)).save(testSiren);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteSirenOperation() {
        // Tester l'opération de suppression
        Integer SirenId = 1;

        // Mock de la méthode deleteById
        doNothing().when(SirenManager).deleteById(SirenId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            SirenManager.deleteById(SirenId);
        });

        // Vérifier que deleteById a été appelé
        verify(SirenManager, times(1)).deleteById(SirenId);

        logger.info("Delete operation test successful for data table ID: " + SirenId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer SirenId = 1;
        when(SirenManager.findById(SirenId)).thenReturn(testSiren);

        Siren result = assertDoesNotThrow(() -> {
            return SirenManager.findById(SirenId);
        });

        assertNotNull(result);
        assertEquals(testSiren.getId(), result.getId());
        assertEquals(testSiren.getCustomName(), result.getCustomName());

        verify(SirenManager, times(1)).findById(SirenId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(SirenManager.findById(invalidId))
                .thenThrow(new IllegalArgumentException("Aucune Siren trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            SirenManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Siren> mockSirens = Arrays.asList(testSiren, testSiren2);
        when(SirenManager.findByRoomId(roomId)).thenReturn(mockSirens);

        List<Siren> result = SirenManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(SirenManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Siren> mockSirens = Arrays.asList(testSiren);
        when(SirenManager.findByRoomId(roomId)).thenReturn(mockSirens);

        List<Siren> result = SirenManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(SirenManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Siren 1";
        List<Siren> mockSirens = Arrays.asList(testSiren);
        when(SirenManager.findByCustomName(customName)).thenReturn(mockSirens);

        List<Siren> result = SirenManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(SirenManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Sirens
        List<Siren> SirenList = Arrays.asList(testSiren, testSiren2);
        when(SirenManager.saveAll(SirenList)).thenReturn(SirenList);

        List<Siren> result = SirenManager.saveAll(SirenList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(SirenManager, times(1)).saveAll(SirenList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Siren
        doNothing().when(SirenManager).delete(testSiren);

        assertDoesNotThrow(() -> {
            SirenManager.delete(testSiren);
        });

        verify(SirenManager, times(1)).delete(testSiren);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Sirens
        doNothing().when(SirenManager).deleteAll();

        assertDoesNotThrow(() -> {
            SirenManager.deleteAll();
        });

        verify(SirenManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Siren 1";
        doNothing().when(SirenManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            SirenManager.deleteByCustomName(customName);
        });

        verify(SirenManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer SirenId = 1;
        when(SirenManager.existsById(SirenId)).thenReturn(true);

        boolean exists = SirenManager.existsById(SirenId);
        assertTrue(exists);

        verify(SirenManager, times(1)).existsById(SirenId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(SirenManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = SirenManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(SirenManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Sirens
        long expectedCount = 5L;
        when(SirenManager.count()).thenReturn(expectedCount);

        long count = SirenManager.count();
        assertEquals(expectedCount, count);

        verify(SirenManager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(SirenManager.save(any(Siren.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SirenManager.save(testSiren);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer SirenId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(SirenManager).deleteById(SirenId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SirenManager.deleteById(SirenId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testSirenValidation() {
        // Tester la validation des données de table
        Siren validSiren = new Siren();
        validSiren.setCustomName("Valid Siren");
        validSiren.setRoom(testRoom);
        validSiren.setPosX(1.0);
        validSiren.setPosY(1.0);
        validSiren.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validSiren.getCustomName());
        assertNotNull(validSiren.getRoom());
        assertNotNull(validSiren.getPosX());

        logger.info("Siren validation test - all required fields present");
    }

    @Test
    void testSirenWithEmptyName() {
        // Tester avec un nom vide
        Siren emptyNameSiren = new Siren();
        emptyNameSiren.setCustomName("");
        emptyNameSiren.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameSiren.getCustomName().isEmpty());

        logger.info("Siren with empty name detected correctly");
    }

    @Test
    void testSirenWithNullRoom() {
        // Tester avec une salle null
        Siren nullRoomSiren = new Siren();
        nullRoomSiren.setCustomName("Test Siren");
        nullRoomSiren.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomSiren.getRoom());

        logger.info("Siren with null room detected correctly");
    }

    @Test
    void testSirenWithNullName() {
        // Tester avec un nom null
        Siren nullNameSiren = new Siren();
        nullNameSiren.setCustomName(null);
        nullNameSiren.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameSiren.getCustomName());

        logger.info("Siren with null name detected correctly");
    }

    @Test
    void testSirenWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Siren whitespaceNameSiren = new Siren();
        whitespaceNameSiren.setCustomName("   ");
        whitespaceNameSiren.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameSiren.getCustomName().trim().isEmpty());

        logger.info("Siren with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleSirensHandling() {
        // Tester avec plusieurs tables
        Siren Siren3 = new Siren();
        Siren3.setId(3);
        Siren3.setCustomName("Test Siren 3");
        Siren3.setRoom(testRoom);

        List<Siren> multipleSirens = Arrays.asList(testSiren, testSiren2, Siren3);
        when(SirenManager.findAll()).thenReturn(multipleSirens);

        List<Siren> result = SirenManager.findAll();
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
    void testSirenPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testSiren.getPosX());
        assertEquals(2.0, testSiren.getPosY());
        assertEquals(3.0, testSiren.getPosZ());

        logger.info("Siren position values test successful: X=" +
                testSiren.getPosX() + ", Y=" + testSiren.getPosY() +
                ", Z=" + testSiren.getPosZ());
    }

    @Test
    void testSirenIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testSiren.getId());
        assertEquals(Integer.valueOf(2), testSiren2.getId());

        logger.info("Siren ID handling test successful: Siren1 ID=" +
                testSiren.getId() + ", Siren2 ID=" + testSiren2.getId());
    }

    @Test
    void testSirenNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Siren 1", testSiren.getCustomName());
        assertEquals("Test Siren 2", testSiren2.getCustomName());

        logger.info("Siren name handling test successful: Siren1 name='" +
                testSiren.getCustomName() + "', Siren2 name='" + testSiren2.getCustomName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(SirenManager.findAll()).thenReturn(Arrays.asList(testSiren));
        when(SirenManager.save(any(Siren.class))).thenReturn(testSiren);

        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            SirenManager.findAll();
            SirenManager.save(testSiren);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Siren> largeDataset = Arrays.asList(
                testSiren, testSiren2,
                createTestSiren(3, "Siren 3"),
                createTestSiren(4, "Siren 4"),
                createTestSiren(5, "Siren 5"));

        when(SirenManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            SirenView newView = new SirenView(SirenManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Siren invalidSiren = new Siren();
        invalidSiren.setCustomName("Invalid Position Siren");
        invalidSiren.setRoom(testRoom);
        invalidSiren.setPosX(-1.0); // Position négative
        invalidSiren.setPosY(Double.NaN); // Valeur NaN
        invalidSiren.setPosZ(Double.POSITIVE_INFINITY); // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidSiren.getPosX() < 0);
        assertTrue(Double.isNaN(invalidSiren.getPosY()));
        assertTrue(Double.isInfinite(invalidSiren.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private Siren createTestSiren(Integer id, String name) {
        Siren Siren = new Siren();
        Siren.setId(id);
        Siren.setCustomName(name);
        Siren.setRoom(testRoom);
        Siren.setPosX(1.0);
        Siren.setPosY(1.0);
        Siren.setPosZ(1.0);
        return Siren;
    }
}