package com.Search;

import com.dao.ApiIndexCommentDao;
import com.dao.ApiIndexCommentDocDao;
import com.entity.ApiIndexComment;
import com.entity.ApiIndexCommentDoc;
import com.entity.Doc;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * Created by flyboss on 2018/4/23.
 */
public class ApiCommentSearch implements ApiSearch{
    public Map<Integer,Double> vsm(String searchSentence){
        List<ApiIndexComment> searchApiIndexComment = new ArrayList<ApiIndexComment>();
        double[] vector=generateVector(searchSentence,searchApiIndexComment);
        Map<Doc, double[]> vectors=search(searchApiIndexComment);
        return vsm(vector, vectors);
    }

    private double[] generateVector(String searchSentence,List<ApiIndexComment> searchApiIndexComment){
        String[] searchWords =searchSentence.split(" ");
        Map<ApiIndexComment, Integer> searchWordsCount = new HashMap<ApiIndexComment, Integer>();
        ApiIndexCommentDao apiIndexCommentDao = new ApiIndexCommentDao();
        for (String s: searchWords) {
            ApiIndexComment apiIndexComment=apiIndexCommentDao.find(s);
            if (apiIndexComment==null){
                continue;
            }
            if (searchWordsCount.containsKey(apiIndexComment)){
                searchWordsCount.put(apiIndexComment,searchWordsCount.get(apiIndexComment)+1);
            }else {
                searchWordsCount.put(apiIndexComment, 1);
                searchApiIndexComment.add(apiIndexComment);
            }
        }
        double[] vector = new double[searchApiIndexComment.size()];
        for (int i = 0; i< searchApiIndexComment.size(); i++) {
            double idf= searchApiIndexComment.get(i).getIdf();
            double counts=searchWordsCount.get(searchApiIndexComment.get(i));
            double tf=counts/searchWords.length;
            vector[i] =tf*idf;
        }
        return vector;
    }

    private Map<Doc, double[]> search(List<ApiIndexComment> searchApiIndexComment){
        ApiIndexCommentDocDao apiIndexCommentDocDao = new ApiIndexCommentDocDao();
        Map<Doc, double[]> vectors = new HashMap<Doc, double[]>();
        for(int i=0;i<searchApiIndexComment.size();i++){
            List<ApiIndexCommentDoc> apiIndexCommentDocs=apiIndexCommentDocDao.findByNameId(searchApiIndexComment.get(i).getId());
            for(ApiIndexCommentDoc apiIndexCommentDoc:apiIndexCommentDocs){
                if (vectors.containsKey(apiIndexCommentDoc.getDoc())){
                    vectors.get(apiIndexCommentDoc.getDoc())[i]=apiIndexCommentDoc.getTermFrequency();
                }else{
                    double[] doubles=new double[searchApiIndexComment.size()];
                    doubles[i] = apiIndexCommentDoc.getTermFrequency();
                    vectors.put(apiIndexCommentDoc.getDoc(), doubles);
                }
            }
        }
        return vectors;
    }

    private Map<Integer,Double> vsm(double[] vector,Map<Doc, double[]> vectors){
        double length=0;
        for (int i=0;i<vector.length;i++) {
            length+=vector[i]*vector[i];
        }
        double vectorLength = Math.sqrt(length);

        Map<Integer, Double> apiComment = new HashMap<Integer, Double>();

        for (Map.Entry<Doc,double[]> entry: vectors.entrySet()) {
            double molecular=SearchUtil.countVectorDistance(vector,entry.getValue());
            //TODO this is very stupid getCommentLength  getNameLength
            double denominator = vectorLength * entry.getKey().getCommentLength();

            apiComment.put(entry.getKey().getId(),molecular/denominator);
        }
        return SearchUtil.sortByValue(apiComment);
    }

}
