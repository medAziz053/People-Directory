package com.mridene.PeopleDirectorywebflux.model;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document(collection = "companies")
public class Company {
    @Id
    private String id;

    @NotBlank
    @Size(max = 80)
    private String name;

    @NotNull
    private Date createdAt;

    private String website;

    private String headquarters;

    public Company () {

    }

    public Company (String name) {
        this.name = name;
        this.createdAt = new Date();
    }

    public Company (String name, Date createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public Company (String name, Date createdAt, String website, String headquarters) {
        this.name = name;
        this.createdAt = createdAt;
        this.website = website;
        this.headquarters = headquarters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", website='" + website + '\'' +
                ", headquarters='" + headquarters + '\'' +
                '}';
    }
}
