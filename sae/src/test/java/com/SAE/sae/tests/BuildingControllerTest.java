package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.Building;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BuildingControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateBuilding() {
        Building create = new Building("createTest");

        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/buildings", create, Building.class);
        Building[] got = restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/by-custom-name?name=createTest",Building[].class);
        assert got[0] != null;
        restTemplate.delete("http://localhost:" + port + "/api/v1/buildings/" + got[0].getId());
    }

    @Test
    void testDeleteBuilding() {
        Building create = new Building("createTest");

        Building got = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/buildings", create, Building.class).getBody();
        restTemplate.delete("http://localhost:" + port + "/api/v1/buildings/" + got.getId());

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/" + got.getId(),
                Building.class)).satisfies(buildings -> {
                    assertThat(buildings).isNull();
                });
    }

    @Test
    void testDeleteByName() {
        Building create = new Building("deleteTest");

        Building got = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/buildings", create, Building.class).getBody();
        restTemplate.delete("http://localhost:" + port + "/api/v1/buildings/by-custom-name?name=deleteTest");

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/by-custom-name?name=deleteTest",
                Building[].class)).satisfies(buildings -> {
                    assertThat(buildings).isEmpty();
                });
    }

    @Test
    void testGetAllBuildings() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings",
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
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/1",
                Building.class)).satisfies(buildings -> {
                    assertThat(buildings).isNotNull();
                    assertThat(buildings.getName()).isEqualTo("IUT Annecy");
                });
    }

    @Test
    void testGetByName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/by-custom-name?name=Tetras",
                Building[].class)).satisfies(buildings -> {
                    assertThat(buildings[0]).isNotNull();
                    assertThat(buildings[0].getId() == 2);
                });
    }

    @Test
    void testUpdateBuilding() {
        Building update = new Building(3,"updateTest", null);
        restTemplate.put("http://localhost:" + port + "/api/v1/buildings", update);
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/buildings/3",
                Building.class)).satisfies(buildings -> {
                    assertThat(buildings).isNotNull();
                    assertThat(buildings.getName()).isEqualTo("updateTest");
                });
        update.setName("Campus Principal");
        restTemplate.put("http://localhost:" + port + "/api/v1/buildings", update);
    }
}
