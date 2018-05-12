package com.main;

import com.dao.FuncIndexDao;
import com.entity.FuncIndex;
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
        FuncIndexDao funcIndexDao = new FuncIndexDao();
        String s="adsfasdfasdfasdfa";
        funcIndexDao.add(new FuncIndex(s));
    }

}
