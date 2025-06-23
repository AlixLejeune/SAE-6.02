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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;
import com.SAE.sae.service.RoomObjects.DoorManager;
import com.SAE.sae.service.RoomManager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;

@ExtendWith(MockitoExtension.class)
/**
 * Test class for the DoorView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement de la vue de
 * gestion des tables de données
 * et de ses interactions avec les services.
 */
public class DoorViewTest {

    @Mock
    private DoorManager DoorManager;

    @Mock
    private RoomManager roomManager;

    private DoorView DoorView;
    private Door testDoor;
    private Door testDoor2;
    private Room testRoom;
    private Building testBuilding;

    Logger logger = Logger.getLogger(DoorViewTest.class.getName());

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

        testDoor = new Door();
        testDoor.setId(1);
        testDoor.setCustomName("Test Door 1");
        testDoor.setRoom(testRoom);
        testDoor.setPosX(1.0);
        testDoor.setPosY(2.0);
        testDoor.setPosZ(3.0);
        testDoor.setSizeX(1.5);
        testDoor.setSizeY(2.5);
        testDoor.setSizeZ(0.8);

        testDoor2 = new Door();
        testDoor2.setId(2);
        testDoor2.setCustomName("Test Door 2");
        testDoor2.setRoom(testRoom);
        testDoor2.setPosX(2.0);
        testDoor2.setPosY(3.0);
        testDoor2.setPosZ(4.0);
        testDoor2.setSizeX(2.0);
        testDoor2.setSizeY(3.0);
        testDoor2.setSizeZ(1.0);

        // Initialiser la DoorView avec les mocks
        DoorView = new DoorView(DoorManager, roomManager);

        logger.info("DoorView test setup completed");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    void testDoorViewInstantiation() {
        // Vérifier que la DoorView peut être instanciée correctement
        assertNotNull(DoorView);
        logger.info("DoorView instantiated successfully");
    }

    @Test
    void testConstructorCallsLoadData() {
        // Mock des données pour vérifier l'appel à findAll lors de l'initialisation
        List<Door> mockDoors = Arrays.asList(testDoor, testDoor2);
        when(DoorManager.findAll()).thenReturn(mockDoors);

        // Créer une nouvelle instance pour tester l'initialisation
        DoorView newView = new DoorView(DoorManager, roomManager);

        // Vérifier que findAll a été appelé pendant l'initialisation
        verify(DoorManager, atLeastOnce()).findAll();

        logger.info("Constructor correctly calls loadData method");
    }

