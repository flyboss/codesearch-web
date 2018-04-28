package com.entity;

import javax.persistence.*;

/**
 * Created by flyboss on 2018/4/25.
 */
@Entity
@Table(name = "func_index_code")
public class FuncIndexCode {
    @Column(name = "func_index_code_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "term_frequency")
    private Double termFrequency;

    @Column(name = "is_name")
    private boolean isName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "func_index_id")
    private FuncIndex funcIndex;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "code_id")
    private Code code;

    public FuncIndexCode() {
    }

    public FuncIndexCode(FuncIndex funcIndex, Code code,boolean isName,Double termFrequency) {
        this.termFrequency = termFrequency;
        this.funcIndex = funcIndex;
        this.code = code;
        this.isName = isName;
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

    public boolean isName() {
        return isName;
    }

    public void setName(boolean name) {
        isName = name;
    }

    public FuncIndex getFuncIndex() {
        return funcIndex;
    }

    public void setFuncIndex(FuncIndex funcIndex) {
        this.funcIndex = funcIndex;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
