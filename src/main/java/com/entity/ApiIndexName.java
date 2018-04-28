package com.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/18.
 */
@Entity
@Table(name = "api_index_name")
public class ApiIndexName {
    @Column(name = "api_index_name_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "api_index_name")
    private String name;

    @Column(name = "idf")
    private Double idf;

    @OneToMany(mappedBy = "apiIndexName")
    private Set<ApiIndexNameDoc> apiIndexNameDocs = new HashSet<ApiIndexNameDoc>();



    public ApiIndexName() {
    }

    public ApiIndexName(String name) {
        this.name = name;
    }

    public ApiIndexName(String name, Double idf) {
        this.name = name;
        this.idf = idf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Double getIdf() {
        return idf;
    }

    public void setIdf(Double idf) {
        this.idf = idf;
    }

    public Set<ApiIndexNameDoc> getApiIndexNameDocs() {
        return apiIndexNameDocs;
    }

    public void setApiIndexNameDocs(Set<ApiIndexNameDoc> apiIndexNameDocs) {
        this.apiIndexNameDocs = apiIndexNameDocs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiIndexName)) return false;

        ApiIndexName that = (ApiIndexName) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ApiIndexName{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idf=" + idf +
                ", apiIndexNameDocs=" + apiIndexNameDocs +
                '}';
    }
}
