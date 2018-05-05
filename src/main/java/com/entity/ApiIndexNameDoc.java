package com.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/20.
 */
@Entity
@Table(name = "api_index_name_doc")
public class ApiIndexNameDoc {
    @Column(name = "api_index_name_doc_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "term_frequency")
    private Double termFrequency;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "api_index_name_id")
    private ApiIndexName apiIndexName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doc_id")
    private Doc doc;

    public ApiIndexNameDoc() {
    }


    public ApiIndexNameDoc(ApiIndexName apiIndexName, Doc doc, Double termFrequency) {
        this.termFrequency = termFrequency;
        this.apiIndexName = apiIndexName;
        this.doc = doc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(Double termFrequency) {
        this.termFrequency = termFrequency;
    }

    public ApiIndexName getApiIndexName() {
        return apiIndexName;
    }

    public void setApiIndexName(ApiIndexName apiIndexName) {
        this.apiIndexName = apiIndexName;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiIndexNameDoc)) return false;

        ApiIndexNameDoc that = (ApiIndexNameDoc) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
