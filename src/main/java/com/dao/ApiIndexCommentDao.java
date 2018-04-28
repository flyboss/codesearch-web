package com.dao;

import com.entity.ApiIndexComment;
import com.main.Main;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by flyboss on 2018/4/18.
 */
public class ApiIndexCommentDao {
    public ApiIndexComment add(ApiIndexComment apiIndexComment) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(apiIndexComment);
            transaction.commit();
            return apiIndexComment;
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

    public ApiIndexComment find(Integer id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            ApiIndexComment apiIndexComment = session.find(ApiIndexComment.class, id);
            return apiIndexComment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public ApiIndexComment find(String comment) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from ApiIndexComment where comment = :comment");
            query.setParameter("comment", comment);
            List<ApiIndexComment> apiIndexComments = query.getResultList();
            if (apiIndexComments.size() == 0) {
                return null;
            } else {
                return apiIndexComments.get(0);
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

    public void update(ApiIndexComment apiIndexComment) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(apiIndexComment);
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
