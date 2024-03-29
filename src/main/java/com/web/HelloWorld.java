package com.web;

/**
 * Created by flyboss on 2018/4/28.
 */

import com.Search.Search;
import com.alibaba.fastjson.JSON;
import com.entity.Code;
import com.entity.SampleFunc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SampleFunc[] sampleFuncs=null;
        try {
            resp.setContentType("application/json; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            Search search = new Search();
            String searchWords = req.getParameter("searchWords");
            searchWords = searchWords.replaceAll("/+", " ");
            List<Code> codes = search.run(searchWords);
            sampleFuncs = new SampleFunc[codes.size()];
            for (int i = 0; i < codes.size(); i++) {
                sampleFuncs[i] = new SampleFunc(codes.get(i).getOriginBody(), codes.get(i).getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            if (sampleFuncs==null||sampleFuncs.length==0){
                out.write("");
            }else{

                out.write(JSON.toJSONString(sampleFuncs));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
