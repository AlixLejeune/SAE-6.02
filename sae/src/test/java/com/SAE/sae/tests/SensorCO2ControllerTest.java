package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.SensorCO2;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SensorCO2ControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
    
    @Test
    void testCreateSensorCO2() {
        SensorCO2 create = new SensorCO2("createTest");

        SensorCO2 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensorco2s", create, SensorCO2.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId(),
                SensorCO2.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        SensorCO2 del = new SensorCO2("delCustomNameTest");
        SensorCO2 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensorco2s", del, SensorCO2.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensorco2s/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId(), SensorCO2.class) == null;
    }

    @Test
    void testDeleteSensorCO2() {
        SensorCO2 del = new SensorCO2("delCustomNameTest");
        SensorCO2 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensorco2s", del, SensorCO2.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId(), SensorCO2.class) == null;
    }

    @Test
    void testGetAllSensorCO2s() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s",
                SensorCO2[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Capteur CO2 amphi");
                    assertThat(d[1].getCustomName()).isEqualTo("Capteur CO2 cours");
                    assertThat(d[2].getCustomName()).isEqualTo("Capteur CO2 lab");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/by-custom-name?name=Capteur CO2 lab", SensorCO2[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/by-room/2", SensorCO2[].class)[0].getId()).isEqualTo(2);
    }

    @Test
    void testGetSensorCO2ById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/3", SensorCO2.class).getCustomName()).isEqualTo("Capteur CO2 lab");
    }

    @Test
    void testUpdateSensorCO2() {
        SensorCO2 create = new SensorCO2("update");

        SensorCO2 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensorco2s", create, SensorCO2.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/sensorco2s", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId(),
                SensorCO2.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensorco2s/" + res.getId());
    }
}
