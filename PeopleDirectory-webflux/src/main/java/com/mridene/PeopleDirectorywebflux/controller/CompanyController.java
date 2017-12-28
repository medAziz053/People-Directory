package com.mridene.PeopleDirectorywebflux.controller;

import com.mridene.PeopleDirectorywebflux.model.Company;
import com.mridene.PeopleDirectorywebflux.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public Flux<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @PostMapping("/companies")
    public Mono<Company> createCompanies(@Valid @RequestBody Company company) {
        return companyRepository.save(company);
    }

    @GetMapping("/companies/{id}")
    public Mono<ResponseEntity<Company>> getCompanyById(@PathVariable(value = "id") String companyId) {
        return companyRepository.findById(companyId)
                .map(savedCompany -> ResponseEntity.ok(savedCompany))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/companies/{id}")
    public Mono<ResponseEntity<Company>> updateCompany(@PathVariable(value = "id") String companyId,
                                                   @Valid @RequestBody Company company) {
        return companyRepository.findById(companyId)
                .flatMap(existingCompany -> {
                    existingCompany.setName(company.getName());
                    existingCompany.setCreatedAt(company.getCreatedAt());
                    existingCompany.setWebsite(company.getWebsite());
                    existingCompany.setHeadquarters(company.getHeadquarters());
                    return companyRepository.save(existingCompany);
                })
                .map(updatedCompany -> new ResponseEntity<>(updatedCompany, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/companies/{id}")
    public Mono<ResponseEntity<Void>> deleteCompany(@PathVariable(value = "id") String companyId) {

        return companyRepository.findById(companyId)
                .flatMap(existingCompany ->
                        companyRepository.delete(existingCompany)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Companies are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/companies", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Company> streamAllCompanies() {
        return companyRepository.findAll();
    }
}
