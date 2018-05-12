package com.Search;

import com.dao.ApiIndexNameDao;
import com.dao.ApiIndexNameDocDao;
import com.entity.ApiIndexName;
import com.entity.ApiIndexNameDoc;
import com.entity.Doc;

import java.util.*;

/**
 * Created by flyboss on 2018/4/23.
 */
public class ApiNameSearch implements ApiSearch{

    public Map<Integer,Double> vsm(String searchSentence){
        List<ApiIndexName> searchApiIndexName = new ArrayList<ApiIndexName>();
        double[] vector=generateVector(searchSentence,searchApiIndexName);
        Map<Doc, double[]> vectors=search(searchApiIndexName);
        return vsm(vector, vectors);
    }

    /**
     *
     *
     */
    private double[] generateVector(String searchSentence,List<ApiIndexName> searchApiIndexName){
        String[] searchWords =searchSentence.split(" ");
        Map<ApiIndexName, Integer> searchWordsCount = new HashMap<ApiIndexName, Integer>();
        ApiIndexNameDao apiIndexNameDao = new ApiIndexNameDao();
        for (String s: searchWords) {
            ApiIndexName apiIndexName=apiIndexNameDao.find(s);
            if (apiIndexName==null){
                continue;
            }
            if (searchWordsCount.containsKey(apiIndexName)){
                searchWordsCount.put(apiIndexName,searchWordsCount.get(apiIndexName)+1);
            }else {
                searchWordsCount.put(apiIndexName, 1);
                searchApiIndexName.add(apiIndexName);
            }
        }
        double[] vector = new double[searchApiIndexName.size()];
        for (int i = 0; i< searchApiIndexName.size(); i++) {
            try {
                double idf = searchApiIndexName.get(i).getIdf();
                double counts = searchWordsCount.get(searchApiIndexName.get(i));
                double tf = counts / searchWords.length;
                vector[i] = tf * idf;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return vector;
    }

    private Map<Doc, double[]> search(List<ApiIndexName> searchApiIndexName){
        ApiIndexNameDocDao apiIndexNameDocDao = new ApiIndexNameDocDao();
        Map<Doc, double[]> vectors = new HashMap<Doc, double[]>();
        for(int i=0;i<searchApiIndexName.size();i++){
            List<ApiIndexNameDoc> apiIndexNameDocs=apiIndexNameDocDao.findByNameId(searchApiIndexName.get(i).getId());
            for(ApiIndexNameDoc apiIndexNameDoc:apiIndexNameDocs){
                if (vectors.containsKey(apiIndexNameDoc.getDoc())){
                    vectors.get(apiIndexNameDoc.getDoc())[i]=apiIndexNameDoc.getTermFrequency();
                }else{
                    double[] doubles=new double[searchApiIndexName.size()];
                    doubles[i] = apiIndexNameDoc.getTermFrequency();
                    vectors.put(apiIndexNameDoc.getDoc(), doubles);
                }
            }
        }
        return vectors;
    }

    private Map<Integer, Double> vsm(double[] vector,Map<Doc, double[]> vectors){
        double length=0;
        for (int i=0;i<vector.length;i++) {
            length+=vector[i]*vector[i];
        }
        double vectorLength = Math.sqrt(length);

        Map<Integer, Double> apiText = new HashMap<Integer, Double>();

        for (Map.Entry<Doc,double[]> entry: vectors.entrySet()) {
            double molecular=SearchUtil.countVectorDistance(vector,entry.getValue());
            double denominator = vectorLength * entry.getKey().getNameLength();
            apiText.put(entry.getKey().getId(),molecular/denominator);
        }
        return SearchUtil.sortByValue(apiText);
    }
}
