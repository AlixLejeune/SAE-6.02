package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Door;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DoorControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateDoor() {
        Door create = new Door("createTest");

        Door res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/doors", create, Door.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/" + res.getId(),
                Door.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/doors/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Door del = new Door("delCustomNameTest");
        Door res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/doors", del, Door.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/doors/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/" + res.getId(), Door.class) == null;
    }

    @Test
    void testDeleteDoor() {
        Door del = new Door("deleteTest");
        Door res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/doors", del, Door.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/doors/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/" + res.getId(), Door.class) == null;
    }

    @Test
    void testGetAllDoors() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors",
                Door[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(6);
                    assertThat(d[0].getCustomName()).isEqualTo("Porte amphithéâtre principale");
                    assertThat(d[1].getCustomName()).isEqualTo("Porte amphithéâtre secours");
                    assertThat(d[2].getCustomName()).isEqualTo("Porte salle cours");
                    assertThat(d[3].getCustomName()).isEqualTo("Porte laboratoire");
                    assertThat(d[4].getCustomName()).isEqualTo("Porte bureau");
                    assertThat(d[5].getCustomName()).isEqualTo("Porte réunion");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/by-custom-name?name=Porte salle cours", Door[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/by-room/5", Door[].class)[0].getId()).isEqualTo(6);
    }

    @Test
    void testGetDoorById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/3", Door.class).getCustomName()).isEqualTo("Porte salle cours");
    }

    @Test
    void testUpdateDoor() {
        Door create = new Door("update");

        Door res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/doors", create, Door.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/doors", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/doors/" + res.getId(),
                Door.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/doors/" + res.getId());
    }
}
