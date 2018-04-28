package com.main;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by flyboss on 2018/3/21.
 */
public class Main {
    public static Configuration config;
    public static SessionFactory sessionFactory;
    private final static Logger logger = LogManager.getLogger(Main.class);
    static {
        //1.加载hibernate.cfg.xml配置
        config=new Configuration().configure();
        //2.获取SessionFactory
        sessionFactory=config.buildSessionFactory();
    }
    public static void main(String[] args){
        System.out.println("Main.main");
        //logger.info("dd");
        logger.warn("dd");
    }

}
