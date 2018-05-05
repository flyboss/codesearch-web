package com.entity;


import com.util.StringUtil;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flyboss on 2018/3/30.
 */
@Entity
@Table(name = "doc")
public class Doc {
    @Column(name = "doc_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "original_comment")
    private String originalComment;

    @Column(name = "search_comment")
    private String searchComment;

    @Column(name="comment_word_count")
    private int commentWordCount;

    @Column(name = "calledcount")
    private Integer calledCount;

    @Column(name = "package")
    private String docsPackage;

    @Column(name = "class")
    private String docsClass;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "search_name")
    private String searchName;

    @Column(name="name_word_count")
    private int nameWordCount;

    @Column(name = "args")
    private String docsArgs;

    @Column(name = "name_length")
    private Double nameLength;

    @Column(name = "comment_length")
    private Double commentLength;

    @ManyToMany(mappedBy = "docs")
    private Set<Code> codes = new HashSet<Code>();

    @OneToMany(mappedBy = "doc")
    private Set<ApiIndexNameDoc> apiIndexNameDocs = new HashSet<ApiIndexNameDoc>();

    @OneToMany(mappedBy = "doc")
    private Set<ApiIndexCommentDoc> apiIndexCommentDocs = new HashSet<ApiIndexCommentDoc>();

    public Doc() {
    }

    public Doc(String docsPackage, String docsClass, String originalName,  String originalComment,String docsArgs) {
        this.docsPackage = docsPackage;
        this.docsClass = docsClass;
        this.originalName = originalName;
        this.originalComment = originalComment;
        this.docsArgs = docsArgs;
        this.calledCount = 0;
        this.fullName = docsPackage + "." + docsClass + "." + originalName;
        this.searchName =StringUtil.preDispose(this.fullName);
        this.nameWordCount = this.searchName.split(" ").length;
        this.searchComment = StringUtil.preDispose(this.originalComment);
        this.commentWordCount=this.searchComment.split(" ").length;
    }

    public Doc(String docsPackage, String docsClass, String originalName, String searchName, String originalComment, String searchComment, String docsArgs) {
        this.docsPackage = docsPackage;
        this.docsClass = docsClass;
        this.originalName = originalName;
        this.searchName = searchName;
        this.originalComment = originalComment;
        this.searchComment = searchComment;
        this.docsArgs = docsArgs;
        this.fullName = docsPackage + " " + docsClass + " " + originalName;
        this.commentWordCount=this.searchComment.split(" ").length;
        this.nameWordCount = this.searchName.split(" ").length;
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

    public String getOriginalComment() {
        return originalComment;
    }

    public void setOriginalComment(String comment) {
        this.originalComment = comment;
    }

    public Integer getCalledCount() {
        return calledCount;
    }

    public void setCalledCount(Integer calledCount) {
        this.calledCount = calledCount;
    }

    public String getDocsPackage() {
        return docsPackage;
    }

    public void setDocsPackage(String docsPackage) {
        this.docsPackage = docsPackage;
    }

    public String getDocsClass() {
        return docsClass;
    }

    public void setDocsClass(String docsClass) {
        this.docsClass = docsClass;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String name) {
        this.originalName = name;
    }

    public String getDocsArgs() {
        return docsArgs;
    }

    public void setDocsArgs(String docsArgs) {
        this.docsArgs = docsArgs;
    }

    public Set<Code> getCodes() {
        return codes;
    }

    public void setCodes(Set<Code> codes) {
        this.codes = codes;
    }

    public String getSearchComment() {
        return searchComment;
    }

    public void setSearchComment(String comment) {
        this.searchComment = comment;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String name) {
        this.searchName = name;
    }

    public Set<ApiIndexNameDoc> getApiIndexNameDocs() {
        return apiIndexNameDocs;
    }

    public void setApiIndexNameDocs(Set<ApiIndexNameDoc> apiIndexNameDocs) {
        this.apiIndexNameDocs = apiIndexNameDocs;
    }

    public Set<ApiIndexCommentDoc> getApiIndexCommentDocs() {
        return apiIndexCommentDocs;
    }

    public void setApiIndexCommentDocs(Set<ApiIndexCommentDoc> apiIndexCommentDocs) {
        this.apiIndexCommentDocs = apiIndexCommentDocs;
    }

    public int getCommentWordCount() {
        return commentWordCount;
    }

    public void setCommentWordCount(int commentWordCount) {
        this.commentWordCount = commentWordCount;
    }

    public int getNameWordCount() {
        return nameWordCount;
    }

    public void setNameWordCount(int nameWordCount) {
        this.nameWordCount = nameWordCount;
    }

    public Double getNameLength() {
        return nameLength;
    }

    public void setNameLength(Double nameLength) {
        this.nameLength = nameLength;
    }

    public Double getCommentLength() {
        return commentLength;
    }

    public void setCommentLength(Double commentLength) {
        this.commentLength = commentLength;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", comment='" + originalComment + '\'' +
                ", calledCount=" + calledCount +
                ", docsPackage='" + docsPackage + '\'' +
                ", docsClass='" + docsClass + '\'' +
                ", name='" + originalName + '\'' +
                ", docsArgs='" + docsArgs + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doc)) return false;

        Doc doc = (Doc) o;

        return getId().equals(doc.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
