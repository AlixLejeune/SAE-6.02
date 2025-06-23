package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Lamp;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LampControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
    
    @Test
    void testCreateLamp() {
        Lamp create = new Lamp("createTest");

        Lamp res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/lamps", create, Lamp.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/" + res.getId(),
                Lamp.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/lamps/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Lamp del = new Lamp("delCustomNameTest");
        Lamp res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/lamps", del, Lamp.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/lamps/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/" + res.getId(), Lamp.class) == null;
    }

    @Test
    void testDeleteLamp() {
        Lamp del = new Lamp("deleteTest");
        Lamp res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/lamps", del, Lamp.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/lamps/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/" + res.getId(), Lamp.class) == null;
    }

    @Test
    void testGetAllLamps() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps",
                Lamp[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Éclairage scène");
                    assertThat(d[1].getCustomName()).isEqualTo("Éclairage tableau");
                    assertThat(d[2].getCustomName()).isEqualTo("Éclairage laboratoire");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/by-custom-name?name=Éclairage laboratoire", Lamp[].class)[0].getId()).isEqualTo(3);
    }
    
    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/by-room/3", Lamp[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetLampLampById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/3", Lamp.class).getCustomName()).isEqualTo("Éclairage laboratoire");
    }

    @Test
    void testUpdateLamp() {
        Lamp create = new Lamp("update");

        Lamp res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/lamps", create, Lamp.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/lamps", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/lamps/" + res.getId(),
                Lamp.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/lamps/" + res.getId());
    }
}
