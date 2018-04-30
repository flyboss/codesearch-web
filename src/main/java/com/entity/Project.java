package com.entity;

import javax.persistence.*;

/**
 * Created by flyboss on 2018/4/29.
 */
@Entity
@Table(name = "project")
public class Project {
    @Column(name = "project_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "baseurl")
    private String baseUrl;

    @Column(name = "filepath")
    private String filePath;

    @Column(name = "isParse")
    private boolean isParse;

    public Project() {
    }

    public Project(String name, String baseUrl, String filePath) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.filePath = filePath;
        this.isParse=false;
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isParse() {
        return isParse;
    }

    public void setParse(boolean parse) {
        isParse = parse;
    }
}
