package com.parsedoc;

import com.dao.*;
import com.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flyboss on 2018/4/18.
 */
public class ExtractIndex {
    public static void main(String[] args) {
//        ExtractIndex extractIndex = new ExtractIndex();
//        DocDao docDao = new DocDao();
//        List<Doc> docs = docDao.getAll();
//        //extractIndex.extractApiIndexName(docs);
//        extractIndex.extractApiIndexComment(docs);
    }

//    public void extractApiIndexName(List<Doc> docs ){
//        ApiIndexNameDao apiIndexNameDao = new ApiIndexNameDao();
//        ApiIndexNameDocDao apiIndexNameDocDao = new ApiIndexNameDocDao();
//        System.out.println(docs.size());
//        int n=0;
//        for (Doc doc:docs) {
//            System.out.println(++n);
//            String[] strings=doc.getSearchName().split(" ");
//            Map<ApiIndexName, Integer> tf = new HashMap<ApiIndexName, Integer>();
//            for (String s:strings) {
//                ApiIndexName apiIndexName=apiIndexNameDao.find(s);
//                if (apiIndexName==null) {
//                    apiIndexName = apiIndexNameDao.add(new ApiIndexName(s));
//                }
//                if(tf.containsKey(apiIndexName)){
//                    tf.put(apiIndexName,tf.get(apiIndexName)+1);
//                }else{
//                    tf.put(apiIndexName,1);
//                }
//            }
//            for (Map.Entry<ApiIndexName,Integer> entry:tf.entrySet()){
//                ApiIndexName apiIndexName=entry.getKey();
//                ApiIndexNameDoc apiIndexNameDoc=new ApiIndexNameDoc(apiIndexName,doc,entry.getValue().doubleValue()/doc.getNameWordCount());
//                apiIndexNameDocDao.add(apiIndexNameDoc);
//            }
//        }
//        System.out.println("extractApiIndexName");
//    }
//
//    public void extractApiIndexComment(List<Doc> docs){
//        ApiIndexCommentDao apiIndexCommentDao = new ApiIndexCommentDao();
//        ApiIndexCommentDocDao apiIndexCommentDocDao = new ApiIndexCommentDocDao();
//        System.out.println(docs.size());
//        int n=0;
//        for (Doc doc:docs) {
//            System.out.println(++n);
//            String[] strings=doc.getSearchComment().split(" ");
//            Map<ApiIndexComment, Integer> tf = new HashMap<ApiIndexComment, Integer>();
//            for (String s : strings) {
//                ApiIndexComment apiIndexComment = apiIndexCommentDao.find(s);
//                if (apiIndexComment == null) {
//                    apiIndexComment = apiIndexCommentDao.add(new ApiIndexComment(s));
//                }
//                if (tf.containsKey(apiIndexComment)) {
//                    tf.put(apiIndexComment, tf.get(apiIndexComment) + 1);
//                }else{
//                    tf.put(apiIndexComment, 1);
//                }
//            }
//            for (Map.Entry<ApiIndexComment, Integer> entry : tf.entrySet()) {
//                ApiIndexComment apiIndexComment = entry.getKey();
//                ApiIndexCommentDoc apiIndexCommentDoc = new ApiIndexCommentDoc(apiIndexComment, doc, entry.getValue().doubleValue() / doc.getCommentWordCount());
//                apiIndexCommentDocDao.add(apiIndexCommentDoc);
//            }
//        }
//        System.out.println("extractApiIndexComment");
//    }


}
