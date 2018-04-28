package com.parsedoc;

import com.dao.*;
import com.entity.*;
import com.util.ParseDocUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flyboss on 2018/3/29.
 */
public class Parsedoc {
    private String packageName;
    private String className;
    private final static Logger logger = LogManager.getLogger(Parsedoc.class);
    private static final int docCount = 23750;


    public static void main(String[] args) {
        Parsedoc parsedoc = new Parsedoc();

        DaoUtil.truncateTable("doc");
        ParseDocUtil parseDocUtil = new ParseDocUtil();
        List<String> list = parseDocUtil.get();
        for (String s : list) {
            System.out.println(s);
            parsedoc.parse(s);
        }

        DaoUtil.truncateTable("api_index_comment");
        DaoUtil.truncateTable("api_index_comment_doc");
        DaoUtil.truncateTable("api_index_name");
        DaoUtil.truncateTable("api_index_name_doc");
        parsedoc.calculateApiIndexNameIDF();
        parsedoc.calculateApiIndexCommentIDF();
        parsedoc.countNameVectorLength();
        parsedoc.countCommentVectorLength();
        System.out.println("finish");
    }

    private void parse(String filePath) {
        File input = new File(filePath);
        try {
            Document doc = Jsoup.parse(input, "UTF-8", "");
            if (isAbstractOrInterface(doc)) {
                return;
            }
            System.out.println("\n" + filePath);
            getPackageAndClass(doc);
            if (doc.getElementsByClass("details").size() == 0) {
                return;
            }
            Element details = doc.getElementsByClass("details").first().child(0).child(0);
            parseElement(details, "Constructor");
            parseElement(details, "Method");
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e);
        }
    }

    private void parseElement(Element details, String elementName) {
        for (int i = 0; i < details.children().size(); i++) {
            if (details.child(i).child(0).child(1).text().startsWith(elementName)) {
                Element li = details.child(i).child(0);
                analysis(li);
                return;
            }
        }
    }

    private void analysis(Element rootli) {
        for (Element ul : rootli.children()) {
            if (ul.tag().toString().equals("ul")) {
                Element li = ul.child(0);
                String originalName = li.child(0).text();
                String args = getArgs(li);
                String originalComment = "";
                if (li.children().size() >= 3) {
                    originalComment = li.child(2).text();
                }
                if (originalComment.length() > 900||args.length()>120) {
                    return;
                }
                Doc doc = new Doc(packageName, className, originalName, originalComment, args);
                DocDao docDao = new DocDao();
                docDao.add(doc);

                //calculate tf
                extractApiIndexName(doc);
                extractApiIndexComment(doc);
            }
        }
    }

    private String getArgs(Element li) {
        String args = li.child(1).text();
        System.out.println(args);
        args = args.replaceAll("\\n", "");
        args = args.replaceAll(" ", "");
        args = args.replaceAll("[\\u00A0]+", " ");
        args = args.substring(args.lastIndexOf('(') + 1, args.lastIndexOf(')'));
        System.out.println(args);

        String[] strings;
        if (args.contains("<")) {
            strings = parseAngleBrackets(args);
        } else {
            strings = args.split(",");
        }

        if (strings.length == 1 && strings[0].equals("")) {
            return strings[0];
        }
        StringBuffer stringBuffer = new StringBuffer();
        //filter parameter name
        for (String s : strings) {
            stringBuffer.append(s.substring(0, s.lastIndexOf(" ")) + ", ");
        }
        return stringBuffer.substring(0, stringBuffer.length() - 2);
    }

    private String[] parseAngleBrackets(String s) {
        StringBuffer args = new StringBuffer(s);
        int angleBrackets = 0;
        for (int i = 0; i < args.length(); i++) {
            if (args.charAt(i) == '<') {
                angleBrackets++;
            } else if (args.charAt(i) == '>') {
                angleBrackets--;
            } else if (args.charAt(i) == ',' && angleBrackets == 0) {
                args.setCharAt(i, '@');
            }
        }
        return args.toString().split("@");
    }

    private void getPackageAndClass(Document doc) {
        Element header = doc.getElementsByClass("header").first();
        packageName = header.getElementsByClass("subTitle").last().text();
        className = header.child(header.children().size() - 1).text().replaceFirst("Class ", "");
        System.out.println("package:" + packageName);
        System.out.println("class:" + className);
    }

    private boolean isAbstractOrInterface(Document doc) {
        Element description = doc.getElementsByClass("description").first();
        String[] strings = description.select("pre").first().text().split(" ");
        for (String s : strings) {
            if (s.equals("interface") || s.equals("abstract") || s.equals("@interface")) {
                return true;
            }
        }
        return false;
    }

    private void extractApiIndexName(Doc doc) {
        ApiIndexNameDao apiIndexNameDao = new ApiIndexNameDao();
        ApiIndexNameDocDao apiIndexNameDocDao = new ApiIndexNameDocDao();

        String[] strings = doc.getSearchName().split(" ");
        Map<ApiIndexName, Integer> tf = new HashMap<ApiIndexName, Integer>();
        for (String s : strings) {
            if (s == null | s.length() == 0) {
                continue;
            }
            ApiIndexName apiIndexName = apiIndexNameDao.find(s);
            if (apiIndexName == null) {
                apiIndexName = apiIndexNameDao.add(new ApiIndexName(s));
            }
            if (tf.containsKey(apiIndexName)) {
                tf.put(apiIndexName, tf.get(apiIndexName) + 1);
            } else {
                tf.put(apiIndexName, 1);
            }
        }
        for (Map.Entry<ApiIndexName, Integer> entry : tf.entrySet()) {
            ApiIndexName apiIndexName = entry.getKey();
            ApiIndexNameDoc apiIndexNameDoc = new ApiIndexNameDoc(apiIndexName, doc, entry.getValue().doubleValue() / doc.getNameWordCount());
            apiIndexNameDocDao.add(apiIndexNameDoc);
        }
    }

    private void extractApiIndexComment(Doc doc) {
        ApiIndexCommentDao apiIndexCommentDao = new ApiIndexCommentDao();
        ApiIndexCommentDocDao apiIndexCommentDocDao = new ApiIndexCommentDocDao();

        String[] strings = doc.getSearchComment().split(" ");
        Map<ApiIndexComment, Integer> tf = new HashMap<ApiIndexComment, Integer>();
        for (String s : strings) {
            if (s == null | s.length() == 0) {
                continue;
            }
            ApiIndexComment apiIndexComment = apiIndexCommentDao.find(s);
            if (apiIndexComment == null) {
                apiIndexComment = apiIndexCommentDao.add(new ApiIndexComment(s));
            }
            if (tf.containsKey(apiIndexComment)) {
                tf.put(apiIndexComment, tf.get(apiIndexComment) + 1);
            } else {
                tf.put(apiIndexComment, 1);
            }
        }
        for (Map.Entry<ApiIndexComment, Integer> entry : tf.entrySet()) {
            ApiIndexComment apiIndexComment = entry.getKey();
            ApiIndexCommentDoc apiIndexCommentDoc = new ApiIndexCommentDoc(apiIndexComment, doc, entry.getValue().doubleValue() / doc.getCommentWordCount());
            apiIndexCommentDocDao.add(apiIndexCommentDoc);
        }
    }

    private void calculateApiIndexNameIDF() {
        ApiIndexNameDocDao apiIndexNameDocDao = new ApiIndexNameDocDao();
        List<Object[]> objects = apiIndexNameDocDao.countIDF();
        ApiIndexNameDao apiIndexNameDao = new ApiIndexNameDao();
        for (Object[] object : objects) {
            int id = Integer.parseInt(object[0].toString());
            int count = Integer.parseInt(object[1].toString());
            ApiIndexName apiIndexName = apiIndexNameDao.find(id);
            apiIndexName.setIdf(Math.log10(docCount / count));
            apiIndexNameDao.update(apiIndexName);
        }
        System.out.println("calculate ApiIndexName idf finish");
    }

    private void calculateApiIndexCommentIDF() {
        ApiIndexCommentDocDao apiIndexCommentDocDao = new ApiIndexCommentDocDao();
        List<Object[]> objects = apiIndexCommentDocDao.countIDF();
        ApiIndexCommentDao apiIndexCommentDao = new ApiIndexCommentDao();
        for (Object[] object : objects) {
            int id = Integer.parseInt(object[0].toString());
            int count = Integer.parseInt(object[1].toString());
            ApiIndexComment apiIndexComment = apiIndexCommentDao.find(id);
            apiIndexComment.setIdf(Math.log10(docCount / count));
            apiIndexCommentDao.update(apiIndexComment);
        }
        System.out.println("calculate ApiIndexComment idf finish");
    }

    private void countNameVectorLength() {
        DocDao docDao = new DocDao();
        List<Integer> docsId = docDao.getAllId();
        ApiIndexNameDocDao apiIndexNameDocDao = new ApiIndexNameDocDao();
        for (Integer id : docsId) {
            List<ApiIndexNameDoc> apiIndexNameDocs = apiIndexNameDocDao.findByDocId(id);
            List<Double> word = new ArrayList<Double>();
            for (ApiIndexNameDoc apiIndexNameDoc : apiIndexNameDocs) {
                double tf = apiIndexNameDoc.getTermFrequency();
                double idf = apiIndexNameDoc.getApiIndexName().getIdf();
                word.add(tf * idf);
            }
            Doc doc = docDao.find(id);
            doc.setNameLength(countLength(word));
            docDao.update(doc);
        }
        System.out.println("countNameVectorLength finish");
    }

    private void countCommentVectorLength() {
        DocDao docDao = new DocDao();
        List<Integer> docsId = docDao.getAllId();
        ApiIndexCommentDocDao apiIndexCommentDocDao = new ApiIndexCommentDocDao();
        for (Integer id : docsId) {
            List<ApiIndexCommentDoc> apiIndexCommentDocs = apiIndexCommentDocDao.findByDocId(id);
            List<Double> word = new ArrayList<Double>();
            for (ApiIndexCommentDoc apiIndexCommentDoc : apiIndexCommentDocs) {
                Double tf = apiIndexCommentDoc.getTermFrequency();
                Double idf = apiIndexCommentDoc.getApiIndexComment().getIdf();
                word.add(tf * idf);
            }
            Doc doc = docDao.find(id);
            doc.setCommentLength(countLength(word));
            docDao.update(doc);
        }
        System.out.println("countCommentVectorLength finish");
    }

    private Double countLength(List<Double> list) {
        double temp = 0;
        for (Double d : list) {
            temp += d * d;
        }
        return Math.sqrt(temp);
    }
}


