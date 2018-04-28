package com.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by flyboss on 2018/4/1.
 */
public class HibernateUtil {
    private static Configuration config;
    private static SessionFactory sessionFactory;
    static {
        config=new Configuration().configure();
        sessionFactory=config.buildSessionFactory();
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
