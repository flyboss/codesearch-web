package com.Search;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Created by flyboss on 2018/5/12.
 */
public class ApiSearchThread implements Callable<Map<Integer,Double>>{
    private ApiSearch apiSearch;
    private String searchSentence;
    public ApiSearchThread(ApiSearch apiSearch,String searchSentence)
    {
        this.apiSearch=apiSearch;
        this.searchSentence=searchSentence;
    }

    @Override
    public Map<Integer, Double> call() throws Exception {
        try {
            return apiSearch.vsm(searchSentence);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
