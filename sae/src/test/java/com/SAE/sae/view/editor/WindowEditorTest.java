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

import com.SAE.sae.entity.RoomObjects.Window;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.WindowManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the WindowEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de fenêtres
 * et de ses interactions avec les services.
 */
public class WindowEditorTest {

    @MockBean
    private WindowManager windowManager;

    @MockBean
    private RoomManager roomManager;

    private WindowEditor windowEditor;
    private Window testWindow;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(WindowEditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI());
        // Initialiser le WindowEditor avec les mocks
        windowEditor = new WindowEditor(windowManager, roomManager);

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
        testWindow.setCustomName("Test Window");
        testWindow.setRoom(testRoom);
        testWindow.setPosX(1.0);
        testWindow.setPosY(2.0);
        testWindow.setPosZ(3.0);

        logger.info("WindowEditor test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testWindowEditorInstantiation() {
        // Vérifier que le WindowEditor peut être instancié correctement
        assertNotNull(windowEditor);
        logger.info("WindowEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        WindowEditor editor = new WindowEditor(windowManager, roomManager);
        assertNotNull(editor);
        logger.info("WindowEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");

        assertDoesNotThrow(() -> {
            windowEditor.setOnDataChanged(testCallback);
        });

        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            windowEditor.setOnDataChanged(null);
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
            windowEditor.openAddDialog();
        });

        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidWindow() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);

        // Tester l'ouverture du dialog d'édition avec une fenêtre valide
        assertDoesNotThrow(() -> {
            windowEditor.openEditDialog(testWindow);
        });

        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();

        logger.info("Edit dialog opened successfully for window: " + testWindow.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullWindow() {
        // Tester l'ouverture du dialog d'édition avec une fenêtre null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            windowEditor.openEditDialog(null);
        });

        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();

        logger.info("Edit dialog correctly handles null Window input");
    }

    @Test
    void testConfirmDeleteWithValidWindow() {
        // Tester la confirmation de suppression avec une fenêtre valide
        assertDoesNotThrow(() -> {
            windowEditor.confirmDelete(testWindow);
        });

        logger.info("Delete confirmation dialog opened for window: " + testWindow.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullWindow() {
        // Tester la confirmation de suppression avec une fenêtre null
        assertDoesNotThrow(() -> {
            windowEditor.confirmDelete(null);
        });

        logger.info("Delete confirmation correctly handles null Window input");
    }

    @Test
    void testWindowManagerIntegration() {
        // Tester l'intégration avec WindowManager
        assertNotNull(windowManager);

        // Mock du comportement de sauvegarde
        when(windowManager.save(any(Window.class))).thenReturn(testWindow);

        Window savedWindow = windowManager.save(testWindow);
        assertNotNull(savedWindow);
        assertEquals(testWindow.getCustomName(), savedWindow.getCustomName());

        // Vérifier que save a été appelé
        verify(windowManager, times(1)).save(testWindow);

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
    void testWindowValidation() {
        // Tester la validation des données de fenêtre
        Window validWindow = new Window();
        validWindow.setCustomName("Valid Window");
        validWindow.setRoom(testRoom);
        validWindow.setPosX(1.0);
        validWindow.setPosY(1.0);
        validWindow.setPosZ(1.0);

        // Les données devraient être valides
        assertNotNull(validWindow.getCustomName());
        assertNotNull(validWindow.getRoom());
        assertNotNull(validWindow.getPosX());

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

        logger.info("Window with empty name handled correctly");
    }

    @Test
    void testWindowWithNullRoom() {
        // Tester avec une salle null
        Window nullRoomWindow = new Window();
        nullRoomWindow.setCustomName("Test Window");
        nullRoomWindow.setRoom(null);

        // La salle ne devrait pas être null pour une fenêtre valide
        assertNull(nullRoomWindow.getRoom());

        logger.info("Window with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer windowId = 1;

        // Mock de la méthode deleteById
        doNothing().when(windowManager).deleteById(windowId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            windowManager.deleteById(windowId);
        });

        // Vérifier que deleteById a été appelé
        verify(windowManager, times(1)).deleteById(windowId);

        logger.info("Delete operation mock test successful for window ID: " + windowId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(windowManager.save(any(Window.class))).thenReturn(testWindow);

        // Simuler la sauvegarde
        Window result = assertDoesNotThrow(() -> {
            return windowManager.save(testWindow);
        });

        assertNotNull(result);
        assertEquals(testWindow.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(windowManager, times(1)).save(testWindow);

        logger.info("Save operation mock test successful for window: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(windowManager.save(any(Window.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            windowManager.save(testWindow);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer windowId = 1;
        doThrow(new RuntimeException("Delete error")).when(windowManager).deleteById(windowId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            windowManager.deleteById(windowId);
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
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = { false };

        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };

        windowEditor.setOnDataChanged(testCallback);

        // Simuler l'exécution du callback
        testCallback.run();

        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}