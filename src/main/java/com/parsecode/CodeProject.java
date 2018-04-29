package com.parsecode;

import com.dao.DaoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by flyboss on 2018/4/25.
 */
public class CodeProject {
    private static Logger logger = LogManager.getLogger(CodeProject.class);

    private static final String projectRootPath="F:/codewarehouse/";
    private static final String javaUrl = "https://github.com/topics/java";
    public static void main(String[] args) {
        CodeProject codeProject = new CodeProject();
//        DaoUtil.truncateTable("code");
//        DaoUtil.truncateTable("code_doc");
//        DaoUtil.truncateTable("func_index");
//        DaoUtil.truncateTable("func_index_code");
//
//        List<File> files=codeProject.getFilesFromProject(projectRootPath);
//
//        ParseCode parseCode = new ParseCode();
//        for (File file : files) {
//            parseCode.run(file.getAbsolutePath());
//        }
        codeProject.pullBranchToLocal("https://github.com/iluwatar/java-design-patterns.git");
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

    public void pullBranchToLocal(String remoteURL){
        String projectName = remoteURL.substring(remoteURL.lastIndexOf("/")+1, remoteURL.lastIndexOf("."));
        File localPath = new File(projectRootPath+projectName);
        if (localPath.exists()){
            logger.error(projectName+" exists");
        }else{
            if (localPath.mkdir()){
                System.out.println(localPath+" success");
            }
        }
        try {
            Git result = Git.cloneRepository()
                    .setURI(remoteURL)
                    .setDirectory(localPath)
                    .call();
            logger.info("download "+projectName+" succeed");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("download "+projectName+" fail");
        }
    }

    private void crawlProject(){
        final String baseUrl="https://github.com/";
        try {
            Document doc = Jsoup.connect(javaUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
