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
@Table(name = "api_index_comment")
public class ApiIndexComment {
    @Column(name = "api_index_comment_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "api_index_comment")
    private String comment;

    @Column(name = "idf")
    private Double idf;

    @OneToMany(mappedBy = "apiIndexComment")
    private Set<ApiIndexCommentDoc> apiIndexCommentDocs = new HashSet<ApiIndexCommentDoc>();

    public ApiIndexComment() {
    }

    public ApiIndexComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getIdf() {
        return idf;
    }

    public void setIdf(Double idf) {
        this.idf = idf;
    }

    public Set<ApiIndexCommentDoc> getApiIndexCommentDocs() {
        return apiIndexCommentDocs;
    }

    public void setApiIndexCommentDocs(Set<ApiIndexCommentDoc> apiIndexCommentDocs) {
        this.apiIndexCommentDocs = apiIndexCommentDocs;
    }
}