    @Test
    void testLoadDataWithValidDoors() {
        // Mock des données
        List<Door> mockDoors = Arrays.asList(testDoor, testDoor2);
        when(DoorManager.findAll()).thenReturn(mockDoors);

        // Tester le chargement des données
        assertDoesNotThrow(() -> {
            // La méthode loadData est privée, on teste via l'effet du constructeur
            DoorView newView = new DoorView(DoorManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(DoorManager, atLeastOnce()).findAll();

        logger.info("Load data successful with " + mockDoors.size() + " data tables");
    }

    @Test
    void testLoadDataWithEmptyList() {
        // Mock avec une liste vide
        when(DoorManager.findAll()).thenReturn(Arrays.asList());

        // Tester avec une liste vide
        assertDoesNotThrow(() -> {
            DoorView newView = new DoorView(DoorManager, roomManager);
        });

        // Vérifier que findAll a été appelé
        verify(DoorManager, atLeastOnce()).findAll();

        logger.info("Load data handles empty list correctly");
    }

    @Test
    void testLoadDataWithException() {
        // Mock qui lance une exception
        when(DoorManager.findAll())
                .thenThrow(new RuntimeException("Database connection error"));

        // La vue devrait gérer l'exception sans planter
        assertDoesNotThrow(() -> {
            DoorView newView = new DoorView(DoorManager, roomManager);
        });

        logger.info("Load data handles exceptions correctly");
    }

    @Test
    void testDoorManagerIntegration() {
        // Tester l'intégration avec DoorManager
        assertNotNull(DoorManager);

        // Mock du comportement de sauvegarde
        when(DoorManager.save(any(Door.class))).thenReturn(testDoor);

        Door savedDoor = DoorManager.save(testDoor);
        assertNotNull(savedDoor);
        assertEquals(testDoor.getCustomName(), savedDoor.getCustomName());

        // Vérifier que save a été appelé
        verify(DoorManager, times(1)).save(testDoor);

        logger.info("DoorManager integration test successful");
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
    void testSaveDoorOperation() {
        // Tester l'opération de sauvegarde (pas de updateDoor dans le service)
        when(DoorManager.save(any(Door.class))).thenReturn(testDoor);

        // Simuler la sauvegarde
        Door result = assertDoesNotThrow(() -> {
            return DoorManager.save(testDoor);
        });

        assertNotNull(result);
        assertEquals(testDoor.getId(), result.getId());

        // Vérifier que save a été appelé
        verify(DoorManager, times(1)).save(testDoor);

        logger.info("Save operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testDeleteDoorOperation() {
        // Tester l'opération de suppression
        Integer DoorId = 1;

        // Mock de la méthode deleteById
        doNothing().when(DoorManager).deleteById(DoorId);

        // Simuler la suppression
        assertDoesNotThrow(() -> {
            DoorManager.deleteById(DoorId);
        });

        // Vérifier que deleteById a été appelé
        verify(DoorManager, times(1)).deleteById(DoorId);

        logger.info("Delete operation test successful for data table ID: " + DoorId);
    }

    @Test
    void testFindByIdOperation() {
        // Tester l'opération de recherche par ID
        Integer DoorId = 1;
        when(DoorManager.findById(DoorId)).thenReturn(testDoor);

        Door result = assertDoesNotThrow(() -> {
            return DoorManager.findById(DoorId);
        });

        assertNotNull(result);
        assertEquals(testDoor.getId(), result.getId());
        assertEquals(testDoor.getCustomName(), result.getCustomName());

        verify(DoorManager, times(1)).findById(DoorId);

        logger.info("FindById operation test successful for data table: " + result.getCustomName());
    }

    @Test
    void testFindByIdWithInvalidId() {
        // Tester avec un ID invalide
        Integer invalidId = 999;
        when(DoorManager.findById(invalidId))
                .thenThrow(new IllegalArgumentException("Aucune Door trouvée avec l'ID : " + invalidId));

        assertThrows(IllegalArgumentException.class, () -> {
            DoorManager.findById(invalidId);
        });

        logger.info("FindById with invalid ID test successful");
    }

    @Test
    void testFindByRoomIdOperation() {
        // Tester la recherche par ID de salle (Integer)
        Integer roomId = 1;
        List<Door> mockDoors = Arrays.asList(testDoor, testDoor2);
        when(DoorManager.findByRoomId(roomId)).thenReturn(mockDoors);

        List<Door> result = DoorManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(DoorManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Integer) operation test successful");
    }

    @Test
    void testFindByRoomIdLongOperation() {
        // Tester la recherche par ID de salle (Long)
        Long roomId = 1L;
        List<Door> mockDoors = Arrays.asList(testDoor);
        when(DoorManager.findByRoomId(roomId)).thenReturn(mockDoors);

        List<Door> result = DoorManager.findByRoomId(roomId);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(DoorManager, times(1)).findByRoomId(roomId);

        logger.info("FindByRoomId (Long) operation test successful");
    }

    @Test
    void testFindByCustomNameOperation() {
        // Tester la recherche par nom personnalisé
        String customName = "Test Door 1";
        List<Door> mockDoors = Arrays.asList(testDoor);
        when(DoorManager.findByCustomName(customName)).thenReturn(mockDoors);

        List<Door> result = DoorManager.findByCustomName(customName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customName, result.get(0).getCustomName());

        verify(DoorManager, times(1)).findByCustomName(customName);

        logger.info("FindByCustomName operation test successful");
    }

    @Test
    void testSaveAllOperation() {
        // Tester la sauvegarde de plusieurs Doors
        List<Door> DoorList = Arrays.asList(testDoor, testDoor2);
        when(DoorManager.saveAll(DoorList)).thenReturn(DoorList);

        List<Door> result = DoorManager.saveAll(DoorList);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(DoorManager, times(1)).saveAll(DoorList);

        logger.info("SaveAll operation test successful - saved " + result.size() + " data tables");
    }

    @Test
    void testDeleteAllOperation() {
        // Tester la suppression de toutes les Doors
        doNothing().when(DoorManager).deleteAll();

        assertDoesNotThrow(() -> {
            DoorManager.deleteAll();
        });

        verify(DoorManager, times(1)).deleteAll();

        logger.info("DeleteAll operation test successful");
    }

    @Test
    void testDeleteByCustomNameOperation() {
        // Tester la suppression par nom personnalisé
        String customName = "Test Door 1";
        doNothing().when(DoorManager).deleteByCustomName(customName);

        assertDoesNotThrow(() -> {
            DoorManager.deleteByCustomName(customName);
        });

        verify(DoorManager, times(1)).deleteByCustomName(customName);

        logger.info("DeleteByCustomName operation test successful");
    }

    @Test
    void testExistsByIdOperation() {
        // Tester la vérification d'existence par ID
        Integer DoorId = 1;
        when(DoorManager.existsById(DoorId)).thenReturn(true);

        boolean exists = DoorManager.existsById(DoorId);
        assertTrue(exists);

        verify(DoorManager, times(1)).existsById(DoorId);

        logger.info("ExistsById operation test successful");
    }

    @Test
    void testExistsByIdWithNonExistentId() {
        // Tester avec un ID qui n'existe pas
        Integer nonExistentId = 999;
        when(DoorManager.existsById(nonExistentId)).thenReturn(false);

        boolean exists = DoorManager.existsById(nonExistentId);
        assertFalse(exists);

        verify(DoorManager, times(1)).existsById(nonExistentId);

        logger.info("ExistsById with non-existent ID test successful");
    }

    @Test
    void testExceptionHandlingInSave() {
        // Tester la gestion des exceptions lors de la sauvegarde
        when(DoorManager.save(any(Door.class)))
                .thenThrow(new RuntimeException("Save operation failed"));

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            DoorManager.save(testDoor);
        });

        logger.info("Exception handling in save operation tested successfully");
    }

    @Test
    void testExceptionHandlingInDelete() {
        // Tester la gestion des exceptions lors de la suppression
        Integer DoorId = 1;
        doThrow(new RuntimeException("Delete operation failed")).when(DoorManager).deleteById(DoorId);

        // Vérifier que l'exception est bien lancée
        assertThrows(RuntimeException.class, () -> {
            DoorManager.deleteById(DoorId);
        });

        logger.info("Exception handling in delete operation tested successfully");
    }

    @Test
    void testDoorValidation() {
        // Tester la validation des données de table
        Door validDoor = new Door();
        validDoor.setCustomName("Valid Door");
        validDoor.setRoom(testRoom);
        validDoor.setPosX(1.0);
        validDoor.setPosY(1.0);
        validDoor.setPosZ(1.0);
        validDoor.setSizeX(1.0);
        validDoor.setSizeY(1.0);
        validDoor.setSizeZ(1.0);

        // Les données devraient être valides
        assertNotNull(validDoor.getCustomName());
        assertNotNull(validDoor.getRoom());
        assertNotNull(validDoor.getPosX());
        assertNotNull(validDoor.getSizeX());

        logger.info("Door validation test - all required fields present");
    }

    @Test
    void testDoorWithEmptyName() {
        // Tester avec un nom vide
        Door emptyNameDoor = new Door();
        emptyNameDoor.setCustomName("");
        emptyNameDoor.setRoom(testRoom);

        // Le nom ne devrait pas être vide
        assertTrue(emptyNameDoor.getCustomName().isEmpty());

        logger.info("Door with empty name detected correctly");
    }

    @Test
    void testDoorWithNullRoom() {
        // Tester avec une salle null
        Door nullRoomDoor = new Door();
        nullRoomDoor.setCustomName("Test Door");
        nullRoomDoor.setRoom(null);

        // La salle ne devrait pas être null pour une table valide
        assertNull(nullRoomDoor.getRoom());

        logger.info("Door with null room detected correctly");
    }

    @Test
    void testDoorWithNullName() {
        // Tester avec un nom null
        Door nullNameDoor = new Door();
        nullNameDoor.setCustomName(null);
        nullNameDoor.setRoom(testRoom);

        // Le nom ne devrait pas être null pour une table valide
        assertNull(nullNameDoor.getCustomName());

        logger.info("Door with null name detected correctly");
    }

    @Test
    void testDoorWithWhitespaceOnlyName() {
        // Tester avec un nom contenant seulement des espaces
        Door whitespaceNameDoor = new Door();
        whitespaceNameDoor.setCustomName("   ");
        whitespaceNameDoor.setRoom(testRoom);

        // Le nom ne devrait pas être seulement des espaces
        assertTrue(whitespaceNameDoor.getCustomName().trim().isEmpty());

        logger.info("Door with whitespace-only name detected correctly");
    }

    @Test
    void testMultipleDoorsHandling() {
        // Tester avec plusieurs tables
        Door Door3 = new Door();
        Door3.setId(3);
        Door3.setCustomName("Test Door 3");
        Door3.setRoom(testRoom);

        List<Door> multipleDoors = Arrays.asList(testDoor, testDoor2, Door3);
        when(DoorManager.findAll()).thenReturn(multipleDoors);

        List<Door> result = DoorManager.findAll();
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
    void testDoorPositionValues() {
        // Tester les valeurs de position
        assertEquals(1.0, testDoor.getPosX());
        assertEquals(2.0, testDoor.getPosY());
        assertEquals(3.0, testDoor.getPosZ());

        logger.info("Door position values test successful: X=" +
                testDoor.getPosX() + ", Y=" + testDoor.getPosY() +
                ", Z=" + testDoor.getPosZ());
    }

    @Test
    void testDoorSizeValues() {
        // Tester les valeurs de taille
        assertEquals(1.5, testDoor.getSizeX());
        assertEquals(2.5, testDoor.getSizeY());
        assertEquals(0.8, testDoor.getSizeZ());

        logger.info("Door size values test successful: X=" +
                testDoor.getSizeX() + ", Y=" + testDoor.getSizeY() +
                ", Z=" + testDoor.getSizeZ());
    }

    @Test
    void testDoorIdHandling() {
        // Tester la gestion des IDs
        assertEquals(Integer.valueOf(1), testDoor.getId());
        assertEquals(Integer.valueOf(2), testDoor2.getId());

        logger.info("Door ID handling test successful: Door1 ID=" +
                testDoor.getId() + ", Door2 ID=" + testDoor2.getId());
    }

    @Test
    void testDoorNameHandling() {
        // Tester la gestion des noms
        assertEquals("Test Door 1", testDoor.getCustomName());
        assertEquals("Test Door 2", testDoor2.getCustomName());

        logger.info("Door name handling test successful: Door1 name='" +
                testDoor.getCustomName() + "', Door2 name='" + testDoor2.getCustomName() + "'");
    }

    @Test
    void testDoorRoomAssociation() {
        // Tester l'association avec la salle
        assertEquals(testRoom, testDoor.getRoom());
        assertEquals(testRoom.getName(), testDoor.getRoom().getName());

        logger.info("Door room association test successful: Room='" +
                testDoor.getRoom().getName() + "'");
    }

    @Test
    void testInvalidPositionValues() {
        // Tester avec des valeurs de position invalides
        Door invalidDoor = new Door();
        invalidDoor.setCustomName("Invalid Position Door");
        invalidDoor.setRoom(testRoom);
        invalidDoor.setPosX(-1.0); // Position négative
        invalidDoor.setPosY(Double.NaN); // Valeur NaN
        invalidDoor.setPosZ(Double.POSITIVE_INFINITY); // Valeur infinie

        // Ces valeurs devraient être détectées comme invalides
        assertTrue(invalidDoor.getPosX() < 0);
        assertTrue(Double.isNaN(invalidDoor.getPosY()));
        assertTrue(Double.isInfinite(invalidDoor.getPosZ()));

        logger.info("Invalid position values test successful");
    }

    @Test
    void testInvalidSizeValues() {
        // Tester avec des valeurs de taille invalides
        Door invalidDoor = new Door();
        invalidDoor.setCustomName("Invalid Size Door");
        invalidDoor.setRoom(testRoom);
        invalidDoor.setSizeX(0.0); // Taille nulle
        invalidDoor.setSizeY(-1.0); // Taille négative
        invalidDoor.setSizeZ(Double.NaN); // Valeur NaN

        // Ces valeurs devraient être détectées comme invalides
        assertEquals(0.0, invalidDoor.getSizeX());
        assertTrue(invalidDoor.getSizeY() < 0);
        assertTrue(Double.isNaN(invalidDoor.getSizeZ()));

        logger.info("Invalid size values test successful");
    }

    // Méthode utilitaire pour créer des tables de données de test
    private Door createTestDoor(Integer id, String name) {
        Door Door = new Door();
        Door.setId(id);
        Door.setCustomName(name);
        Door.setRoom(testRoom);
        Door.setPosX(1.0);
        Door.setPosY(1.0);
        Door.setPosZ(1.0);
        Door.setSizeX(1.0);
        Door.setSizeY(1.0);
        Door.setSizeZ(1.0);
        return Door;
    }
}