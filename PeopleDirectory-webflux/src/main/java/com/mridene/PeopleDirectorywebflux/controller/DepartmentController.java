package com.mridene.PeopleDirectorywebflux.controller;

import com.mridene.PeopleDirectorywebflux.model.Department;
import com.mridene.PeopleDirectorywebflux.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/departments")
    public Flux<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @PostMapping("/departments")
    public Mono<Department> createDepartments(@Valid @RequestBody Department department) {
        return departmentRepository.save(department);
    }

    @GetMapping("/departments/{id}")
    public Mono<ResponseEntity<Department>> getDepartmentById(@PathVariable(value = "id") String departmentId) {
        return departmentRepository.findById(departmentId)
                .map(savedDepartment -> ResponseEntity.ok(savedDepartment))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/departments/{id}")
    public Mono<ResponseEntity<Department>> updateDepartment(@PathVariable(value = "id") String departmentId,
                                                       @Valid @RequestBody Department department) {
        return departmentRepository.findById(departmentId)
                .flatMap(existingDepartment -> {
                    existingDepartment.setName(department.getName());
                    return departmentRepository.save(existingDepartment);
                })
                .map(updatedCompany -> new ResponseEntity<>(updatedCompany, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/departments/{id}")
    public Mono<ResponseEntity<Void>> deleteDepartment(@PathVariable(value = "id") String departmentId) {

        return departmentRepository.findById(departmentId)
                .flatMap(existingDepartment ->
                        departmentRepository.delete(existingDepartment)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Departments are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/departments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Department> streamAllDepartments() {
        return departmentRepository.findAll();
    }
}
