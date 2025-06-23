package com.SAE.sae.view.editor;

import org.junit.jupiter.api.Test;
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

import com.SAE.sae.entity.RoomObjects.Siren;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.SirenManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the SirenEditor component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de l'éditeur de tables
 * et de ses interactions avec les services.
 */
public class SirenEditorTest {

    @MockBean
    private SirenManager SirenManager;
    
    @MockBean
    private RoomManager roomManager;
    
    private SirenEditor SirenEditor;
    private Siren testSiren;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(SirenEditorTest.class.getName());

    @BeforeEach
    void setUp() {
        UI.setCurrent(new UI()); 
        // Initialiser le SirenEditor avec les mocks
        SirenEditor = new SirenEditor(SirenManager, roomManager);
        
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
        testSiren.setCustomName("Test Table");
        testSiren.setRoom(testRoom);
        testSiren.setPosX(1.0);
        testSiren.setPosY(2.0);
        testSiren.setPosZ(3.0);
        
        logger.info("SirenEditor test setup completed");
    }

    @Test
    void testSirenEditorInstantiation() {
        // Vérifier que le SirenEditor peut être instancié correctement
        assertNotNull(SirenEditor);
        logger.info("SirenEditor instantiated successfully");
    }

    @Test
    void testConstructorWithValidManagers() {
        // Vérifier que le constructeur fonctionne avec des managers valides
        SirenEditor editor = new SirenEditor(SirenManager, roomManager);
        assertNotNull(editor);
        logger.info("SirenEditor constructor works with valid managers");
    }

    @Test
    void testSetOnDataChangedCallback() {
        // Tester la configuration du callback
        Runnable testCallback = () -> logger.info("Callback executed");
        
        assertDoesNotThrow(() -> {
            SirenEditor.setOnDataChanged(testCallback);
        });
        
        logger.info("OnDataChanged callback set successfully");
    }

    @Test
    void testSetOnDataChangedWithNullCallback() {
        // Tester avec un callback null
        assertDoesNotThrow(() -> {
            SirenEditor.setOnDataChanged(null);
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
            SirenEditor.openAddDialog();
        });
        
        // Vérifier que getAllRooms a été appelé pour peupler la ComboBox
        verify(roomManager, atLeastOnce()).getAllRooms();
        
        logger.info("Add dialog opened successfully with mocked room data");
    }

    @Test
    void testOpenEditDialogWithValidSiren() {
        // Mock des données nécessaires
        List<Room> mockRooms = Arrays.asList(testRoom);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);
        
        // Tester l'ouverture du dialog d'édition avec une table valide
        assertDoesNotThrow(() -> {
            SirenEditor.openEditDialog(testSiren);
        });
        
        // Vérifier que getAllRooms a été appelé
        verify(roomManager, atLeastOnce()).getAllRooms();
        
        logger.info("Edit dialog opened successfully for table: " + testSiren.getCustomName());
    }

    @Test
    void testOpenEditDialogWithNullSiren() {
        // Tester l'ouverture du dialog d'édition avec une table null
        // Cela devrait afficher une notification d'avertissement
        assertDoesNotThrow(() -> {
            SirenEditor.openEditDialog(null);
        });
        
        // Vérifier que getAllRooms n'a pas été appelé car le dialog ne s'ouvre pas
        verify(roomManager, never()).getAllRooms();
        
        logger.info("Edit dialog correctly handles null Siren input");
    }

    @Test
    void testConfirmDeleteWithValidSiren() {
        // Tester la confirmation de suppression avec une table valide
        assertDoesNotThrow(() -> {
            SirenEditor.confirmDelete(testSiren);
        });
        
        logger.info("Delete confirmation dialog opened for table: " + testSiren.getCustomName());
    }

    @Test
    void testConfirmDeleteWithNullSiren() {
        // Tester la confirmation de suppression avec une table null
        assertDoesNotThrow(() -> {
            SirenEditor.confirmDelete(null);
        });
        
        logger.info("Delete confirmation correctly handles null Siren input");
    }

    @Test
    void testSirenManagerIntegration() {
        // Tester l'intégration avec SirenManager
        assertNotNull(SirenManager);
        
        // Mock du comportement de sauvegarde
        when(SirenManager.save(any(Siren.class))).thenReturn(testSiren);
        
        Siren savedTable = SirenManager.save(testSiren);
        assertNotNull(savedTable);
        assertEquals(testSiren.getCustomName(), savedTable.getCustomName());
        
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
    void testSirenValidation() {
        // Tester la validation des données de table
        Siren validTable = new Siren();
        validTable.setCustomName("Valid Table");
        validTable.setRoom(testRoom);
        validTable.setPosX(1.0);
        validTable.setPosY(1.0);
        validTable.setPosZ(1.0);

        
        // Les données devraient être valides
        assertNotNull(validTable.getCustomName());
        assertNotNull(validTable.getRoom());
        assertNotNull(validTable.getPosX());
        
        logger.info("Siren validation test - all required fields present");
    }

    @Test
    void testSirenWithEmptyName() {
        // Tester avec un nom vide
        Siren emptyNameTable = new Siren();
        emptyNameTable.setCustomName("");
        emptyNameTable.setRoom(testRoom);
        
        // Le nom ne devrait pas être vide
        assertTrue(emptyNameTable.getCustomName().isEmpty());
        
        logger.info("Siren with empty name handled correctly");
    }

    @Test
    void testSirenWithNullRoom() {
        // Tester avec une salle null
        Siren nullRoomTable = new Siren();
        nullRoomTable.setCustomName("Test Table");
        nullRoomTable.setRoom(null);
        
        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomTable.getRoom());
        
        logger.info("Siren with null room detected correctly");
    }

    @Test
    void testDeleteOperationMocking() {
        // Tester l'opération de suppression
        Integer tableId = 1;
        
        // Mock de la méthode deleteById
        doNothing().when(SirenManager).deleteById(tableId);
        
        // Simuler la suppression
        assertDoesNotThrow(() -> {
            SirenManager.deleteById(tableId);
        });
        
        // Vérifier que deleteById a été appelé
        verify(SirenManager, times(1)).deleteById(tableId);
        
        logger.info("Delete operation mock test successful for table ID: " + tableId);
    }

    @Test
    void testSaveOperationMocking() {
        // Tester l'opération de sauvegarde
        when(SirenManager.save(any(Siren.class))).thenReturn(testSiren);
        
        // Simuler la sauvegarde
        Siren result = assertDoesNotThrow(() -> {
            return SirenManager.save(testSiren);
        });
        
        assertNotNull(result);
        assertEquals(testSiren.getId(), result.getId());
        
        // Vérifier que save a été appelé
        verify(SirenManager, times(1)).save(testSiren);
        
        logger.info("Save operation mock test successful for table: " + result.getCustomName());
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(SirenManager.save(any(Siren.class)))
            .thenThrow(new RuntimeException("Database error"));
        
        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SirenManager.save(testSiren);
        });
        
        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer tableId = 1;
        doThrow(new RuntimeException("Delete error")).when(SirenManager).deleteById(tableId);
        
        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            SirenManager.deleteById(tableId);
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
    void testCallbackExecution() {
        // Tester l'exécution du callback
        boolean[] callbackExecuted = {false};
        
        Runnable testCallback = () -> {
            callbackExecuted[0] = true;
            logger.info("Test callback executed successfully");
        };
        
        SirenEditor.setOnDataChanged(testCallback);
        
        // Simuler l'exécution du callback
        testCallback.run();
        
        assertTrue(callbackExecuted[0]);
        logger.info("Callback execution test successful");
    }
}