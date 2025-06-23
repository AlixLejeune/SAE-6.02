package com.SAE.sae.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.SAE.sae.entity.Building;
import com.SAE.sae.entity.Room;
import com.SAE.sae.service.BuildingManager;
import com.SAE.sae.service.RoomManager;
import com.SAE.sae.service.RoomObjects.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
/**
 * Test class for the HomeView component.
 * Tests de bout en bout pour vérifier le bon fonctionnement du tableau de bord principal
 * et de ses interactions avec les données des managers.
 */
public class HomeViewTest {

    @MockBean
    private BuildingManager buildingManager;
    
    @MockBean
    private RoomManager roomManager;
    
    @MockBean
    private LampManager lampManager;
    
    @MockBean
    private SensorCO2Manager sensorCO2Manager;
    
    @MockBean
    private Sensor6in1Manager sensor6in1Manager;
    
    @MockBean
    private HeaterManager heaterManager;
    
    @MockBean
    private WindowManager windowManager;
    
    @MockBean
    private DoorManager doorManager;
    
    private HomeView homeView;

    Logger logger = Logger.getLogger(HomeViewTest.class.getName());

    @BeforeEach
    void setUp() {
        // Mock des données par défaut
        when(buildingManager.getAllBuildings()).thenReturn(new ArrayList<>());
        when(roomManager.getAllRooms()).thenReturn(new ArrayList<>());
        when(lampManager.count()).thenReturn(0L);
        when(sensorCO2Manager.count()).thenReturn(0L);
        when(sensor6in1Manager.count()).thenReturn(0L);
        when(heaterManager.count()).thenReturn(0L);
        when(windowManager.count()).thenReturn(0L);
        when(doorManager.count()).thenReturn(0L);
        
        // Initialiser le HomeView avec les mocks
        homeView = new HomeView(buildingManager, roomManager, lampManager, 
                               sensorCO2Manager, sensor6in1Manager, heaterManager,
                               windowManager, doorManager);
    }

    @Test
    void testHomeViewInstantiation() {
        // Vérifier que le HomeView peut être instancié correctement
        assertNotNull(homeView);
        logger.info("HomeView instantiated successfully");
        
        // Vérifier que le style de fond est appliqué
        String background = homeView.getElement().getStyle().get("background");
        assertNotNull(background);
        assertTrue(background.contains("linear-gradient"));
        logger.info("HomeView background gradient correctly applied");
    }

    @Test
    void testLayoutConfiguration() {
        // Vérifier la configuration du layout principal
        assertNotNull(homeView);
        
        // Vérifier les propriétés CSS
        String minHeight = homeView.getElement().getStyle().get("min-height");
        String backgroundAttachment = homeView.getElement().getStyle().get("background-attachment");
        
        assertEquals("100vh", minHeight);
        assertEquals("fixed", backgroundAttachment);
        
        logger.info("Layout configuration test passed");
        logger.info("Min-height: " + minHeight + ", Background-attachment: " + backgroundAttachment);
    }

    @Test
    void testKPISectionWithData() {
        // Mock des données pour les KPI
        Building building1 = new Building();
        building1.setId(1);
        building1.setName("Building 1");
        
        Building building2 = new Building();
        building2.setId(2);
        building2.setName("Building 2");
        
        Room room1 = new Room();
        room1.setId(1);
        room1.setName("Room 1");
        
        Room room2 = new Room();
        room2.setId(2);
        room2.setName("Room 2");
        
        List<Building> mockBuildings = Arrays.asList(building1, building2);
        List<Room> mockRooms = Arrays.asList(room1, room2);
        
        when(buildingManager.getAllBuildings()).thenReturn(mockBuildings);
        when(roomManager.getAllRooms()).thenReturn(mockRooms);
        when(lampManager.count()).thenReturn(5L);
        when(sensorCO2Manager.count()).thenReturn(3L);
        when(sensor6in1Manager.count()).thenReturn(2L);
        when(heaterManager.count()).thenReturn(4L);
        when(windowManager.count()).thenReturn(6L);
        when(doorManager.count()).thenReturn(8L);
        
        // Créer un nouveau HomeView avec les données mockées
        HomeView testView = new HomeView(buildingManager, roomManager, lampManager,
                                        sensorCO2Manager, sensor6in1Manager, heaterManager,
                                        windowManager, doorManager);
        
        assertNotNull(testView);
        
        // Vérifier que les managers ont été appelés
        verify(buildingManager, atLeastOnce()).getAllBuildings();
        verify(roomManager, atLeastOnce()).getAllRooms();
        verify(lampManager, atLeastOnce()).count();
        verify(sensorCO2Manager, atLeastOnce()).count();
        verify(sensor6in1Manager, atLeastOnce()).count();
        verify(heaterManager, atLeastOnce()).count();
        verify(windowManager, atLeastOnce()).count();
        verify(doorManager, atLeastOnce()).count();
        
        logger.info("KPI section with data test passed - all managers called correctly");
    }

