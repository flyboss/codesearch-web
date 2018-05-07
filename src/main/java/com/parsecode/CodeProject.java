package com.parsecode;

import com.dao.DaoUtil;
import com.dao.ProjectDao;
import com.entity.Project;
import com.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    //presto has some problem
    public static void main(String[] args) {
        CodeProject codeProject = new CodeProject();
        codeProject.crawlProject();
    }

    private void crawlProject(){
        final String githubJava="/github.html";
        try {
            File file = new File(this.getClass().getResource(githubJava).getPath());
            Document doc = Jsoup.parse(file, "UTF-8", "https://github.com/");
            Elements articles = doc.select("article");
            for (int i = 0; i < 100; i++) {
                try{
                    String projectUrl=articles.get(i).child(0).child(0).child(0).absUrl("href");
                    Document project = Jsoup.connect(projectUrl).get();
                    String branch=project.getElementById("js-repo-pjax-container").child(1).child(0).child(4).child(2).child(0).child(1).text();
                    pullBranchToLocal(projectUrl,branch);
                }catch (Exception e){
                    logger.error(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullBranchToLocal(String remoteURL,String branch){
        String projectName = remoteURL.substring(remoteURL.lastIndexOf("/")+1);
        ProjectDao projectDao = new ProjectDao();
        if(projectDao.find(projectName)!=null){
            return;
        }

        File localPath = new File(projectRootPath+projectName);
        if (localPath.exists()){
            System.out.println(localPath+" exist");
            logger.error(localPath+" exist");
            return;
        }else{
            if (localPath.mkdir()){
                System.out.println(localPath+" mkdir");
                logger.info(localPath+" mkdir");
            }
        }
        try {
            Git result = Git.cloneRepository()
                    .setURI(remoteURL+".git")
                    .setDirectory(localPath)
                    .call();
            logger.info("download "+projectName+" succeed");
            Project project=new Project(projectName,remoteURL+"/tree/"+branch+"/",projectRootPath+projectName);
            projectDao.add(project);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("download "+projectName +" fail");
        }
    }
}
