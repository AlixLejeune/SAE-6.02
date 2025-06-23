package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Plug;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlugControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreatePlug() {
        Plug create = new Plug("createTest");

        Plug res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/plugs", create, Plug.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/" + res.getId(),
                Plug.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/plugs/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Plug del = new Plug("delCustomNameTest");
        Plug res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/plugs", del, Plug.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/plugs/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/" + res.getId(), Plug.class) == null;
    }

    @Test
    void testDeletePlug() {
        Plug del = new Plug("deleteTest");
        Plug res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/plugs", del, Plug.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/plugs/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/" + res.getId(), Plug.class) == null;
    }

    @Test
    void testGetAllPlugs() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs",
                Plug[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Prise projecteur");
                    assertThat(d[1].getCustomName()).isEqualTo("Prise ordinateur prof");
                    assertThat(d[2].getCustomName()).isEqualTo("Prise station 1");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/by-custom-name?name=Prise station 1", Plug[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/by-room/2", Plug[].class)[0].getId()).isEqualTo(2);
    }

    @Test
    void testGetPlugPlugById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/3", Plug.class).getCustomName()).isEqualTo("Prise station 1");
    }

    @Test
    void testUpdatePlug() {
        Plug create = new Plug("update");

        Plug res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/plugs", create, Plug.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/plugs", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/plugs/" + res.getId(),
                Plug.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/plugs/" + res.getId());
    }
}
