package com.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/25.
 */
@Entity
@Table(name = "func_index")
public class FuncIndex {
    @Column(name = "func_index_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "word")
    private String word;

    @Column(name = "name_idf")
    private double nameIdf;

    @Column(name = "body_idf")
    private double bodyIdf;

    @OneToMany(mappedBy = "funcIndex")
    private Set<FuncIndexCode> funcIndexCodes = new HashSet<FuncIndexCode>();

    public FuncIndex() {
    }

    public FuncIndex(String word) {
        this.word = word;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getNameIdf() {
        return nameIdf;
    }

    public void setNameIdf(Double nameIdf) {
        this.nameIdf = nameIdf;
    }

    public Double getBodyIdf() {
        return bodyIdf;
    }

    public void setBodyIdf(Double bodyIdf) {
        this.bodyIdf = bodyIdf;
    }

    public Set<FuncIndexCode> getFuncIndexCodes() {
        return funcIndexCodes;
    }

    public void setFuncIndexCodes(Set<FuncIndexCode> funcIndexCodes) {
        this.funcIndexCodes = funcIndexCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuncIndex)) return false;

        FuncIndex funcIndex = (FuncIndex) o;

        return getId().equals(funcIndex.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
