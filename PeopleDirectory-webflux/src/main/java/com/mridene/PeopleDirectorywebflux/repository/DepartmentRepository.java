package com.mridene.PeopleDirectorywebflux.repository;

import com.mridene.PeopleDirectorywebflux.model.Department;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends ReactiveMongoRepository<Department, String> {

}
