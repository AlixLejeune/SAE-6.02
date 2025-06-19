package com.SAE.sae.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import static org.assertj.core.api.Assertions.assertThat;

import com.SAE.sae.entity.Building;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BuildingControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @BeforeAll
    static void setUp() {
        // This method can be used to set up any required data before all tests run
        // For example, you can initialize the database with some Livre objects
    }

    @BeforeEach
    void init() {
        // This method can be used to reset the state before each test
        // For example, you can clear the database or set up a specific state
    }   

    @Test
    void testCreateBuilding() {

    }

    @Test
    void testDeleteBuilding() {

    }

    @Test
    void testDeleteByName() {

    }

    @Test
    void testGetAllBuildings() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/",
                Building[].class)).satisfies(buildings -> {
                    assertThat(buildings).isNotEmpty();
                    assertThat(buildings.length).isEqualTo(3);
                    assertThat(buildings[0].getName()).isEqualTo("IUT Annecy");
                    assertThat(buildings[1].getName()).isEqualTo("Tetras");
                    assertThat(buildings[2].getName()).isEqualTo("Campus Principal");
                });
    }

    @Test
    void testGetBuildingById() {

    }

    @Test
    void testGetByName() {

    }

    @Test
    void testUpdateBuilding() {

    }
}
