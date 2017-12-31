package com.mridene.PeopleDirectorywebflux;

import com.mridene.PeopleDirectorywebflux.model.Department;
import com.mridene.PeopleDirectorywebflux.repository.DepartmentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateDepartment() {
        Department department = new Department("This is a Test Department");

        webTestClient.post().uri("/departments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(department), Department.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("This is a Test Department");
    }

    @Test
    public void testGetAllDepartments() {
        webTestClient.get().uri("/departments")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Department.class);
    }

    @Test
    public void testGetSingleDepartment() {
        Department department = departmentRepository.save(new Department("Test department")).block();

        webTestClient.get()
                .uri("/departments/{id}", Collections.singletonMap("id", department.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateDepartment() {
        Department department = departmentRepository.save(new Department("Initial Department")).block();

        Department newDepartmentData = new Department("Updated Department");

        webTestClient.put()
                .uri("/departments/{id}", Collections.singletonMap("id", department.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newDepartmentData), Department.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Updated Department");
    }

    @Test
    public void testDeleteDepartment() {
        Department department = departmentRepository.save(new Department("To be deleted")).block();

        webTestClient.delete()
                .uri("/departments/{id}", Collections.singletonMap("id",  department.getId()))
                .exchange()
                .expectStatus().isOk();
    }

}
