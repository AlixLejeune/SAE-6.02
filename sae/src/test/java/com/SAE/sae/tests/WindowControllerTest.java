package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.Window;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WindowControllerTest {
    
    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateWindow() {
        Window create = new Window("createTest");

        Window res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/windows", create, Window.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/" + res.getId(),
                Window.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/windows/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        Window del = new Window("delCustomNameTest");
        Window res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/windows", del, Window.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/windows/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/" + res.getId(), Window.class) == null;
    }

    @Test
    void testDeleteWindow() {
        Window del = new Window("deleteTest");
        Window res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/windows", del, Window.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/windows/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/" + res.getId(), Window.class) == null;
    }

    @Test
    void testGetAllWindows() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows",
                Window[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(6);
                    assertThat(d[0].getCustomName()).isEqualTo("Fenêtre amphi principale");
                    assertThat(d[1].getCustomName()).isEqualTo("Fenêtre salle cours");
                    assertThat(d[2].getCustomName()).isEqualTo("Fenêtre labo 1");
                    assertThat(d[3].getCustomName()).isEqualTo("Fenêtre labo 2");
                    assertThat(d[4].getCustomName()).isEqualTo("Fenêtre bureau");
                    assertThat(d[5].getCustomName()).isEqualTo("Fenêtre réunion");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/by-custom-name?name=Fenêtre labo 1", Window[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/by-room/5", Window[].class)[0].getId()).isEqualTo(6);
    }

    @Test
    void testGetWindowById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/3", Window.class).getCustomName()).isEqualTo("Fenêtre labo 1");
    }

    @Test
    void testUpdateWindow() {
        Window create = new Window("update");

        Window res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/windows", create, Window.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/windows", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/windows/" + res.getId(),
                Window.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/windows/" + res.getId());
    }
}
