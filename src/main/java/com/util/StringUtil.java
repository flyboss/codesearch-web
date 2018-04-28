package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/17.
 */
public class StringUtil {
    //    public static String dispose(String text){
//        Document doc = new Document(text);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Sentence sent : doc.sentences()) {
//            for(int i = 0; i < sent.words().size(); i++) {
//                if (sent.posTag(i) != null&&sent.lemma(i)!=null ) {
//                    if(sent.posTag(i).contains("NN")||sent.posTag(i).contains("JJ")||sent.posTag(i).contains("VB")){
//                        stringBuilder.append(sent.lemma(i)+" ");
//                    }
//                }
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    public static String trimFunctionName(String name){
//
//    }
    private static Set<String> stopWords = new HashSet<String>();
    private static Set<String> keywords = new HashSet<String>();

    private static Stemmer stemmer = new Stemmer();

    static {
        readProperties(stopWords, "/english.txt");
        readProperties(keywords, "/keyword.txt");
    }

    public static void main(String[] args) {
        String s = "happiness";
        System.out.println(stemming(s));
    }

    public static String deleteStopWordsAndKeyword(String origin) {
        String s = delete(origin, stopWords);
        return delete(s, keywords);
    }

    public static String preDispose(String string) {
        string = StringUtil.replaceAllPunctuation(string);
        string = StringUtil.trimCamel(string);
        string = StringUtil.deleteStopWordsAndKeyword(string);
        string = StringUtil.stemming(string);
        return string;
    }

    public static String stemming(String origin) {
        String[] strings = origin.toLowerCase().split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stemmer.add(s.toCharArray(), s.length());
            stemmer.stem();
            stringBuilder.append(stemmer.toString() + " ");
        }
        return stringBuilder.toString();
    }


    public static String deleteStopWord(String origin) {
        return delete(origin, stopWords);
    }

    public static String deleteKeyword(String origin) {
        return delete(origin, keywords);
    }

    public static String trimCamel(String name) {
        if (name == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int j;
        for (int i = 0; i < name.length(); i++) {
            j=i+1;
            if (Character.isLowerCase(name.charAt(i))&&j<name.length()&&Character.isUpperCase(name.charAt(j))) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(name.charAt(i));
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static String replaceAllPunctuation(String string) {
        return string.replaceAll("\\p{Punct}", " ");
    }

    private static void readProperties(Set<String> set, String filename) {
        File file = new File(StringUtil.class.getResource(filename).getPath());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                set.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String delete(String origin, Set<String> set) {
        if (origin == null) {
            return "";
        }
        String[] texts = origin.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            if (!set.contains(texts[i])) {
                stringBuilder.append(texts[i] + " ");
            }
        }
        return stringBuilder.toString().toLowerCase();
    }


}
