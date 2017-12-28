package com.mridene.PeopleDirectorywebflux;

import com.mridene.PeopleDirectorywebflux.model.Company;
import com.mridene.PeopleDirectorywebflux.repository.CompanyRepository;
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
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PeopleDirectoryWebfluxApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	CompanyRepository companyRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreateCompany() {
		Company company = new Company("This is a Test Company", new Date(), "www.testcompany.com", "test location");

		webTestClient.post().uri("/companies")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(company), Company.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.name").isEqualTo("This is a Test Company")
				.jsonPath("$.createdAt").isNotEmpty()
				.jsonPath("$.website").isEqualTo("www.testcompany.com")
				.jsonPath("$.headquarters").isEqualTo("test location");
	}

	@Test
	public void testGetAllCompanies() {
		webTestClient.get().uri("/companies")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(Company.class);
	}

	@Test
	public void testGetSingleCompany() {
		Company company = companyRepository.save(new Company("Test company")).block();

		webTestClient.get()
				.uri("/companies/{id}", Collections.singletonMap("id", company.getId()))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void testUpdateCompany() {
		/**TODO
		 * Test on createdAt value
		 * **/
		Date initialDate = new Date(1999,	01,	01);
		String initialWebsite = "www.initialwebsite.com";
		String initialHeadquarters = "initial headquarterts";

		Date updatedDate = new Date(2000,	10,	10);
		String updatedWebsite = "www.updatedwebsite.com";
		String updatedHeadquarters = "updated headquarterts";

		Company company = companyRepository.save(new Company("Initial Company",
				initialDate, initialWebsite, initialHeadquarters)).block();

		Company newCompanyData = new Company("Updated Company",
				updatedDate, updatedWebsite, updatedHeadquarters);

		webTestClient.put()
				.uri("/companies/{id}", Collections.singletonMap("id", company.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(newCompanyData), Company.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.name").isEqualTo("Updated Company")
				.jsonPath("$.website").isEqualTo(updatedWebsite)
				.jsonPath("$.headquarters").isEqualTo(updatedHeadquarters);
	}

	@Test
	public void testDeleteCompany() {
		Company company = companyRepository.save(new Company("To be deleted", new Date())).block();

		webTestClient.delete()
				.uri("/companies/{id}", Collections.singletonMap("id",  company.getId()))
				.exchange()
				.expectStatus().isOk();
	}

}
