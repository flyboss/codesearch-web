package com.web;

/**
 * Created by flyboss on 2018/4/28.
 */
import com.Search.Search;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应内容类型
        resp.setContentType("text/html");

        //设置逻辑实现
        PrintWriter out = resp.getWriter();
        Search search = new Search();
        out.println("<h1>" + search.helloWorld() + "</h1>");
    }
}
