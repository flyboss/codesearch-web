package com.entity;

/**
 * Created by flyboss on 2018/4/30.
 */
public class SampleFunc {
    private String funcCode;
    private String url;

    public SampleFunc() {
    }

    public SampleFunc(String funcCode, String url) {
        this.funcCode = funcCode;
        this.url = url;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
