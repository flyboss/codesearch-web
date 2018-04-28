package com.dao;

import com.entity.ApiIndexCommentDoc;
import com.entity.ApiIndexNameDoc;
import com.main.Main;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by flyboss on 2018/4/21.
 */
public class ApiIndexCommentDocDao {
    public ApiIndexCommentDoc add(ApiIndexCommentDoc apiIndexCommentDoc) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(apiIndexCommentDoc);
            transaction.commit();
            return apiIndexCommentDoc;
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

    public List<Object[]> countIDF(){
        Session session = null;
        String hql="select apiIndexComment.id,count(*) FROM ApiIndexCommentDoc group by apiIndexComment" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            List<Object[]> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<ApiIndexCommentDoc> findByDocId(int id) {
        Session session = null;
        String hql="FROM ApiIndexCommentDoc where doc.id=:id" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            query.setParameter("id",id);
            List<ApiIndexCommentDoc> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<ApiIndexCommentDoc> findByNameId(int id){
        Session session = null;
        String hql="FROM ApiIndexCommentDoc where apiIndexComment.id=:id" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            query.setParameter("id",id);
            List<ApiIndexCommentDoc> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