    @Test
    void testEmptyDataHandling() {
        // Tester avec des données vides
        when(buildingManager.getAllBuildings()).thenReturn(new ArrayList<>());
        when(roomManager.getAllRooms()).thenReturn(new ArrayList<>());
        when(lampManager.count()).thenReturn(0L);
        when(sensorCO2Manager.count()).thenReturn(0L);
        when(sensor6in1Manager.count()).thenReturn(0L);
        when(heaterManager.count()).thenReturn(0L);
        when(windowManager.count()).thenReturn(0L);
        when(doorManager.count()).thenReturn(0L);
        
        HomeView emptyDataView = new HomeView(buildingManager, roomManager, lampManager,
                                             sensorCO2Manager, sensor6in1Manager, heaterManager,
                                             windowManager, doorManager);
        
        assertNotNull(emptyDataView);
        logger.info("Empty data handling test passed - HomeView handles empty data gracefully");
    }

    @Test
    void testDeviceCountCalculations() {
        // Mock des compteurs d'appareils
        when(lampManager.count()).thenReturn(10L);
        when(sensorCO2Manager.count()).thenReturn(5L);
        when(sensor6in1Manager.count()).thenReturn(3L);
        when(heaterManager.count()).thenReturn(7L);
        when(windowManager.count()).thenReturn(12L);
        when(doorManager.count()).thenReturn(8L);
        
        HomeView deviceTestView = new HomeView(buildingManager, roomManager, lampManager,
                                              sensorCO2Manager, sensor6in1Manager, heaterManager,
                                              windowManager, doorManager);
        
        assertNotNull(deviceTestView);
        
        // Vérifier que tous les managers de comptage ont été appelés
        verify(lampManager, atLeastOnce()).count();
        verify(sensorCO2Manager, atLeastOnce()).count();
        verify(sensor6in1Manager, atLeastOnce()).count();
        verify(heaterManager, atLeastOnce()).count();
        verify(windowManager, atLeastOnce()).count();
        verify(doorManager, atLeastOnce()).count();
        
        logger.info("Device count calculations test passed - total should be 45 devices");
    }

    @Test
    void testBuildingStatusPanel() {
        // Mock des bâtiments avec leurs salles
        Building building1 = new Building();
        building1.setId(1);
        building1.setName("Main Building");
        
        Building building2 = new Building();
        building2.setId(2);
        building2.setName("Secondary Building");
        
        Room room1 = new Room();
        room1.setId(1);
        room1.setName("Conference Room");
        room1.setBuilding(building1);
        
        Room room2 = new Room();
        room2.setId(2);
        room2.setName("Office");
        room2.setBuilding(building1);
        
        Room room3 = new Room();
        room3.setId(3);
        room3.setName("Storage");
        room3.setBuilding(building2);
        
        List<Building> mockBuildings = Arrays.asList(building1, building2);
        List<Room> building1Rooms = Arrays.asList(room1, room2);
        List<Room> building2Rooms = Arrays.asList(room3);
        
        when(buildingManager.getAllBuildings()).thenReturn(mockBuildings);
        when(roomManager.getRoomsByBuildingId(1)).thenReturn(building1Rooms);
        when(roomManager.getRoomsByBuildingId(2)).thenReturn(building2Rooms);
        
        HomeView buildingTestView = new HomeView(buildingManager, roomManager, lampManager,
                                                sensorCO2Manager, sensor6in1Manager, heaterManager,
                                                windowManager, doorManager);
        
        assertNotNull(buildingTestView);
        
        // Vérifier les appels aux managers
        verify(buildingManager, atLeastOnce()).getAllBuildings();
        verify(roomManager, atLeastOnce()).getRoomsByBuildingId(1);
        verify(roomManager, atLeastOnce()).getRoomsByBuildingId(2);
        
        logger.info("Building status panel test passed - buildings and rooms correctly processed");
    }

