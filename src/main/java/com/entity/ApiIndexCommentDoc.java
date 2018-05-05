package com.entity;

import javax.persistence.*;

/**
 * Created by flyboss on 2018/4/21.
 */
@Entity
@Table(name = "api_index_comment_doc")
public class ApiIndexCommentDoc {
    @Column(name = "api_index_comment_doc_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "api_index_comment_id")
    private ApiIndexComment apiIndexComment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doc_id")
    private Doc doc;

    @Column(name = "term_frequency")
    private Double termFrequency;

    public ApiIndexCommentDoc() {
    }

    public ApiIndexCommentDoc(ApiIndexComment apiIndexComment, Doc doc, Double termFrequency) {
        this.apiIndexComment = apiIndexComment;
        this.doc = doc;
        this.termFrequency = termFrequency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ApiIndexComment getApiIndexComment() {
        return apiIndexComment;
    }

    public void setApiIndexComment(ApiIndexComment apiIndexComment) {
        this.apiIndexComment = apiIndexComment;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public Double getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(Double termFrequency) {
        this.termFrequency = termFrequency;
    }

    @Override
    public String toString() {
        return "ApiIndexCommentDoc{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiIndexCommentDoc)) return false;

        ApiIndexCommentDoc that = (ApiIndexCommentDoc) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
