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


}
