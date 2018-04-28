package com.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyboss on 2018/4/6.
 */
public class ParseDocUtil {
    private final String docFilepath="D:\\docs\\api\\";


    /**
     * get all the classes url
     * @return
     */
    public List<String> get(){
        File input = new File("D:\\docs\\api\\allclasses-frame.html");
        List<String> list=new ArrayList<String>();
        try{
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Element indexContainer=doc.getElementsByClass("indexContainer").first().child(0);
            for (Element e:indexContainer.children()) {
                list.add(docFilepath+e.child(0).attr("href"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