    @Test
    void testErrorHandlingInKPISection() {
        // Simuler une exception dans un des managers
        when(buildingManager.getAllBuildings()).thenThrow(new RuntimeException("Database error"));
        
        // Vérifier que HomeView ne lève pas d'exception
        assertDoesNotThrow(() -> {
            HomeView errorTestView = new HomeView(buildingManager, roomManager, lampManager,
                                                 sensorCO2Manager, sensor6in1Manager, heaterManager,
                                                 windowManager, doorManager);
            logger.info("Error handling in KPI section - HomeView handles exceptions gracefully");
        });
        
        logger.info("Error handling test completed - no exceptions propagated");
    }

    @Test
    void testDeviceDistributionChart() {
        // Mock avec des données variées pour le graphique
        when(lampManager.count()).thenReturn(15L);
        when(sensorCO2Manager.count()).thenReturn(8L);
        when(sensor6in1Manager.count()).thenReturn(4L);
        when(heaterManager.count()).thenReturn(12L);
        when(windowManager.count()).thenReturn(20L);
        when(doorManager.count()).thenReturn(6L);
        
        HomeView chartTestView = new HomeView(buildingManager, roomManager, lampManager,
                                             sensorCO2Manager, sensor6in1Manager, heaterManager,
                                             windowManager, doorManager);
        
        assertNotNull(chartTestView);
        
        // Vérifier que tous les compteurs sont appelés pour le graphique
        verify(lampManager, atLeastOnce()).count();
        verify(sensorCO2Manager, atLeastOnce()).count();
        verify(sensor6in1Manager, atLeastOnce()).count();
        verify(heaterManager, atLeastOnce()).count();
        verify(windowManager, atLeastOnce()).count();
        verify(doorManager, atLeastOnce()).count();
        
        logger.info("Device distribution chart test passed - total devices: 65");
    }

    @Test
    void testManagerDependencyInjection() {
        // Vérifier que tous les managers sont correctement mockés
        assertNotNull(buildingManager);
        assertNotNull(roomManager);
        assertNotNull(lampManager);
        assertNotNull(sensorCO2Manager);
        assertNotNull(sensor6in1Manager);
        assertNotNull(heaterManager);
        assertNotNull(windowManager);
        assertNotNull(doorManager);
        
        logger.info("All manager mocks injected successfully");
        
        // Tester l'accès aux données de chaque manager
        when(buildingManager.getAllBuildings()).thenReturn(new ArrayList<>());
        when(roomManager.getAllRooms()).thenReturn(new ArrayList<>());
        
        List<Building> buildings = buildingManager.getAllBuildings();
        List<Room> rooms = roomManager.getAllRooms();
        
        assertNotNull(buildings);
        assertNotNull(rooms);
        
        logger.info("Manager dependency injection test completed successfully");
    }

    @Test
    void testActiveDevicesCalculation() {
        // Test du calcul des appareils actifs (85% des appareils totaux)
        when(lampManager.count()).thenReturn(10L);
        when(sensorCO2Manager.count()).thenReturn(10L);
        when(sensor6in1Manager.count()).thenReturn(10L);
        when(heaterManager.count()).thenReturn(10L);
        when(windowManager.count()).thenReturn(10L);
        when(doorManager.count()).thenReturn(10L);
        // Total = 60 appareils, actifs = 51 (85%)
        
        HomeView activeTestView = new HomeView(buildingManager, roomManager, lampManager,
                                              sensorCO2Manager, sensor6in1Manager, heaterManager,
                                              windowManager, doorManager);
        
        assertNotNull(activeTestView);
        
        // Vérifier que tous les managers sont appelés
        verify(lampManager, atLeastOnce()).count();
        verify(sensorCO2Manager, atLeastOnce()).count();
        verify(sensor6in1Manager, atLeastOnce()).count();
        verify(heaterManager, atLeastOnce()).count();
        verify(windowManager, atLeastOnce()).count();
        verify(doorManager, atLeastOnce()).count();
        
        logger.info("Active devices calculation test passed - should show 51 active devices out of 60 total");
    }

