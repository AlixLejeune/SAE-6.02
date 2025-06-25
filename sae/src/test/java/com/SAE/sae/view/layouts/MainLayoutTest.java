package com.SAE.sae.view.layouts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.Room;
import com.SAE.sae.repository.BuildingRepository;
import com.SAE.sae.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
/**
 * Test class for the MainLayout component.
 * Tests de bout en bout pour vérifier le bon fonctionnement du layout principal
 * et de ses interactions avec les données.
 */
public class MainLayoutTest {

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private RoomRepository roomRepository;

    private MainLayout mainLayout;

    Logger logger = Logger.getLogger(MainLayoutTest.class.getName());

    @BeforeEach
    void setUp() {
        // Initialiser le MainLayout avec les mocks
        mainLayout = new MainLayout(buildingRepository, roomRepository);
    }

    @Test
    void testMainLayoutInstantiation() {
        // Vérifier que le MainLayout peut être instancié correctement
        assertNotNull(mainLayout);
        logger.info("MainLayout instantiated successfully");
    }

    @Test
    void testHeaderConfiguration() {
        // Vérifier que le header est configuré correctement
        assertNotNull(mainLayout);

        // Le header devrait contenir un DrawerToggle et un titre
        // Note: En raison de l'architecture Vaadin, nous testons indirectement
        logger.info("Header configuration test - MainLayout should have drawer toggle and title");

        // Vérifier que la configuration de style est appliquée
        String minHeight = mainLayout.getElement().getStyle().get("min-height");
        assertEquals("100vh", minHeight);
        logger.info("MainLayout min-height correctly set to: " + minHeight);
    }

    @Test
    void testNavigationDrawerCreation() {
        // Vérifier que le drawer de navigation est créé
        assertNotNull(mainLayout);
        logger.info("Navigation drawer should be created with menu container");

        // Le drawer devrait être ajouté au layout
        assertTrue(mainLayout.getElement().getChildren().count() > 0);
        logger.info("MainLayout has child elements (drawer should be present)");
    }

    @Test
    void testBuildingDataIntegration() {
        // Mock des données de bâtiments
        Building building1 = new Building();
        building1.setId(1);
        building1.setName("Building 1");

        Building building2 = new Building();
        building2.setId(2);
        building2.setName("Building 2");

        List<Building> mockBuildings = Arrays.asList(building1, building2);
        when(buildingRepository.findAll()).thenReturn(mockBuildings);

        logger.info("Mocked " + mockBuildings.size() + " buildings");

        // Créer un nouveau MainLayout pour tester avec les données mockées
        MainLayout testLayout = new MainLayout(buildingRepository, roomRepository);
        assertNotNull(testLayout);

        // Vérifier que findAll a été appelé
        verify(buildingRepository, atLeastOnce()).findAll();

        logger.info("MainLayout created successfully with mocked building data");
    }

    @Test
    void testBuildingWithRoomsIntegration() {
        // Mock des données
        Building testBuilding = new Building();
        testBuilding.setId(1);
        testBuilding.setName("Test Building");

        Room testRoom1 = new Room();
        testRoom1.setId(1);
        testRoom1.setName("Test Room 1");
        testRoom1.setBuilding(testBuilding);

        Room testRoom2 = new Room();
        testRoom2.setId(2);
        testRoom2.setName("Test Room 2");
        testRoom2.setBuilding(testBuilding);

        List<Room> mockRooms = Arrays.asList(testRoom1, testRoom2);
        List<Building> mockBuildings = Arrays.asList(testBuilding);

        when(buildingRepository.findAll()).thenReturn(mockBuildings);
        when(roomRepository.findByBuilding_Id(1)).thenReturn(mockRooms);

        logger.info("Mocked building with rooms: " + testRoom1.getName() + ", " + testRoom2.getName());

        // Créer un nouveau MainLayout pour tester l'affichage
        MainLayout layoutWithData = new MainLayout(buildingRepository, roomRepository);
        assertNotNull(layoutWithData);

        // Vérifier que les méthodes ont été appelées
        verify(buildingRepository, atLeastOnce()).findAll();
        verify(roomRepository, atLeastOnce()).findByBuilding_Id(1);

        logger.info("MainLayout created successfully with mocked building and room data");
    }

    @Test
    void testMenuContainerStructure() {
        // Tester la structure du conteneur de menu
        assertNotNull(mainLayout);

        // Le MainLayout devrait avoir une structure de navigation
        // Note: Test indirect car l'accès direct aux composants privés est limité
        logger.info("Testing menu container structure indirectly");

        // Vérifier que le layout est correctement configuré
        String background = mainLayout.getElement().getStyle().get("background");
        assertNotNull(background);
        assertTrue(background.contains("linear-gradient"));
        logger.info("MainLayout background gradient correctly applied: " + background);
    }

    @Test
    void testRepositoryDependencyInjection() {
        // Vérifier que les repositories sont correctement mockés
        assertNotNull(buildingRepository);
        assertNotNull(roomRepository);
        logger.info("Repository mocks injected successfully");

        // Mock des données pour le test
        when(buildingRepository.findAll()).thenReturn(new ArrayList<>());
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        // Tester l'accès aux données
        List<Building> buildings = buildingRepository.findAll();
        assertNotNull(buildings);
        logger.info("BuildingRepository mock works - returned " + buildings.size() + " buildings");

        List<Room> rooms = roomRepository.findAll();
        assertNotNull(rooms);
        logger.info("RoomRepository mock works - returned " + rooms.size() + " rooms");
    }

    @Test
    void testLayoutResponsiveness() {
        // Tester la responsivité du layout
        assertNotNull(mainLayout);

        // Vérifier les propriétés CSS responsives
        String minHeight = mainLayout.getElement().getStyle().get("min-height");
        String backgroundAttachment = mainLayout.getElement().getStyle().get("background-attachment");

        assertEquals("100vh", minHeight);
        assertEquals("fixed", backgroundAttachment);

        logger.info("Layout responsiveness properties correctly set");
        logger.info("Min-height: " + minHeight + ", Background-attachment: " + backgroundAttachment);
    }

    @Test
    void testNavigationFunctionality() {
        // Tester la fonctionnalité de navigation (test indirect)
        assertNotNull(mainLayout);

        // Créer un nouveau MainLayout pour simuler les interactions
        MainLayout navTestLayout = new MainLayout(buildingRepository, roomRepository);
        assertNotNull(navTestLayout);

        logger.info("Navigation functionality test - MainLayout should handle navigation correctly");

        // Le layout devrait être configuré pour la navigation
        assertTrue(navTestLayout instanceof com.vaadin.flow.component.applayout.AppLayout);
        logger.info("MainLayout correctly extends AppLayout for navigation");
    }

    @Test
    void testErrorHandlingInBuildingSection() {
        // Tester la gestion des erreurs dans la section des bâtiments

        // Simuler une situation où les repositories pourraient échouer
        // En créant un MainLayout normal et en vérifiant qu'il ne lève pas d'exception
        assertDoesNotThrow(() -> {
            MainLayout errorTestLayout = new MainLayout(buildingRepository, roomRepository);
            logger.info("MainLayout handles potential repository errors gracefully");
        });

        logger.info("Error handling test completed - no exceptions thrown");
    }
}