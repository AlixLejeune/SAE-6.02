package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.Room;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {
    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
 

    @Test
    void testCreateRoom() {
        Room create = new Room("createTest",0,0,0,1);

        Room res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/rooms", create, Room.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/" + res.getId(),
                Room.class)).satisfies(rooms -> {
                    assertThat(rooms).isNotNull();
                    assertThat(rooms.getName()).isEqualTo(create.getName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/rooms/" + res.getId());
    }

    @Test
    void testDeleteRoom() {
        Room create = new Room(6,"deleteTest",0,0,0,null,1);

        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/rooms", create, Room.class);
        restTemplate.delete("http://localhost:" + port + "/api/v1/rooms/6");

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/6",
                Room.class)).satisfies(rooms -> {
                    assertThat(rooms).isNull();
                });
    }

    @Test
    void testGetAllRooms() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms",
                Room[].class)).satisfies(rooms -> {
                    assertThat(rooms).isNotEmpty();
                    assertThat(rooms.length).isEqualTo(5);
                    assertThat(rooms[0].getName()).isEqualTo("Amphithéâtre A");
                    assertThat(rooms[1].getName()).isEqualTo("Salle B101");
                    assertThat(rooms[2].getName()).isEqualTo("Lab Info");
                    assertThat(rooms[3].getName()).isEqualTo("Bureau Dir");
                    assertThat(rooms[4].getName()).isEqualTo("Réunion R1");
                });
    }

    @Test
    void testGetRoomById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/1",
                Room.class)).satisfies(rooms -> {
                    assertThat(rooms).isNotNull();
                    assertThat(rooms.getName()).isEqualTo("Amphithéâtre A");
                });
    }

    @Test
    void testUpdateRoom() {
        Room update = new Room("test",6.0,8.0,3.0,5);
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/rooms", update, Room.class);
        Room get = restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/by-custom-name?name=test", Room[].class)[0];
        get.setName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/rooms", get);
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/"+ get.getId(),
                Room.class)).satisfies(rooms -> {
                    assertThat(rooms).isNotNull();
                    assertThat(rooms.getName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/rooms/" + get.getId());
    }
}