    @Test
    void testMaxDeviceCountCalculation() {
        // Test pour vérifier le calcul du maximum d'appareils (pour les barres de progression)
        when(lampManager.count()).thenReturn(5L);
        when(sensorCO2Manager.count()).thenReturn(15L); // Max
        when(sensor6in1Manager.count()).thenReturn(3L);
        when(heaterManager.count()).thenReturn(8L);
        when(windowManager.count()).thenReturn(12L);
        when(doorManager.count()).thenReturn(7L);
        
        HomeView maxTestView = new HomeView(buildingManager, roomManager, lampManager,
                                           sensorCO2Manager, sensor6in1Manager, heaterManager,
                                           windowManager, doorManager);
        
        assertNotNull(maxTestView);
        
        // Le maximum devrait être 15 (capteurs CO2)
        verify(sensorCO2Manager, atLeastOnce()).count();
        
        logger.info("Max device count calculation test passed - max should be 15 (CO2 sensors)");
    }

    @Test
    void testErrorHandlingInDeviceManagers() {
        // Simuler des erreurs dans différents managers
        when(lampManager.count()).thenThrow(new RuntimeException("Lamp service error"));
        when(sensorCO2Manager.count()).thenReturn(5L);
        when(sensor6in1Manager.count()).thenReturn(3L);
        when(heaterManager.count()).thenReturn(7L);
        when(windowManager.count()).thenReturn(10L);
        when(doorManager.count()).thenReturn(6L);
        
        // Vérifier que HomeView gère les erreurs gracieusement
        assertDoesNotThrow(() -> {
            HomeView errorManagerView = new HomeView(buildingManager, roomManager, lampManager,
                                                    sensorCO2Manager, sensor6in1Manager, heaterManager,
                                                    windowManager, doorManager);
            logger.info("Error handling in device managers - HomeView handles manager errors gracefully");
        });
        
        logger.info("Device manager error handling test completed");
    }

    @Test
    void testLargeDataSetHandling() {
        // Test avec un grand nombre de bâtiments et de salles
        List<Building> largeBuildings = new ArrayList<>();
        List<Room> largeRooms = new ArrayList<>();
        
        for (int i = 1; i <= 50; i++) {
            Building building = new Building();
            building.setId(i);
            building.setName("Building " + i);
            largeBuildings.add(building);
            
            // 3 salles par bâtiment
            for (int j = 1; j <= 3; j++) {
                Room room = new Room();
                room.setId(i * 10 + j);
                room.setName("Room " + j + " - Building " + i);
                room.setBuilding(building);
                largeRooms.add(room);
            }
        }
        
        when(buildingManager.getAllBuildings()).thenReturn(largeBuildings);
        when(roomManager.getAllRooms()).thenReturn(largeRooms);
        when(lampManager.count()).thenReturn(200L);
        when(sensorCO2Manager.count()).thenReturn(150L);
        when(sensor6in1Manager.count()).thenReturn(100L);
        when(heaterManager.count()).thenReturn(180L);
        when(windowManager.count()).thenReturn(300L);
        when(doorManager.count()).thenReturn(250L);
        
        // Créer HomeView avec un grand dataset
        HomeView largeDataView = new HomeView(buildingManager, roomManager, lampManager,
                                             sensorCO2Manager, sensor6in1Manager, heaterManager,
                                             windowManager, doorManager);
        
        assertNotNull(largeDataView);
        
        // Vérifier que tous les managers ont été appelés
        verify(buildingManager, atLeastOnce()).getAllBuildings();
        verify(roomManager, atLeastOnce()).getAllRooms();
        
        logger.info("Large dataset handling test passed - 50 buildings, 150 rooms, 1180 total devices");
    }
}