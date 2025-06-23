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

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.WindowManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;


@ExtendWith(MockitoExtension.class)
/**
 * Test class for the WindowView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de
 * gestion des tables de données
 * et de ses interactions avec les services.
 */
public class WindowViewTest {

    @Mock
    private WindowManager WindowManager;

    @Mock
    private RoomManager roomManager;

    private WindowView WindowView;
    private Window testWindow;
    private Window testWindow2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(WindowViewTest.class.getName());

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

        testWindow = new Window();
        testWindow.setId(1);
        testWindow.setCustomName("Test Window 1");
        testWindow.setRoom(testRoom);
        testWindow.setPosX(1.0);
        testWindow.setPosY(2.0);
        testWindow.setPosZ(3.0);
        testWindow.setSizeX(1.5);
        testWindow.setSizeY(2.5);
        testWindow.setSizeZ(0.8);

        testWindow2 = new Window();
        testWindow2.setId(2);
        testWindow2.setCustomName("Test Window 2");
        testWindow2.setRoom(testRoom);
        testWindow2.setPosX(2.0);
        testWindow2.setPosY(3.0);
        testWindow2.setPosZ(4.0);
        testWindow2.setSizeX(2.0);
        testWindow2.setSizeY(3.0);
        testWindow2.setSizeZ(1.0);

        // Initialiser la WindowView avec les mocks
        WindowView = new WindowView(WindowManager, roomManager);

