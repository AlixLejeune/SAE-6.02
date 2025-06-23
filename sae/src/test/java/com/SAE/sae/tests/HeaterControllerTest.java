package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Heater;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HeaterControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateHeater() {
        Heater create = new Heater("createTest");

        Heater res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/heaters", create, Heater.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/" + res.getId(),
                Heater.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/heaters/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Heater del = new Heater("delCustomNameTest");
        Heater res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/heaters", del, Heater.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/heaters/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/" + res.getId(), Heater.class) == null;
    }


    @Test
    void testDeleteHeater() {
        Heater del = new Heater("deleteTest");
        Heater res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/heaters", del, Heater.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/heaters/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/" + res.getId(), Heater.class) == null;
    }

    @Test
    void testGetAllHeaters() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters",
                Heater[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(5);
                    assertThat(d[0].getCustomName()).isEqualTo("Radiateur amphi gauche");
                    assertThat(d[1].getCustomName()).isEqualTo("Radiateur amphi droite");
                    assertThat(d[2].getCustomName()).isEqualTo("Radiateur salle cours");
                    assertThat(d[3].getCustomName()).isEqualTo("Radiateur bureau");
                    assertThat(d[4].getCustomName()).isEqualTo("Radiateur réunion");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/by-custom-name?name=Radiateur salle cours", Heater[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/by-room/5", Heater[].class)[0].getId()).isEqualTo(5);
    }

    @Test
    void testGetHeaterById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/3", Heater.class).getCustomName()).isEqualTo("Radiateur salle cours");
    }

    @Test
    void testUpdateHeater() {
        Heater create = new Heater("update");

        Heater res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/heaters", create, Heater.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/heaters", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/heaters/" + res.getId(),
                Heater.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/heaters/" + res.getId());
    }
}
