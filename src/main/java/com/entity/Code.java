package com.entity;

import com.util.StringUtil;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/4.
 */
@Entity
@Table(name = "code")
public class Code {
    @Column(name = "code_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "package")
    private String codePackage;

    @Column(name = "class")
    private String codeClass;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "startline")
    private Integer startline;

    @Column(name = "endline")
    private Integer endline;

    @Column(name = "origin_body")
    private String originBody;

    @Column(name = "filepath")
    private String filepath;

    @Column(name = "search_Name")
    private String searchName;

    @Column(name = "search_body")
    private String searchBody;

    @Column(name = "doc")
    private String doc;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "code_doc",
            joinColumns = {@JoinColumn(name = "code_id")},
            inverseJoinColumns = {@JoinColumn(name = "doc_id")})
    private Set<Doc> docs = new HashSet<Doc>();

    public Code() {
    }

    public Code(String codePackage, String codeClass, String originName, Integer startline, Integer endline, String originBody, String filepath, String doc, Set<Doc> docs) {
        this.codePackage = codePackage;
        this.codeClass = codeClass;
        this.originName = originName;
        this.startline = startline;
        this.endline = endline;
        this.originBody = originBody;
        this.filepath = filepath;
        this.doc = doc;
        this.docs = docs;
        this.fullName=codePackage+"."+codeClass+"."+originName;
        this.searchName =StringUtil.preDispose(this.fullName);
        this.searchBody = StringUtil.preDispose(this.doc+" "+this.originBody);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCodePackage() {
        return codePackage;
    }

    public void setCodePackage(String codePackage) {
        this.codePackage = codePackage;
    }

    public String getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(String codeClass) {
        this.codeClass = codeClass;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String name) {
        this.originName = name;
    }

    public Integer getStartline() {
        return startline;
    }

    public void setStartline(Integer startline) {
        this.startline = startline;
    }

    public Integer getEndline() {
        return endline;
    }

    public void setEndline(Integer span) {
        this.endline = span;
    }

    public String getOriginBody() {
        return originBody;
    }

    public void setOriginBody(String codeText) {
        this.originBody = codeText;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Set<Doc> getDocs() {
        return docs;
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchBody() {
        return searchBody;
    }

    public void setSearchBody(String searchBody) {
        this.searchBody = searchBody;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code = (Code) o;

        return id != null ? id.equals(code.id) : code.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", codePackage='" + codePackage + '\'' +
                ", codeClass='" + codeClass + '\'' +
                ", searchName='" + originName + '\'' +
                ", startSpan=" + startline +
                ", length=" + endline +
                ", codeText='" + originBody + '\'' +
                ", filepath='" + filepath + '\'' +
                '}';
    }
}
