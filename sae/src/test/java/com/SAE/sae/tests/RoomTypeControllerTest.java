package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.RoomType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomTypeControllerTest {
    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
 

    @Test
    void testCreateRoomType() {
        RoomType create = new RoomType("createTest");

        RoomType res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/room_types", create, RoomType.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/room_types/" + res.getId(),
                RoomType.class)).satisfies(roomTypes -> {
                    assertThat(roomTypes).isNotNull();
                    assertThat(roomTypes.getName()).isEqualTo(create.getName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/room_types/" + res.getId());
    }

    @Test
    void testDeleteRoomType() {
        RoomType create = new RoomType(6,"deleteTest");

        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/room_types", create, RoomType.class);
        restTemplate.delete("http://localhost:" + port + "/api/v1/room_types/6");

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/room_types/6",
                RoomType.class)).satisfies(roomtypes -> {
                    assertThat(roomtypes).isNull();
                });
    }

    @Test
    void testGetAllRoomTypes() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/room_types",
                RoomType[].class)).satisfies(roomtypes -> {
                    assertThat(roomtypes).isNotEmpty();
                    assertThat(roomtypes.length).isEqualTo(5);
                    assertThat(roomtypes[0].getName()).isEqualTo("Amphithéâtre");
                    assertThat(roomtypes[1].getName()).isEqualTo("Salle de cours");
                    assertThat(roomtypes[2].getName()).isEqualTo("Laboratoire");
                    assertThat(roomtypes[3].getName()).isEqualTo("Bureau");
                    assertThat(roomtypes[4].getName()).isEqualTo("Salle de réunion");
                });
    }

    @Test
    void testGetRoomTypeById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/room_types/1",
                RoomType.class)).satisfies(roomtypes -> {
                    assertThat(roomtypes).isNotNull();
                    assertThat(roomtypes.getName()).isEqualTo("Amphithéâtre");
                });
    }

    @Test
    void testUpdateRoomType() {
        RoomType update = new RoomType(5,"updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/room_types", update);
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/room_types/5",
                RoomType.class)).satisfies(roomtypes -> {
                    assertThat(roomtypes).isNotNull();
                    assertThat(roomtypes.getName()).isEqualTo("updateTest");
                });
        update.setName("Salle de réunion");
        restTemplate.put("http://localhost:" + port + "/api/v1/room_types", update);
    }
}
