package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Sensor6in1;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Sensor6in1ControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
    
    @Test
    void testCreateSensor6in1() {
        Sensor6in1 create = new Sensor6in1("createTest");

        Sensor6in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor6in1s", create, Sensor6in1.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId(),
                Sensor6in1.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Sensor6in1 del = new Sensor6in1("delCustomNameTest");
        Sensor6in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor6in1s", del, Sensor6in1.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor6in1s/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId(), Sensor6in1.class) == null;
    }

    @Test
    void testDeleteSensor6in1() {
        Sensor6in1 del = new Sensor6in1("delCustomNameTest");
        Sensor6in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor6in1s", del, Sensor6in1.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId(), Sensor6in1.class) == null;
    }

    @Test
    void testGetAllSensor6in1s() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s",
                Sensor6in1[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Capteur 6en1 amphi");
                    assertThat(d[1].getCustomName()).isEqualTo("Capteur 6en1 cours");
                    assertThat(d[2].getCustomName()).isEqualTo("Capteur 6en1 bureau");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/by-custom-name?name=Capteur 6en1 bureau", Sensor6in1[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/by-room/2", Sensor6in1[].class)[0].getId()).isEqualTo(2);
    }

    @Test
    void testGetSensor6in1ById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/3", Sensor6in1.class).getCustomName()).isEqualTo("Capteur 6en1 bureau");
    }

    @Test
    void testUpdateSensor6in1() {
        Sensor6in1 create = new Sensor6in1("update");

        Sensor6in1 res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensor6in1s", create, Sensor6in1.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/sensor6in1s", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId(),
                Sensor6in1.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sensor6in1s/" + res.getId());
    }
}
