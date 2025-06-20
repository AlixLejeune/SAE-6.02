package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.Building;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {
    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;
 

    @Test
    void testCreateRoom() {
        Room create = new Room(6,"createTest",0,0,0,new Building(),1);

        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/rooms", create, Room.class);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/6",
                Room.class)).satisfies(rooms -> {
                    //assertThat(rooms).isNotNull();
                    assertThat(rooms).isEqualTo(create);
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/rooms/6");
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
        Room update = new Room(5,"updateTest",6,8,3,null,5);
        restTemplate.put("http://localhost:" + port + "/api/v1/rooms", update);
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/rooms/5",
                Room.class)).satisfies(rooms -> {
                    assertThat(rooms).isNotNull();
                    assertThat(rooms.getName()).isEqualTo("updateTest");
                });
        update.setName("Réunion R1");
        restTemplate.put("http://localhost:" + port + "/api/v1/rooms", update);
    }
}
