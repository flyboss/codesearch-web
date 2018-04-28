package com.dao;

import com.entity.ApiIndexName;
import com.entity.Doc;
import com.main.Main;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;

/**
 * Created by flyboss on 2018/4/18.
 */
public class ApiIndexNameDao {
    public ApiIndexName add(ApiIndexName apiIndexName) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(apiIndexName);
            transaction.commit();
            return apiIndexName;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public ApiIndexName find(Integer id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            ApiIndexName apiIndexName = session.find(ApiIndexName.class, id);
            return apiIndexName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public ApiIndexName find(String name) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            if (session == null) {
                System.out.println("null");
            }
            Query query = session.createQuery("from ApiIndexName where name = :name");
            query.setParameter("name", name);
            List<ApiIndexName> apiIndexNames = query.getResultList();
            if (apiIndexNames.size() == 0) {
                return null;
            } else {
                return apiIndexNames.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(ApiIndexName apiIndexName) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(apiIndexName);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