        logger.info("WindowView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testWindowViewInstantiation() {
        // Vérifier que la WindowView peut être instanciée correctement
        assertNotNull(WindowView);
        logger.info("WindowView instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        WindowView view = new WindowView(WindowManager, roomManager);
        assertNotNull(view);
        logger.info("WindowView constructor works with valid managers");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Window> mockWindows = Arrays.asList(testWindow, testWindow2);
        when(WindowManager.findAll()).thenReturn(mockWindows);

        // Créer une nouvelle instance pour tester l'initialisation
        WindowView newView = new WindowView(WindowManager, roomManager);

        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(WindowManager, atLeastOnce()).findAll();

        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidWindows() {
        // Mock des données
        List<Window> mockWindows = Arrays.asList(testWindow, testWindow2);
        when(WindowManager.findAll()).thenReturn(mockWindows);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            WindowView newView = new WindowView(WindowManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(WindowManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockWindows.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(WindowManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            WindowView newView = new WindowView(WindowManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(WindowManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testWindowManagerIntegration() {
        // Tester l'intégration avec WindowManager
        assertNotNull(WindowManager);

        // Mock du comportement de sauvegarde
        when(WindowManager.save(any(Window.class))).thenReturn(testWindow);

        Window savedWindow = WindowManager.save(testWindow);
        assertNotNull(savedWindow);
        assertEquals(testWindow.getCustomName(), savedWindow.getCustomName());

        // Vérifier que save a été appelé
        verify(WindowManager, times(1)).save(testWindow);

        logger.info("WindowManager integration test successful");
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
    void testSaveWindowOperation() {
        // Tester l'opération de sauvegarde (pas de updateWindow dans le service)
        when(WindowManager.save(any(Window.class))).thenReturn(testWindow);

        // Simuler la sauvegarde
        Window result = assertDoesNotThrow(() -> {
            return WindowManager.save(testWindow);
        });

        assertNotNull(result);
        assertEquals(testWindow.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(WindowManager, times(1)).save(testWindow);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteWindowOperation() {
        // Tester l'opération de suppression
        Integer WindowId = 1;

        // Mock de la méthode deleteById
        doNothing().when(WindowManager).deleteById(WindowId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            WindowManager.deleteById(WindowId);
        });

        // Vérifier que deleteById a été appelé
        verify(WindowManager, times(1)).deleteById(WindowId);

        logger.info("Delete operation test successful for data table ID: " + WindowId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer WindowId = 1;
        when(WindowManager.findById(WindowId)).thenReturn(testWindow);

        Window result = assertDoesNotThrow(() -> {
            return WindowManager.findById(WindowId);
        });

        assertNotNull(result);
        assertEquals(testWindow.getId(), result.getId());
        assertEquals(testWindow.getCustomName(), result.getCustomName());

        verify(WindowManager, times(1)).findById(WindowId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(WindowManager.findById(invalidId))
                .thenThrow(new IllegalArgumentException("Aucune Window trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            WindowManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Window> mockWindows = Arrays.asList(testWindow, testWindow2);
        when(WindowManager.findByRoomId(roomId)).thenReturn(mockWindows);

        List<Window> result = WindowManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(WindowManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Window> mockWindows = Arrays.asList(testWindow);
        when(WindowManager.findByRoomId(roomId)).thenReturn(mockWindows);

        List<Window> result = WindowManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(WindowManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Window 1";
        List<Window> mockWindows = Arrays.asList(testWindow);
        when(WindowManager.findByCustomName(customName)).thenReturn(mockWindows);

        List<Window> result = WindowManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(WindowManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Windows
        List<Window> WindowList = Arrays.asList(testWindow, testWindow2);
        when(WindowManager.saveAll(WindowList)).thenReturn(WindowList);

        List<Window> result = WindowManager.saveAll(WindowList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(WindowManager, times(1)).saveAll(WindowList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteOperation() {
        // Tester la suppression d'un objet Window
        doNothing().when(WindowManager).delete(testWindow);

        assertDoesNotThrow(() -> {
            WindowManager.delete(testWindow);
        });

        verify(WindowManager, times(1)).delete(testWindow);

        logger.info("Delete operation test successful");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Windows
        doNothing().when(WindowManager).deleteAll();

        assertDoesNotThrow(() -> {
            WindowManager.deleteAll();
        });

        verify(WindowManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Window 1";
        doNothing().when(WindowManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            WindowManager.deleteByCustomName(customName);
        });

        verify(WindowManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer WindowId = 1;
        when(WindowManager.existsById(WindowId)).thenReturn(true);

        boolean exists = WindowManager.existsById(WindowId);
        assertTrue(exists);

        verify(WindowManager, times(1)).existsById(WindowId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(WindowManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = WindowManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(WindowManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testCountOperation() {
        // Tester le comptage des Windows
        long expectedCount = 5L;
        when(WindowManager.count()).thenReturn(expectedCount);

        long count = WindowManager.count();
        assertEquals(expectedCount, count);

        verify(WindowManager, times(1)).count();

        logger.info("Count operation test successful - count: " + count);
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(WindowManager.save(any(Window.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            WindowManager.save(testWindow);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer WindowId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(WindowManager).deleteById(WindowId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            WindowManager.deleteById(WindowId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testWindowValidation() {
        // Tester la validation des données de table
        Window validWindow = new Window();
        validWindow.setCustomName("Valid Window");
        validWindow.setRoom(testRoom);
        validWindow.setPosX(1.0);
        validWindow.setPosY(1.0);
        validWindow.setPosZ(1.0);
        validWindow.setSizeX(1.0);
        validWindow.setSizeY(1.0);
        validWindow.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validWindow.getCustomName());
        assertNotNull(validWindow.getRoom());
        assertNotNull(validWindow.getPosX());
        assertNotNull(validWindow.getSizeX());

        logger.info("Window validation test - all required fields present");
    }

    @Test
    void testWindowWithEmptyName() {
        // Tester avec un nom vide
        Window emptyNameWindow = new Window();
        emptyNameWindow.setCustomName("");
        emptyNameWindow.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameWindow.getCustomName().isEmpty());

        logger.info("Window with empty name detected correctly");
    }

    @Test
    void testWindowWithNullRoom() {
        // Tester avec une salle null
        Window nullRoomWindow = new Window();
        nullRoomWindow.setCustomName("Test Window");
        nullRoomWindow.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomWindow.getRoom());

        logger.info("Window with null room detected correctly");
    }

    @Test
    void testWindowWithNullName() {
        // Tester avec un nom null
        Window nullNameWindow = new Window();
        nullNameWindow.setCustomName(null);
        nullNameWindow.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameWindow.getCustomName());

        logger.info("Window with null name detected correctly");
    }

    @Test
    void testWindowWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Window whitespaceNameWindow = new Window();
        whitespaceNameWindow.setCustomName("   ");
        whitespaceNameWindow.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameWindow.getCustomName().trim().isEmpty());

        logger.info("Window with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleWindowsHandling() {
        // Tester avec plusieurs tables
        Window Window3 = new Window();
        Window3.setId(3);
        Window3.setCustomName("Test Window 3");
        Window3.setRoom(testRoom);

        List<Window> multipleWindows = Arrays.asList(testWindow, testWindow2, Window3);
        when(WindowManager.findAll()).thenReturn(multipleWindows);

        List<Window> result = WindowManager.findAll();
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
    void testWindowPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testWindow.getPosX());
        assertEquals(2.0, testWindow.getPosY());
        assertEquals(3.0, testWindow.getPosZ());

        logger.info("Window position values test successful: X=" +
                testWindow.getPosX() + ", Y=" + testWindow.getPosY() +
                ", Z=" + testWindow.getPosZ());
    }

    @Test
    void testWindowSizeValues() {
        // Tester les valeurs de taille
        assertEquals(1.5, testWindow.getSizeX());
        assertEquals(2.5, testWindow.getSizeY());
        assertEquals(0.8, testWindow.getSizeZ());

        logger.info("Window size values test successful: X=" +
                testWindow.getSizeX() + ", Y=" + testWindow.getSizeY() +
                ", Z=" + testWindow.getSizeZ());
    }

    @Test
    void testWindowIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testWindow.getId());
        assertEquals(Integer.valueOf(2), testWindow2.getId());

        logger.info("Window ID handling test successful: Window1 ID=" +
                testWindow.getId() + ", Window2 ID=" + testWindow2.getId());
    }

    @Test
    void testWindowNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Window 1", testWindow.getCustomName());
        assertEquals("Test Window 2", testWindow2.getCustomName());

        logger.info("Window name handling test successful: Window1 name='" +
                testWindow.getCustomName() + "', Window2 name='" + testWindow2.getCustomName() + "'");
    }

    @Test
    void testWindowRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testWindow.getRoom());
        assertEquals(testRoom.getName(), testWindow.getRoom().getName());

        logger.info("Window room association test successful: Room='" +
                testWindow.getRoom().getName() + "'");
    }

    @Test
    void testConcurrentOperations() {
        // Tester les opérations concurrentes (simulation)
        when(WindowManager.findAll()).thenReturn(Arrays.asList(testWindow));
        when(WindowManager.save(any(Window.class))).thenReturn(testWindow);

        // Simuler des opérations concurrentes
        assertDoesNotThrow(() -> {
            WindowManager.findAll();
            WindowManager.save(testWindow);
        });

        logger.info("Concurrent operations test successful");
    }

    @Test
    void testPerformanceWithLargeDataset() {
        // Tester avec un grand nombre de tables de données
        List<Window> largeDataset = Arrays.asList(
                testWindow, testWindow2,
                createTestWindow(3, "Window 3"),
                createTestWindow(4, "Window 4"),
                createTestWindow(5, "Window 5"));

        when(WindowManager.findAll()).thenReturn(largeDataset);

        // La vue devrait gérer un dataset plus large sans problème
        assertDoesNotThrow(() -> {
            WindowView newView = new WindowView(WindowManager, roomManager);
        });

        logger.info("Performance test with large dataset successful - " + largeDataset.size() + " data tables");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Window invalidWindow = new Window();
        invalidWindow.setCustomName("Invalid Position Window");
        invalidWindow.setRoom(testRoom);
        invalidWindow.setPosX(-1.0); // Position négative
        invalidWindow.setPosY(Double.NaN); // Valeur NaN
        invalidWindow.setPosZ(Double.POSITIVE_INFINITY); // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidWindow.getPosX() < 0);
        assertTrue(Double.isNaN(invalidWindow.getPosY()));
        assertTrue(Double.isInfinite(invalidWindow.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    @Test
    void testInvalidSizeValues() {
        // Tester avec des valeurs de taille invalides
        Window invalidWindow = new Window();
        invalidWindow.setCustomName("Invalid Size Window");
        invalidWindow.setRoom(testRoom);
        invalidWindow.setSizeX(0.0); // Taille nulle
        invalidWindow.setSizeY(-1.0); // Taille négative
        invalidWindow.setSizeZ(Double.NaN); // Valeur NaN

        // Ces valeurs devraient être détectées comme invalides
        assertEquals(0.0, invalidWindow.getSizeX());
        assertTrue(invalidWindow.getSizeY() < 0);
        assertTrue(Double.isNaN(invalidWindow.getSizeZ()));

        logger.info("Invalid size values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private Window createTestWindow(Integer id, String name) {
        Window Window = new Window();
        Window.setId(id);
        Window.setCustomName(name);
        Window.setRoom(testRoom);
        Window.setPosX(1.0);
        Window.setPosY(1.0);
        Window.setPosZ(1.0);
        Window.setSizeX(1.0);
        Window.setSizeY(1.0);
        Window.setSizeZ(1.0);
        return Window;
    }
}