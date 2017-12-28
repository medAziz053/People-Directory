package com.mridene.PeopleDirectorywebflux.repository;

import com.mridene.PeopleDirectorywebflux.model.Company;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends ReactiveMongoRepository<Company, String> {

}
