package com.parsecode;

import com.dao.DaoUtil;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.util.*;

/**
 * Created by flyboss on 2018/4/25.
 */
public class CodeProject {
    private static final String projectRootPath="F:\\codewarehouse";

    public static void main(String[] args) {
        CodeProject codeProject = new CodeProject();
        DaoUtil.truncateTable("code");
        DaoUtil.truncateTable("code_doc");
        DaoUtil.truncateTable("func_index");
        DaoUtil.truncateTable("func_index_code");

        List<File> files=codeProject.getFilesFromProject(projectRootPath);

        ParseCode parseCode = new ParseCode();
        for (File file : files) {
            parseCode.run(file.getAbsolutePath());
        }
    }

    //TODO 获得准确的非test java文件
    public List<File> getFilesFromProject(String projectPath){
        Queue<File> folders = new LinkedList<File>();
        List<File> javaFiles = new ArrayList<File>();
        folders.offer(new File(projectPath));
        while (folders.size() != 0) {
            File folder = folders.poll();
            File[] files = folder.listFiles();
            for (File file : files) {
                if(file.isDirectory()){
                    if (!file.getName().contains("test")){
                        folders.offer(file);
                    }
                }else if (file.getName().endsWith(".java")){
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }


}
