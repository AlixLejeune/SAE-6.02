package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Sensor9in1;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Sensor9in1ControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
    
    @Test
    void testCreateSensor9in1() {
        Sensor9in1 create = new Sensor9in1("createTest");

        Sensor9in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor9in1s", create, Sensor9in1.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId(),
                Sensor9in1.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Sensor9in1 del = new Sensor9in1("delCustomNameTest");
        Sensor9in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor9in1s", del, Sensor9in1.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor9in1s/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId(), Sensor9in1.class) == null;
    }

    @Test
    void testDeleteSensor9in1() {
        Sensor9in1 del = new Sensor9in1("delCustomNameTest");
        Sensor9in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor9in1s", del, Sensor9in1.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId(), Sensor9in1.class) == null;
    }

    @Test
    void testGetAllSensor9in1s() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s",
                Sensor9in1[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(2);
                    assertThat(d[0].getCustomName()).isEqualTo("Capteur 9en1 lab");
                    assertThat(d[1].getCustomName()).isEqualTo("Capteur 9en1 réunion");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/by-custom-name?name=Capteur 9en1 lab", Sensor9in1[].class)[0].getId()).isEqualTo(1);
    }

    @Test
    void testGetSensor9in1Sensor9in1ByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/by-room/3", Sensor9in1[].class)[0].getId()).isEqualTo(1);
    }

    @Test
    void testGetById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/1", Sensor9in1.class).getCustomName()).isEqualTo("Capteur 9en1 lab");
    }

    @Test
    void testUpdateSensor9in1() {
        Sensor9in1 create = new Sensor9in1("update");

        Sensor9in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor9in1s", create, Sensor9in1.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/sensor9in1s", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId(),
                Sensor9in1.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor9in1s/" + res.getId());
    }
}
