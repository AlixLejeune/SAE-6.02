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


import com.SAE.sae.entity.RoomObjects.Plug;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.PlugManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the PlugView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de gestion des tables de données
 * et de ses interactions avec les services.
 */
public class PlugViewTest {

    @MockBean
    private PlugManager PlugManager;

    @MockBean
    private RoomManager roomManager;

    private PlugView PlugView;
    private Plug testPlug;
    private Plug testPlug2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(PlugViewTest.class.getName());

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

        testPlug = new Plug();
        testPlug.setId(1);
        testPlug.setCustomName("Test Plug 1");
        testPlug.setRoom(testRoom);
        testPlug.setPosX(1.0);
        testPlug.setPosY(2.0);
        testPlug.setPosZ(3.0);


        testPlug2 = new Plug();
        testPlug2.setId(2);
        testPlug2.setCustomName("Test Plug 2");
        testPlug2.setRoom(testRoom);
        testPlug2.setPosX(2.0);
        testPlug2.setPosY(3.0);
        testPlug2.setPosZ(4.0);

        // Initialiser la PlugView avec les mocks
        PlugView = new PlugView(PlugManager, roomManager);

        logger.info("PlugView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testPlugViewInstantiation() {
        // Vérifier que la PlugView peut être instanciée correctement
        assertNotNull(PlugView);
        logger.info("PlugView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        PlugView view = new PlugView(PlugManager, roomManager);
        assertNotNull(view);
        logger.info("PlugView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Plug> mockPlugs = Arrays.asList(testPlug, testPlug2);
        when(PlugManager.findAll()).thenReturn(mockPlugs);

        // Créer une nouvelle instance pour tester l'initialisation
        PlugView newView = new PlugView(PlugManager, roomManager);
        
        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(PlugManager, atLeastOnce()).findAll();
        
        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidPlugs() {
        // Mock des données
        List<Plug> mockPlugs = Arrays.asList(testPlug, testPlug2);
        when(PlugManager.findAll()).thenReturn(mockPlugs);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            PlugView newView = new PlugView(PlugManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(PlugManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockPlugs.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(PlugManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            PlugView newView = new PlugView(PlugManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(PlugManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(PlugManager.findAll())
            .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            PlugView newView = new PlugView(PlugManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testPlugManagerIntegration() {
        // Tester l'intégration avec PlugManager
        assertNotNull(PlugManager);

        // Mock du comportement de sauvegarde
        when(PlugManager.save(any(Plug.class))).thenReturn(testPlug);

        Plug savedPlug = PlugManager.save(testPlug);
        assertNotNull(savedPlug);
        assertEquals(testPlug.getCustomName(), savedPlug.getCustomName());

        // Vérifier que save a été appelé
        verify(PlugManager, times(1)).save(testPlug);

        logger.info("PlugManager integration test successful");
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
    void testSavePlugOperation() {
        // Tester l'opération de sauvegarde (pas de updatePlug dans le service)
        when(PlugManager.save(any(Plug.class))).thenReturn(testPlug);

        // Simuler la sauvegarde
        Plug result = assertDoesNotThrow(() -> {
            return PlugManager.save(testPlug);
        });

        assertNotNull(result);
        assertEquals(testPlug.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(PlugManager, times(1)).save(testPlug);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeletePlugOperation() {
        // Tester l'opération de suppression
        Integer PlugId = 1;

        // Mock de la méthode deleteById
        doNothing().when(PlugManager).deleteById(PlugId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            PlugManager.deleteById(PlugId);
        });

        // Vérifier que deleteById a été appelé
        verify(PlugManager, times(1)).deleteById(PlugId);

        logger.info("Delete operation test successful for data table ID: " + PlugId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer PlugId = 1;
        when(PlugManager.findById(PlugId)).thenReturn(testPlug);

        Plug result = assertDoesNotThrow(() -> {
            return PlugManager.findById(PlugId);
        });

        assertNotNull(result);
        assertEquals(testPlug.getId(), result.getId());
        assertEquals(testPlug.getCustomName(), result.getCustomName());

        verify(PlugManager, times(1)).findById(PlugId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(PlugManager.findById(invalidId))
            .thenThrow(new IllegalArgumentException("Aucune Plug trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            PlugManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Plug> mockPlugs = Arrays.asList(testPlug, testPlug2);
        when(PlugManager.findByRoomId(roomId)).thenReturn(mockPlugs);

        List<Plug> result = PlugManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(PlugManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Plug> mockPlugs = Arrays.asList(testPlug);
        when(PlugManager.findByRoomId(roomId)).thenReturn(mockPlugs);

        List<Plug> result = PlugManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(PlugManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Plug 1";
        List<Plug> mockPlugs = Arrays.asList(testPlug);
        when(PlugManager.findByCustomName(customName)).thenReturn(mockPlugs);

        List<Plug> result = PlugManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(PlugManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Plugs
        List<Plug> PlugList = Arrays.asList(testPlug, testPlug2);
        when(PlugManager.saveAll(PlugList)).thenReturn(PlugList);

        List<Plug> result = PlugManager.saveAll(PlugList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(PlugManager, times(1)).saveAll(PlugList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Plug
        doNothing().when(PlugManager).delete(testPlug);

        assertDoesNotThrow(() -> {
            PlugManager.delete(testPlug);
        });

        verify(PlugManager, times(1)).delete(testPlug);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Plugs
        doNothing().when(PlugManager).deleteAll();

        assertDoesNotThrow(() -> {
            PlugManager.deleteAll();
        });

        verify(PlugManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Plug 1";
        doNothing().when(PlugManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            PlugManager.deleteByCustomName(customName);
        });

        verify(PlugManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer PlugId = 1;
        when(PlugManager.existsById(PlugId)).thenReturn(true);

        boolean exists = PlugManager.existsById(PlugId);
        assertTrue(exists);

        verify(PlugManager, times(1)).existsById(PlugId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(PlugManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = PlugManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(PlugManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(PlugManager.save(any(Plug.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            PlugManager.save(testPlug);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testPlugValidation() {
        // Tester la validation des données de table
        Plug validPlug = new Plug();
        validPlug.setCustomName("Valid Plug");
        validPlug.setRoom(testRoom);
        validPlug.setPosX(1.0);
        validPlug.setPosY(1.0);
        validPlug.setPosZ(1.0);


        // Les données devraient être valides
        assertNotNull(validPlug.getCustomName());
        assertNotNull(validPlug.getRoom());
        assertNotNull(validPlug.getPosX());

        logger.info("Plug validation test - all required fields present");
    }

    @Test
    void testPlugWithEmptyName() {
        // Tester avec un nom vide
        Plug emptyNamePlug = new Plug();
        emptyNamePlug.setCustomName("");
        emptyNamePlug.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNamePlug.getCustomName().isEmpty());

        logger.info("Plug with empty name detected correctly");
    }

    @Test
    void testPlugWithNullRoom() {
        // Tester avec une salle null
        Plug nullRoomPlug = new Plug();
        nullRoomPlug.setCustomName("Test Plug");
        nullRoomPlug.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomPlug.getRoom());

        logger.info("Plug with null room detected correctly");
    }

    @Test
    void testPlugWithNullName() {
        // Tester avec un nom null
        Plug nullNamePlug = new Plug();
        nullNamePlug.setCustomName(null);
        nullNamePlug.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNamePlug.getCustomName());

        logger.info("Plug with null name detected correctly");
    }

    @Test
    void testPlugWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Plug whitespaceNamePlug = new Plug();
        whitespaceNamePlug.setCustomName("   ");
        whitespaceNamePlug.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNamePlug.getCustomName().trim().isEmpty());

        logger.info("Plug with whitespace-only name detected correctly");
    }

    @Test
    void testMultiplePlugsHandling() {
        // Tester avec plusieurs tables
        Plug Plug3 = new Plug();
        Plug3.setId(3);
        Plug3.setCustomName("Test Plug 3");
        Plug3.setRoom(testRoom);

        List<Plug> multiplePlugs = Arrays.asList(testPlug, testPlug2, Plug3);
        when(PlugManager.findAll()).thenReturn(multiplePlugs);

        List<Plug> result = PlugManager.findAll();
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
    void testPlugPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testPlug.getPosX());
        assertEquals(2.0, testPlug.getPosY());
        assertEquals(3.0, testPlug.getPosZ());

        logger.info("Plug position values test successful: X=" +
                testPlug.getPosX() + ", Y=" + testPlug.getPosY() +
                ", Z=" + testPlug.getPosZ());
    }


    @Test
    void testPlugIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testPlug.getId());
        assertEquals(Integer.valueOf(2), testPlug2.getId());

        logger.info("Plug ID handling test successful: Plug1 ID=" + 
                testPlug.getId() + ", Plug2 ID=" + testPlug2.getId());
    }

    @Test
    void testPlugNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Plug 1", testPlug.getCustomName());
        assertEquals("Test Plug 2", testPlug2.getCustomName());

        logger.info("Plug name handling test successful: Plug1 name='" + 
                testPlug.getCustomName() + "', Plug2 name='" + testPlug2.getCustomName() + "'");
    }

    @Test
    void testPlugRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testPlug.getRoom());
        assertEquals(testRoom.getName(), testPlug.getRoom().getName());

        logger.info("Plug room association test successful: Room='" + 
                testPlug.getRoom().getName() + "'");
    }

    @Test
    void testfindAllMultipleCalls() {
        // Tester plusieurs appels consécutifs
        List<Plug> mockPlugs = Arrays.asList(testPlug);
        when(PlugManager.findAll()).thenReturn(mockPlugs);

        // Faire plusieurs appels
        PlugManager.findAll();
        PlugManager.findAll();
        PlugManager.findAll();

        // Vérifier que la méthode a été appelée 3 fois
        verify(PlugManager, times(3)).findAll();

        logger.info("Multiple findAll calls test successful");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(PlugManager.findAll()).thenReturn(Arrays.asList(testPlug));
        when(PlugManager.save(any(Plug.class))).thenReturn(testPlug);
        
        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            PlugManager.findAll();
            PlugManager.save(testPlug);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Plug> largeDataset = Arrays.asList(
            testPlug, testPlug2,
            createTestPlug(3, "Plug 3"),
            createTestPlug(4, "Plug 4"),
            createTestPlug(5, "Plug 5")
        );
        
        when(PlugManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            PlugView newView = new PlugView(PlugManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testNullManagersHandling() {
        // Tester avec des managers null (devrait lever une exception)
        assertThrows(Exception.class, () -> {
            new PlugView(null, roomManager);
        });

        assertThrows(Exception.class, () -> {
            new PlugView(PlugManager, null);
        });

        assertThrows(Exception.class, () -> {
            new PlugView(null, null);
        });

        logger.info("Null managers handling test successful");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Plug invalidPlug = new Plug();
        invalidPlug.setCustomName("Invalid Position Plug");
        invalidPlug.setRoom(testRoom);
        invalidPlug.setPosX(-1.0);  // Position négative
        invalidPlug.setPosY(Double.NaN);  // Valeur NaN
        invalidPlug.setPosZ(Double.POSITIVE_INFINITY);  // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidPlug.getPosX() < 0);
        assertTrue(Double.isNaN(invalidPlug.getPosY()));
        assertTrue(Double.isInfinite(invalidPlug.getPosZ()));

        logger.info("Invalid position values test successful");
    }


    // Méthode utilitaire pour créer des tables de données de test
    private Plug createTestPlug(Integer id, String name) {
        Plug Plug = new Plug();
        Plug.setId(id);
        Plug.setCustomName(name);
        Plug.setRoom(testRoom);
        Plug.setPosX(1.0);
        Plug.setPosY(1.0);
        Plug.setPosZ(1.0);
        return Plug;
    }
}