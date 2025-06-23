package com.SAE.sae.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.SAE.sae.entity.RoomObjects.DataTable;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DataTableControllerTest {

    @LocalServerPort
	private final int port = 8080;

 	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    void testCreateDataTable() {
        DataTable create = new DataTable("createTest");

        DataTable res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/data-tables", create, DataTable.class).getBody();

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/" + res.getId(),
                DataTable.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo(create.getCustomName());
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/data-tables/" + res.getId());
    }

    @Test
    void testDeleteByCustomName() {
        DataTable del = new DataTable("delCustomNameTest");
        DataTable res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/data-tables", del, DataTable.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/data-tables/by-custom-name?customName=delCustomNameTest");

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/" + res.getId(), DataTable.class) == null;
    }

    @Test
    void testDeleteDataTable() {
        DataTable del = new DataTable("deleteTest");
        DataTable res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/data-tables", del, DataTable.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/api/v1/data-tables/" + res.getId());

        assert restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/" + res.getId(), DataTable.class) == null;
    }

    @Test
    void testGetAllDataTables() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables",
                DataTable[].class)).satisfies(d -> {
                    assertThat(d).isNotEmpty();
                    assertThat(d.length).isEqualTo(3);
                    assertThat(d[0].getCustomName()).isEqualTo("Poste étudiant 1");
                    assertThat(d[1].getCustomName()).isEqualTo("Poste étudiant 2");
                    assertThat(d[2].getCustomName()).isEqualTo("Poste réunion");
                });
    }

    @Test
    void testGetByCustomName() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/by-custom-name?name=Poste réunion", DataTable[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetByRoomId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/by-room/5", DataTable[].class)[0].getId()).isEqualTo(3);
    }

    @Test
    void testGetDataTableById() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/3", DataTable.class).getCustomName()).isEqualTo("Poste réunion");
    }

    @Test
    void testUpdateDataTable() {
        DataTable create = new DataTable("update");

        DataTable res = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/data-tables", create, DataTable.class).getBody();

        res.setCustomName("updateTest");
        restTemplate.put("http://localhost:" + port + "/api/v1/data-tables", res);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/data-tables/" + res.getId(),
                DataTable.class)).satisfies(d -> {
                    assertThat(d).isNotNull();
                    assertThat(d.getCustomName()).isEqualTo("updateTest");
                });
        restTemplate.delete("http://localhost:" + port + "/api/v1/data-tables/" + res.getId());
    }
}
