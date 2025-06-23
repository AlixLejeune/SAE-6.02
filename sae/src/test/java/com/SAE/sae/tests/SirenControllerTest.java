package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Siren;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SirenControllerTest {
    
    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateSiren() {
        Siren create = new Siren("createTest");

        Siren res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sirens", create, Siren.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/" + res.getId(),
                Siren.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sirens/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Siren del = new Siren("delCustomNameTest");
        Siren res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sirens", del, Siren.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sirens/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/" + res.getId(), Siren.class) == null;
    }

    @Test
    void testDeleteSiren() {
        Siren del = new Siren("deleteTest");
        Siren res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sirens", del, Siren.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/sirens/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/" + res.getId(), Siren.class) == null;
    }

    @Test
    void testGetAllSirens() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens",
                Siren[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Sirène amphithéâtre");
                    assertThat(d[1].getCustomName()).isEqualTo("Sirène salle cours");
                    assertThat(d[2].getCustomName()).isEqualTo("Sirène laboratoire");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/by-custom-name?name=Sirène laboratoire", Siren[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/by-room/2", Siren[].class)[0].getId()).isEqualTo(2);
    }

    @Test
    void testGetSirenById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/3", Siren.class).getCustomName()).isEqualTo("Sirène laboratoire");
    }

    @Test
    void testUpdateSiren() {
        Siren create = new Siren("update");

        Siren res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sirens", create, Siren.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/sirens", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/sirens/" + res.getId(),
                Siren.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/sirens/" + res.getId());
    }
}
